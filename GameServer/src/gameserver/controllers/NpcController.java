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
import com.aionemu.commons.database.dao.DAOManager;
import gameserver.ai.AI;
import gameserver.ai.events.Event;
import gameserver.ai.npcai.DummyAi;
import gameserver.configs.main.LegionConfig;
import gameserver.controllers.attack.AttackResult;
import gameserver.controllers.attack.AttackUtil;
import gameserver.dao.SpawnDAO;
import gameserver.dataholders.DataManager;
import gameserver.model.ChatType;
import gameserver.model.EmotionType;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.QuestStateList;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.NpcGameStats;
import gameserver.model.templates.quest.NpcQuestData;
import gameserver.model.templates.teleport.TelelocationTemplate;
import gameserver.model.templates.teleport.TeleportLocation;
import gameserver.model.templates.teleport.TeleporterTemplate;
import gameserver.network.aion.serverpackets.*;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.questEngine.QuestEngine;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.restrictions.RestrictionsManager;
import gameserver.services.*;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.world.Executor;
import gameserver.world.World;
import org.apache.log4j.Logger;
import gameserver.ai.desires.impl.*;

import java.util.List;
import java.util.concurrent.Future;

/**
 * This class is for controlling Npc's
 *
 * @author -Nemesiss-, ATracer (2009-09-29), Sarynth
 */
public class NpcController extends CreatureController<Npc> {
    private static final Logger log = Logger.getLogger(NpcController.class);

    @Override
    public void notSee(VisibleObject object, boolean isOutOfRange) {
        super.notSee(object, isOutOfRange);
        if(object instanceof Player || object instanceof Summon)
            getOwner().getAi().handleEvent(Event.NOT_SEE_PLAYER);	
    }

    @Override
    public void see(VisibleObject object) {
        super.see(object);
        Npc owner = getOwner();
        owner.getAi().handleEvent(Event.SEE_CREATURE);
        if (object instanceof Player) {
            owner.getAi().handleEvent(Event.SEE_PLAYER);
            //TODO check on retail how walking npc is presented, probably need replace emotion
            // with some state etc.
            if (owner.getMoveController().isWalking())
                PacketSendUtility.sendPacket((Player) object, new SM_EMOTION(owner, EmotionType.WALK));
            else if (owner.getMoveController().isWalking() && owner.canSee((Player) object))
                owner.getAi().clearDesires();
                owner.getAi().addDesire(new AggressionDesire(owner, 100));
        } else if (object instanceof Summon) {
            owner.getAi().handleEvent(Event.SEE_PLAYER);
        }
    }

    @Override
    public void onRespawn() {
        super.onRespawn();

        cancelTask(TaskId.DECAY);

        Npc owner = getOwner();

        if (owner != null && owner.isCustom()) {
            DAOManager.getDAO(SpawnDAO.class).setSpawned(owner.getSpawn().getSpawnId(), owner.getObjectId(), true);
        }

        //set state from npc templates
        if (owner.getObjectTemplate().getState() != 0)
            owner.setState(owner.getObjectTemplate().getState());
        else
            owner.setState(CreatureState.NPC_IDLE);

        owner.getLifeStats().setCurrentHpPercent(100);
        owner.getLifeStats().setCurrentMpPercent(100);
        owner.getAi().handleEvent(Event.RESPAWNED);

        if (owner.getSpawn().getNpcFlyState() != 0) {
            owner.setState(CreatureState.FLYING);
        }
    }

    public void onDespawn(boolean forced) {
        if (forced)
            cancelTask(TaskId.DECAY);

        Npc owner = getOwner();

        if (owner == null || !owner.isSpawned())
            return;

        if (owner != null && owner.isCustom()) {
            DAOManager.getDAO(SpawnDAO.class).setSpawned(owner.getSpawn().getSpawnId(), owner.getSpawn().isNoRespawn(1) ? -1 : owner.getObjectId(), false);
        }

        owner.getAi().handleEvent(Event.DESPAWN);
        World.getInstance().despawn(owner);
    }

    @Override
    public void onDie(Creature lastAttacker) {
        super.onDie(lastAttacker);

        Npc owner = getOwner();

        addTask(TaskId.DECAY, RespawnService.scheduleDecayTask(this.getOwner()));
        scheduleRespawn();

        PacketSendUtility.broadcastPacket(owner,
                new SM_EMOTION(owner, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()));

        // Monster Controller overrides this method.
        this.doReward();

        owner.getAi().handleEvent(Event.DIED);

        // deselect target at the end
        owner.setTarget(null);
        PacketSendUtility.broadcastPacket(owner, new SM_LOOKATOBJECT(owner));
    }

