/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is pryvate software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.controllers;

import gameserver.configs.main.CustomConfig;
import gameserver.configs.administration.AdminConfig;
import gameserver.configs.main.GSConfig;
import gameserver.configs.main.GeoDataConfig;
import gameserver.controllers.SummonController.UnsummonType;
import gameserver.controllers.attack.AttackResult;
import gameserver.controllers.attack.AttackUtil;
import gameserver.controllers.movement.MovementType;
import gameserver.dataholders.DataManager;
import gameserver.model.EmotionType;
import gameserver.model.TaskId;
import gameserver.model.alliance.PlayerAllianceEvent;
import gameserver.model.gameobjects.*;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.SkillListEntry;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.state.CreatureVisualState;
import gameserver.model.gameobjects.stats.PlayerGameStats;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.group.GroupEvent;
import gameserver.model.templates.quest.QuestItems;
import gameserver.model.templates.stats.PlayerStatsTemplate;
import gameserver.network.aion.serverpackets.*;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.questEngine.QuestEngine;
import gameserver.questEngine.model.QuestCookie;
import gameserver.restrictions.RestrictionsManager;
import gameserver.services.*;
import gameserver.services.ZoneService.ZoneUpdateMode;
import gameserver.skillengine.SkillEngine;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.HealType;
import gameserver.skillengine.model.Skill;
import gameserver.spawnengine.SpawnEngine;
import gameserver.taskmanager.tasks.PacketBroadcaster.BroadcastMode;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.Executor;
import gameserver.world.World;
import gameserver.world.WorldType;
import gameserver.world.zone.ZoneInstance;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

/**
 * This class is for controlling players.
 *
 * @author -Nemesiss-, ATracer (2009-09-29), xavier, Sarynth
 * @author RotO (Attack-speed hack protection)
 */
public class PlayerController extends CreatureController<Player> {
    private boolean isInShutdownProgress = false;

    private boolean canAutoRevive = true;

    /**
     * Zone update mask
     */
    private volatile byte zoneUpdateMask;

    private long lastAttackMilis = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public void see(VisibleObject object) {
        super.see(object);
        if (object instanceof Player) {
            Player player = (Player) object;
            PacketSendUtility.sendPacket(getOwner(), new SM_PLAYER_INFO(player, getOwner().isEnemyPlayer((Player) object)));
            if (player.getToyPet() != null) {
                Logger.getLogger(PlayerController.class).debug("Player " + getOwner().getName() + " sees " + object.getName() + " that has toypet");
                PacketSendUtility.sendPacket(getOwner(), new SM_PET(3, player.getToyPet()));
            }
            getOwner().getEffectController().sendEffectIconsTo((Player) object);
        } else if (object instanceof Kisk) {
            Kisk kisk = ((Kisk) object);
            PacketSendUtility.sendPacket(getOwner(), new SM_NPC_INFO(getOwner(), kisk));
            if (getOwner().getCommonData().getRace() == kisk.getOwnerRace())
                PacketSendUtility.sendPacket(getOwner(), new SM_KISK_UPDATE(kisk));
        } else if (object instanceof GroupGate) {
            GroupGate groupgate = ((GroupGate) object);
            PacketSendUtility.sendPacket(getOwner(), new SM_NPC_INFO(getOwner(), groupgate));
        } else if (object instanceof Npc) {
            boolean update = false;
            Npc npc = ((Npc) object);

            PacketSendUtility.sendPacket(getOwner(), new SM_NPC_INFO(npc, getOwner()));

            for (int questId : QuestEngine.getInstance().getNpcQuestData(npc.getNpcId()).getOnQuestStart()) {
                if (QuestService.checkStartCondition(new QuestCookie(object, getOwner(), questId, 0))) {
                    if (!getOwner().getNearbyQuests().contains(questId)) {
                        update = true;
                        getOwner().getNearbyQuests().add(questId);
                    }
                }
            }

            if (update)
                updateNearbyQuestList();

            if (npc.hasWalkRoutes()) {
                double distanceToTarget = MathUtil.getDistance(npc.getX(), npc.getY(), npc.getZ(), npc.getMoveController().getTargetX(), npc.getMoveController().getTargetY(), npc.getMoveController().getTargetZ());
                float x2 = (float) (((npc.getMoveController().getTargetX() - npc.getX()) / distanceToTarget) * npc.getMoveController().getSpeed() * 0.5);
                float y2 = (float) (((npc.getMoveController().getTargetY() - npc.getY()) / distanceToTarget) * npc.getMoveController().getSpeed() * 0.5);
                float z2 = (float) (((npc.getMoveController().getTargetZ() - npc.getZ()) / distanceToTarget) * npc.getMoveController().getSpeed() * 0.5);
                byte h2 = (byte) (Math.toDegrees(Math.atan2(y2, x2)) / 3);

                PacketSendUtility.sendPacket(getOwner(), new SM_MOVE(npc.getObjectId(),
                        npc.getX(), npc.getY(), npc.getZ(),
                        x2, y2, z2, h2, MovementType.MOVEMENT_START_KEYBOARD));
            }

        } else if (object instanceof Summon) {
            Summon npc = ((Summon) object);
            PacketSendUtility.sendPacket(getOwner(), new SM_NPC_INFO(npc));
        } else if (object instanceof Gatherable || object instanceof StaticObject) {
            PacketSendUtility.sendPacket(getOwner(), new SM_GATHERABLE_INFO(object));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notSee(VisibleObject object, boolean isOutOfRange) {
        super.notSee(object, isOutOfRange);
        if (object instanceof Npc) {
            boolean update = false;
            for (int questId : QuestEngine.getInstance().getNpcQuestData(((Npc) object).getNpcId()).getOnQuestStart()) {
                if (QuestService.checkStartCondition(new QuestCookie(object, getOwner(), questId, 0))) {
                    if (getOwner().getNearbyQuests().contains(questId)) {
                        update = true;
                        getOwner().getNearbyQuests().remove(getOwner().getNearbyQuests().indexOf(questId));
                    }
                }
            }
            if (update)
                updateNearbyQuestList();
        }

        PacketSendUtility.sendPacket(getOwner(), new SM_DELETE(object, isOutOfRange ? 0 : 15));
    }

    public void updateNearbyQuests() {
        getOwner().getNearbyQuests().clear();

        getOwner().getKnownList().doOnAllNpcs(new Executor<Npc>() {
            @Override
            public boolean run(Npc obj) {
                for (int questId : QuestEngine.getInstance().getNpcQuestData(((Npc) obj).getNpcId()).getOnQuestStart()) {
                    if (QuestService.checkStartCondition(new QuestCookie(obj, getOwner(), questId, 0))) {
                        if (!getOwner().getNearbyQuests().contains(questId)) {
                            getOwner().getNearbyQuests().add(questId);
                        }
                    }
                }
                return true;
            }
        }, true);

        updateNearbyQuestList();
    }


    /**
     * Set zone instance as null (Where no zones defined)
     */
    public void resetZone() {
        getOwner().setZoneInstance(null);
    }

    public void onEnterWorld() {
        //remove abyss transformation if worldtype != abyss && worldtype != balaurea
        for (Effect ef : getOwner().getEffectController().getAbnormalEffects()) {
            if (ef.isAvatar()) {
                if (getOwner().getWorldType() != WorldType.ABYSS && getOwner().getWorldType() != WorldType.BALAUREA) {
                    getOwner().getEffectController().removeEffect(ef.getSkillId());
                    getOwner().getEffectController().removeEffect(ef.getLaunchSkillId());
                    break;

                }
            }
        }

        //remove arena status if not in arena zone
        if (ArenaService.getInstance().isInArena(getOwner()) && !ArenaService.getInstance().isInArenaZone(getOwner()))
            ArenaService.getInstance().unregister(getOwner());
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Should only be triggered from one place (life stats)
     */
    @Override
    public void onDie(Creature lastAttacker) {
        Player player = this.getOwner();

        Creature master = null;
        if (lastAttacker != null)
            master = lastAttacker.getMaster();

        if (master instanceof Player) {
            if (ArenaService.getInstance().isInArena(player)) {
                ArenaService.getInstance().onDie(player, lastAttacker);
                return;
            }
            else if (isDueling((Player) master)) {
                DuelService.getInstance().onDie(player);
                return;
            }
        }

        this.doReward();

        // Effects removed with super.onDie()
        boolean hasSelfRezEffect = player.getReviveController().checkForSelfRezEffect(player) && canAutoRevive;

        super.onDie(lastAttacker);

        if (master instanceof Npc || master == player) {
            if (player.getLevel() > 4)
                player.getCommonData().calculateExpLoss();
        }

        /**
         * Release summon
         */
        Summon summon = player.getSummon();
        if (summon != null)
            summon.getController().release(UnsummonType.UNSPECIFIED);

        if (player.getToyPet() != null)
            ToyPetService.getInstance().dismissPet(player, player.getToyPet().getPetId());

        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, lastAttacker == null ? 0 :
                lastAttacker.getObjectId()), true);

        // SM_DIE Packet
        int kiskTimeRemaining = (player.getKisk() != null ? player.getKisk().getRemainingLifetime() : 0);
        boolean hasSelfRezItem = player.getReviveController().checkForSelfRezItem(player) && canAutoRevive;
        PacketSendUtility.sendPacket(player, new SM_DIE(hasSelfRezEffect, hasSelfRezItem, kiskTimeRemaining));

        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.DIE);
        QuestEngine.getInstance().onDie(new QuestCookie(null, player, 0, 0));
        player.getObserveController().notifyDeath(player);
    }