    @Override
    public Npc getOwner() {
        return (Npc) super.getOwner();
    }

    @Override
    public void onDialogRequest(Player player) {
        getOwner().getAi().handleEvent(Event.TALK);

        if (QuestEngine.getInstance().onDialog(new QuestCookie(getOwner(), player, 0, -1)))
            return;

        // Zephyr Deliveryman
        if (getOwner().getObjectId() == player.getZephyrObjectId()) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 18));
            return;
        }

        int titleId = getOwner().getObjectTemplate().getTitleId();
        if
                (
                (
                        // title ids of npcs
                        titleId == 315018
                                || titleId == 350474
                                || titleId == 350473
                                || titleId == 350212
                                || titleId == 350304
                                || titleId == 350305
                                || titleId == 370000
                                || titleId == 370003
                                // aerolinks
                                || (getOwner().getNpcId() >= 730265 && getOwner().getNpcId() <= 730269)
                )
                ) {
            NpcQuestData npcQD = QuestEngine.getInstance().getNpcQuestData(getOwner().getNpcId());
            QuestStateList list = player.getQuestStateList();
            List<Integer> events = npcQD.getOnTalkEvent();
            boolean hasQuestFromNpc = false;
            for (int e : events) {
                QuestState qs = list.getQuestState(e);
                if (qs != null && qs.getStatus() != QuestStatus.COMPLETE) {
                    hasQuestFromNpc = true;
                    break;
                } else {
                    continue;
                }
            }
            if (hasQuestFromNpc)
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 10));
            else
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));

        } else
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 10));
    }

    /**
     * This method should be called to make forced despawn of NPC and delete it from the world
     */
    public void onDelete() {
        if (getOwner().isInWorld()) {
            this.getOwner().getAi().clearDesires();
            this.onDespawn(true);
            this.delete();
        }
    }

    /**
     * Handle dialog
     */
    @Override
    public void onDialogSelect(int dialogId, final Player player, int questId) {

        Npc npc = getOwner();
        int targetObjectId = npc.getObjectId();

        if (QuestEngine.getInstance().onDialog(new QuestCookie(npc, player, questId, dialogId)))
            return;

        switch (dialogId) {
            case 2:
                PacketSendUtility.sendPacket(player, new SM_TRADELIST(npc,
                    TradeService.getTradeListData().getTradeListTemplate(npc.getNpcId()),
                    player.getPrices().getVendorBuyModifier(), player));
                break;
            case 3:
                PacketSendUtility.sendPacket(player, new SM_SELL_ITEM(targetObjectId, player.getPrices().getVendorSellModifier(player.getCommonData().getRace())));
                break;
            case 4:
                // stigma
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1));
                break;
            case 5:
                // create legion
                if (MathUtil.isInRange(npc, player, 10)) // avoiding exploit with sending fake dialog_select packet
                {
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 2));
                } else {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.LEGION_CREATE_TOO_FAR_FROM_NPC());
                }
                break;
            case 6:
                // disband legion
                if (MathUtil.isInRange(npc, player, 10)) // avoiding exploit with sending fake dialog_select packet
                {
                    LegionService.getInstance().requestDisbandLegion(npc, player);
                } else {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.LEGION_DISPERSE_TOO_FAR_FROM_NPC());
                }
                break;
            case 7:
                // recreate legion
                if (MathUtil.isInRange(npc, player, 10)) // voiding exploit with sending fake client dialog_select
                // packet
                {
                    LegionService.getInstance().recreateLegion(npc, player);
                } else {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.LEGION_DISPERSE_TOO_FAR_FROM_NPC());
                }
                break;
            case 20:
                // warehouse
                if (MathUtil.isInRange(npc, player, 10)) // voiding exploit with sending fake client dialog_select
                // packet
                {
                    if (!RestrictionsManager.canUseWarehouse(player))
                        return;

                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 26));
                    WarehouseService.sendWarehouseInfo(player, true);
                }
                break;
            case 25:
                // TODO hotfix to prevent opening the legion wh when a quest returns false.
                break;
            case 27:
                // Consign trade?? npc karinerk, koorunerk
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 13));
                break;
            case 29:
                // soul healing
                final long expLost = player.getCommonData().getExpRecoverable();
                if (expLost == 0)
                    player.getEffectController().removeEffect(8291);
                final double factor = (expLost < 1000000 ?
                        0.25 - (0.00000015 * expLost)
                        : 0.1);
                final int price = (int) (expLost * factor * CustomConfig.SOULHEALING_PRICE_MULTIPLIER);

                RequestResponseHandler responseHandler = new RequestResponseHandler(npc) {
                    @Override
                    public void acceptRequest(Creature requester, Player responder) {
                        if (player.getInventory().getKinahItem().getItemCount() >= price) {
                            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.EXP(String.valueOf(expLost)));
                            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.SOUL_HEALED());
                            player.getCommonData().resetRecoverableExp();
                            player.getInventory().decreaseKinah(price);
                            player.getEffectController().removeEffect(8291);
                        } else {
                            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.NOT_ENOUGH_KINAH(price));
                        }
                    }

                    @Override
                    public void denyRequest(Creature requester, Player responder) {
                        // no message
                    }
                };
                if (player.getCommonData().getExpRecoverable() > 0) {
                    boolean result = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_SOUL_HEALING,
                            responseHandler);
                    if (result) {
                        PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(
                                SM_QUESTION_WINDOW.STR_SOUL_HEALING, 0, String.valueOf(price)
                        ));
                    }
                } else {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.DONT_HAVE_RECOVERED_EXP());
                }
                break;
            case 30:
                switch (npc.getNpcId()) {
                    // Triniel Arena (Enter)
                    case 204089:
                        TeleportService.teleportTo(player, 120010000, 1, 984f, 1543f, 222.1f, 0);
                        ArenaService.getInstance().registerPlayerForTriniel(player);
                        break;
                    // Coliseum (Enter)
                    case 203764:
                        TeleportService.teleportTo(player, 110010000, 1, 1462.5f, 1326.1f, 564.1f, 0);
                        ArenaService.getInstance().registerPlayerForColiseum(player);
                        break;
                    // Eracus Temple (Enter)
                    case 203981:
                        TeleportService.teleportTo(player, 210020000, 1, 439.3f, 422.2f, 274.3f, 0);
                        break;
                }
                break;
            case 31:
                switch (npc.getNpcId()) {
                    // Triniel Arena (Leave)
                    case 204087:
                        TeleportService.teleportTo(player, 120010000, 1, 1005.1f, 1528.9f, 222.1f, 0);
                        ArenaService.getInstance().unregister(player);
                        break;
                    // Coliseum (Leave)
                    case 203875:
                        TeleportService.teleportTo(player, 110010000, 1, 1470.3f, 1343.5f, 563.7f, 21);
                        ArenaService.getInstance().unregister(player);
                        break;
                    // Eracus Temple (Leave)
                    case 203982:
                        TeleportService.teleportTo(player, 210020000, 1, 446.2f, 431.1f, 274.5f, 0);
                        break;
                }
                break;
            case 35:
                // Godstone socketing
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 21));
                break;
            case 36:
                // remove mana stone
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 20));
                break;
            case 37:
                // modify appearance
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 19));
                break;
            case 38:
                // flight and teleport
                TeleportService.showMap(player, targetObjectId, npc.getNpcId());
                break;
            case 39:
                // improve extraction
            case 40:
                // learn tailoring armor smithing etc...
                CraftSkillUpdateService.getInstance().learnSkill(player, npc);
                break;
            case 41:
                // expand cube
                CubeExpandService.expandCube(player, npc);
                break;
            case 42:
                WarehouseService.expandWarehouse(player, npc);
                break;
            case 47:
                // legion warehouse
                if (LegionConfig.LEGION_WAREHOUSE)
                    if (MathUtil.isInRange(npc, player, 10))
                        LegionService.getInstance().openLegionWarehouse(player);
                break;
            case 50:
                // WTF??? Quest dialog packet
                break;
            case 52:
                if (MathUtil.isInRange(npc, player, 10)) // avoiding exploit with sending fake dialog_select packet
                {
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 28));
                }
                break;
            case 53:
                // coin reward
                PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "This feature is not available yet",
                        ChatType.ANNOUNCEMENTS));
                break;
            case 55:
            case 56:
                byte changesex = 0; //0 plastic surgery, 1 gender switch
                byte check_ticket = 2; // 2 no ticket, 1 have ticket
                if (dialogId == 56) {
                    //Gender Switch
                    changesex = 1;
                    if (player.getInventory().getItemCountByItemId(169660000) > 0 || player.getInventory().getItemCountByItemId(169660001) > 0)
                        check_ticket = 1;
                } else {
                    //Plastic Surgery
                    if (player.getInventory().getItemCountByItemId(169650000) > 0 || player.getInventory().getItemCountByItemId(169650001) > 0)
                        check_ticket = 1;
                }
                PacketSendUtility.sendPacket(player, new SM_PLASTIC_SURGERY(player, check_ticket, changesex));
                player.setEditMode(true);
                break;
            case 60:
                // armsfusion
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 29));
                break;
            case 61:
                // armsbreaking
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 30));
                break;
            case 64:
                // repurchase
                PacketSendUtility.sendPacket(player, new SM_REPURCHASE(npc, player));
                break;
            case 65:
                // adopt pet
                PacketSendUtility.sendPacket(player, new SM_PET(6));
                break;
            case 66:
                // surrender pet
                PacketSendUtility.sendPacket(player, new SM_PET(7));
                break;
            case 10000:
                // generic npc reply (most are teleporters)
                TeleporterTemplate template = DataManager.TELEPORTER_DATA.getTeleporterTemplate(npc.getNpcId());
                if (template != null) {
                    TeleportLocation loc = template.getTeleLocIdData().getTelelocations().get(0);
                    if (loc != null) {
                        player.getInventory().decreaseKinah(loc.getPrice());
                        TelelocationTemplate tlt = DataManager.TELELOCATION_DATA.getTelelocationTemplate(loc.getLocId());
                        TeleportService.teleportTo(player, tlt.getMapId(), tlt.getX(), tlt.getY(), tlt.getZ(), 1000);
                    }
                }
                break;
            default:
                if (questId > 0)
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, dialogId, questId));
                else
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, dialogId));
                break;
        }
    }

    @Override
    public void onAttack(final Creature creature, int skillId, TYPE type, int damage, boolean notifyAttackedObservers) {
        if (getOwner().getLifeStats().isAlreadyDead())
            return;

        super.onAttack(creature, skillId, type, damage, notifyAttackedObservers);

        Npc npc = getOwner();

        Creature actingCreature = creature.getActingCreature();
        if (actingCreature instanceof Player)
            if (QuestEngine.getInstance().onAttack(new QuestCookie(npc, (Player) actingCreature, 0, 0)))
                return;

        AI<?> ai = npc.getAi();
        if (ai instanceof DummyAi) {
            log.warn("CHECKPOINT: npc attacked without ai " + npc.getObjectTemplate().getTemplateId());
            return;
        }
        if(getOwner().getTribe().equals("DUMMY"))
        	damage = 0;

        getOwner().getKnownList().doOnAllNpcs(new Executor<Npc>() {
            @Override
            public boolean run(Npc tmp) {
                if (getOwner().isSupportFrom(tmp) && MathUtil.isInRange(getOwner(), tmp, 10)) {
                    tmp.getAggroList().addHate(creature, 10);
                }
                return true;
            }
        }, true);

        npc.getLifeStats().reduceHp(damage, actingCreature);

        PacketSendUtility.broadcastPacket(npc, new SM_ATTACK_STATUS(npc, type, skillId, damage));
    }

    @Override
    public void attackTarget(Creature target) {
        Npc npc = getOwner();

        /**
         * Check all prerequisites
         */
        if (npc == null || npc.getLifeStats().isAlreadyDead() || !npc.isSpawned())
            return;

        if (!npc.canAttack())
            return;

        AI<?> ai = npc.getAi();
        NpcGameStats gameStats = npc.getGameStats();

        if (target == null || target.getLifeStats().isAlreadyDead()) {
            ai.handleEvent(Event.MOST_HATED_CHANGED);
            return;
        }

        /**
         * notify attack observers
         */
        super.attackTarget(target);

        /**
         * Calculate and apply damage
         */
        List<AttackResult> attackList = AttackUtil.calculateAttackResult(npc, target);

        int damage = 0;
        for (AttackResult result : attackList) {
            damage += result.getDamage();
        }

        int attackType = 0; // TODO investigate attack types (0 or 1)
        PacketSendUtility.broadcastPacket(npc, new SM_ATTACK(npc, target, gameStats
                .getAttackCounter(), 274, attackType, attackList));

        target.getController().onAttack(npc, damage, true);
        gameStats.increaseAttackCounter();
    }

    @Override
    public void onStartMove() {
        super.onStartMove();
    }

    @Override
    public void onMove() {
        super.onMove();
    }

    @Override
    public void onStopMove() {
        super.onStopMove();
    }

    /**
     * Schedule respawn of npc
     * In instances - no npc respawn
     */
    public void scheduleRespawn() {
        if (getOwner().isInInstance())
            return;

        int instanceId = getOwner().getInstanceId();
        if (!getOwner().getSpawn().isNoRespawn(instanceId)) {
            Future<?> respawnTask = RespawnService.scheduleRespawnTask(getOwner());
            addTask(TaskId.RESPAWN, respawnTask);
        }
    }
}