    @Override
    public void doReward() {
        Player victim = getOwner();
        PvpService.getInstance().doReward(victim);

        // DP reward
        // TODO: Figure out what DP reward should be for PvP
        //int currentDp = winner.getCommonData().getDp();
        //int dpReward = StatFunctions.calculateSoloDPReward(winner, getOwner());
        //winner.getCommonData().setDp(dpReward + currentDp);

    }

    @Override
    public void onRespawn() {
        super.onRespawn();
        startProtectionActiveTask();
    }

    @Override
    public void attackTarget(Creature target) {
        Player player = getOwner();

        /**
         * Check all prerequisites
         */
        if (target == null || !player.canAttack())
            return;

        PlayerGameStats gameStats = player.getGameStats();

        // check player attack Z distance
        if (Math.abs(player.getZ() - target.getZ()) > 6)
            return;

        if (!RestrictionsManager.canAttack(player, target))
            return;

        int attackSpeed = gameStats.getCurrentStat(StatEnum.ATTACK_SPEED);
        long milis = System.currentTimeMillis();
        if (milis - lastAttackMilis < attackSpeed) {
            /**
             * Hack!
             */
            return;
        }
        lastAttackMilis = milis;

        /**
         * notify attack observers
         */
        super.attackTarget(target);

        /**
         * Calculate and apply damage
         */
        List<AttackResult> attackResult = AttackUtil.calculateAttackResult(player, target);

        int damage = 0;
        for (AttackResult result : attackResult) {
            damage += result.getDamage();
        }

        long time = System.currentTimeMillis();
        int attackType = 0; // TODO investigate attack types
        PacketSendUtility.broadcastPacket(player, new SM_ATTACK(player, target, gameStats.getAttackCounter(),
                (int) time, attackType, attackResult), true);

        target.getController().onAttack(player, damage, true);

        gameStats.increaseAttackCounter();
    }

    public void onAttack(Creature creature, int skillId, TYPE type, int damage, int unknown, boolean notifyAttackedObservers) {
        Player player = getOwner();

        if (player.getLifeStats().isAlreadyDead())
            return;

        // Reduce the damage to exactly what is required to ensure death.
        // - Important that we don't include 7k worth of damage when the
        //   creature only has 100 hp remaining. (For AggroList dmg count.)
        if (damage > player.getLifeStats().getCurrentHp())
            damage = player.getLifeStats().getCurrentHp() + 1;

        super.onAttack(creature, skillId, type, damage, notifyAttackedObservers);

        if (player.isInvul() || player.isProtect() || player.isProtectionActive())
            damage = 0;

        player.getLifeStats().reduceHp(damage, creature, false);

        PacketSendUtility.broadcastPacket(player, new SM_ATTACK_STATUS(player, type, skillId, damage, unknown), true);
        if (player.getLifeStats().isAlreadyDead()) {
            player.getController().onDie(creature);
        }
    }

    @Override
    public void onAttack(Creature creature, int skillId, TYPE type, int damage, boolean notifyAttackedObservers) {
        this.onAttack(creature, skillId, type, damage, 0xA6, notifyAttackedObservers);
    }

    /**
     * @param skillId
     * @param targetType
     * @param x
     * @param y
     * @param z
     */
    public void useSkill(int skillId, int targetType, float x, float y, float z) {
        Player player = getOwner();

        Skill skill = SkillEngine.getInstance().getSkillFor(player, skillId, player.getTarget());

        if (skill != null) {
            skill.setTargetType(targetType, x, y, z);
            if (!RestrictionsManager.canUseSkill(player, skill))
                return;

            skill.useSkill();
        }

        skill = null;
    }

    @Override
    public void onMove() {
        super.onMove();
        addZoneUpdateMask(ZoneUpdateMode.ZONE_UPDATE);
    }

    @Override
    public void onStopMove() {
        super.onStopMove();
    }

    @Override
    public void onStartMove() {
        cancelCurrentSkill();
        super.onStartMove();
    }

    /**
     * Perform tasks on Player jumping
     */
    public void onJump() {
        getOwner().getObserveController().notifyJumpObservers();
    }


    /**
     * Cancel current skill and remove cooldown
     */
    public void cancelCurrentSkill() {
        Player player = getOwner();
        Skill castingSkill = player.getCastingSkill();
        if (castingSkill != null) {
            int skillId = castingSkill.getSkillTemplate().getSkillId();
            castingSkill.cancelCast();
            player.removeSkillCoolDown(skillId);
            player.setCasting(null);
            PacketSendUtility.sendPacket(player, new SM_SKILL_CANCEL(player, skillId));
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CANCELED());
        }
    }

    /**
     *
     */
    public void updatePassiveStats() {
        Player player = getOwner();
        for (SkillListEntry skillEntry : player.getSkillList().getAllSkills()) {
            Skill skill = SkillEngine.getInstance().getSkillFor(player, skillEntry.getSkillId(), player.getTarget());
            if (skill != null && skill.isPassive()) {
                skill.useSkill();
            }
        }
    }

    @Override
    public Player getOwner() {
        return (Player) super.getOwner();
    }

    @Override
    public void onRestore(HealType healType, int value) {
        super.onRestore(healType, value);
        switch (healType) {
            case DP:
                getOwner().getCommonData().addDp(value);
                break;
        }
    }


    /**
     * @param player
     * @return
     */
    public boolean isDueling(Player player) {
        return DuelService.getInstance().isDueling(player.getObjectId(), getOwner().getObjectId());
    }

    public void updateNearbyQuestList() {
        getOwner().addPacketBroadcastMask(BroadcastMode.UPDATE_NEARBY_QUEST_LIST);
    }

    public void updateNearbyQuestListImpl() {
        PacketSendUtility.sendPacket(getOwner(), new SM_NEARBY_QUESTS(getOwner().getNearbyQuests()));
    }

    public boolean isInShutdownProgress() {
        return isInShutdownProgress;
    }

    public void setInShutdownProgress(boolean isInShutdownProgress) {
        this.isInShutdownProgress = isInShutdownProgress;
    }

    /**
     * Handle dialog
     */
    @Override
    public void onDialogSelect(int dialogId, Player player, int questId) {
        switch (dialogId) {
            case 2:
                PacketSendUtility.sendPacket(player, new SM_PRIVATE_STORE(getOwner().getStore()));
                break;
        }
    }

    /**
     * @param level
     */
    public void upgradePlayer(int level) {
        Player player = getOwner();

        PlayerStatsTemplate statsTemplate = DataManager.PLAYER_STATS_DATA.getTemplate(player);
        player.setPlayerStatsTemplate(statsTemplate);

        // update stats after setting new template
        player.getGameStats().doLevelUpgrade();
        player.getLifeStats().synchronizeWithMaxStats();
        player.getLifeStats().updateCurrentStats();

        PacketSendUtility.broadcastPacket(player, new SM_LEVEL_UPDATE(
            player.getObjectId(), 0, level), true);
        PacketSendUtility.sendPacket(player, new SM_CUBE_UPDATE(
            player, 6, player.getCommonData().getAdvancedStigmaSlotSize()));
        PacketSendUtility.sendPacket(player, new SM_CUBE_UPDATE(
            player, 5, player.getCommonData().getStigmaSlotSize()));

        // Temporal
        ClassChangeService.showClassChangeDialog(player);

        QuestEngine.getInstance().onLvlUp(new QuestCookie(null, player, 0, 0));
        updateNearbyQuests();
        PacketSendUtility.sendPacket(player, new SM_QUEST_LIST(player));

        PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));

        if (level == 10 && player.getSkillList().getSkillEntry(30001) != null) {
            int skillLevel = player.getSkillList().getSkillLevel(30001);
            player.getSkillList().removeSkill(30001);
            PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player));
            player.getSkillList().addSkill(player, 30002, skillLevel, true);
        }
        // add new skills
        SkillLearnService.addNewSkills(player, false);
        player.getController().updatePassiveStats();

        /**
         * Broadcast Update to all that may care.
         */
        if (player.isInGroup())
            player.getPlayerGroup().updateGroupUIToEvent(player, GroupEvent.UPDATE);
        if (player.isInAlliance())
            AllianceService.getInstance().updateAllianceUIToEvent(player, PlayerAllianceEvent.UPDATE);
        if (player.isLegionMember())
            LegionService.getInstance().updateMemberInfo(player);

        if (CustomConfig.ENABLE_SURVEYS)
            HTMLService.checkSurveys(player);
    }

    /**
     * After entering game player char is "blinking" which means that it's in under some protection, after making an
     * action char stops blinking. - Starts protection active - Schedules task to end protection
     */
    public void startProtectionActiveTask() {
        getOwner().setVisualState(CreatureVisualState.BLINKING);
        PacketSendUtility.broadcastPacket(getOwner(), new SM_PLAYER_STATE(getOwner()), true);
        Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                stopProtectionActiveTask();
            }
        }, 60000);
        addTask(TaskId.PROTECTION_ACTIVE, task);
    }

    /**
     * Stops protection active task after first move or use skill
     */
    public void stopProtectionActiveTask() {
        cancelTask(TaskId.PROTECTION_ACTIVE);
        Player player = getOwner();
        if (player != null && player.isSpawned()) {
            player.unsetVisualState(CreatureVisualState.BLINKING);
            PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
        }
    }

    /**
     * When player arrives at destination point of flying teleport
     */
    public void onFlyTeleportEnd() {
        Player player = getOwner();
        player.unsetState(CreatureState.FLIGHT_TELEPORT);
        player.setFlightTeleportId(0);
        player.setFlightDistance(0);
        player.setState(CreatureState.ACTIVE);
        addZoneUpdateMask(ZoneUpdateMode.ZONE_REFRESH);
    }

    public void onEnterZone(ZoneInstance zoneInstance)
    {
        addZoneUpdateMask(ZoneUpdateMode.ZONE_REFRESH);
        QuestEngine.getInstance().onEnterZone(new QuestCookie(null, this.getOwner(), 0, 0), zoneInstance.getTemplate().getName());
        
        Player player = getOwner();
        ZoneInstance currentZone = player.getZoneInstance();
        if(currentZone != null && GSConfig.FREEFLY == true) {
            currentZone.isFlightAllowed();
        }
        if(currentZone != null && !currentZone.isFlightAllowed() && player.getAccessLevel() < AdminConfig.GM_FLIGHT_FREE) {
            checkNoFly(player);
        }
    }

    public void onLeaveZone(ZoneInstance zoneInstance)
    {
        
    }

    public void checkNoFly(final Player player)	
    {
        if(player.isInState(CreatureState.FLYING))
            player.getFlyController().endFly();	
    }


    /**
     * Zone update mask management
     *
     * @param mode
     */
    public final void addZoneUpdateMask(ZoneUpdateMode mode) {
        zoneUpdateMask |= mode.mask();
        ZoneService.getInstance().add(getOwner());
    }

    public final void removeZoneUpdateMask(ZoneUpdateMode mode) {
        zoneUpdateMask &= ~mode.mask();
    }

    public final byte getZoneUpdateMask() {
        return zoneUpdateMask;
    }

    /**
     * Update zone taking into account the current zone
     */
    public void updateZoneImpl() {
        ZoneService.getInstance().checkZone(getOwner());
    }

    /**
     * Refresh completely zone irrespective of the current zone
     */
    public void refreshZoneImpl() {
        ZoneService.getInstance().findZoneInCurrentMap(getOwner());
    }

    /**
     *
     */
    public void ban() {
        // sp.getTeleportService().teleportTo(this.getOwner(), 510010000, 256f, 256f, 49f, 0);
    }

    /**
     * Check water level (start drowning) and map death level (die)
     */
    public void checkWaterLevel() {
        Player player = getOwner();
        World world = World.getInstance();
        float z = player.getZ();

        if (player.getLifeStats().isAlreadyDead())
            return;

        if (z < world.getWorldMap(player.getWorldId()).getDeathLevel()) {
            die();
            return;
        }

        ZoneInstance currentZone = player.getZoneInstance();
        if (currentZone != null && currentZone.isBreath())
            return;

        //TODO need fix character height
        float playerheight = player.getPlayerAppearance().getHeight() * 1.6f;
        if (z < world.getWorldMap(player.getWorldId()).getWaterLevel() - playerheight)
            ZoneService.getInstance().startDrowning(player);
        else
            ZoneService.getInstance().stopDrowning(player);
    }

    @Override
    public void createSummon(int npcId, int skillLvl) {
        Player master = getOwner();
        Summon summon = SpawnEngine.getInstance().spawnSummon(master, npcId, skillLvl);
        master.setSummon(summon);
        PacketSendUtility.sendPacket(master, new SM_SUMMON_PANEL(summon));
        PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, EmotionType.START_EMOTE2));
        PacketSendUtility.broadcastPacket(summon, new SM_SUMMON_UPDATE(summon));
    }

    public boolean addItems(int itemId, int count) {
        return ItemService.addItems(getOwner(), Collections.singletonList(new QuestItems(itemId, count)));
    }

    public void setCanAutoRevive(boolean canAutoRevive)
    {
        this.canAutoRevive = canAutoRevive;
    }

}