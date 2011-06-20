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

import gameserver.controllers.attack.AttackResult;
import gameserver.controllers.attack.AttackUtil;
import gameserver.model.EmotionType;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.Summon.SummonMode;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.*;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.restrictions.RestrictionsManager;
import gameserver.services.LifeStatsRestoreService;
import gameserver.skillengine.SkillEngine;
import gameserver.skillengine.model.Skill;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.List;
import java.util.ArrayList;

/**
 * @author ATracer
 * @author RotO (Attack-speed hack protection)
 */
public class SummonController extends CreatureController<Summon> {
    private long lastAttackMilis = 0;

    @Override
    public void notSee(VisibleObject object, boolean isOutOfRange) {
        super.notSee(object, isOutOfRange);
        if (getOwner().getMaster() == null)
            return;

        if (object.getObjectId() == getOwner().getMaster().getObjectId()) {
            release(UnsummonType.DISTANCE);
        }
    }

    @Override
    public Summon getOwner() {
        return (Summon) super.getOwner();
    }

    /**
     * Release summon
     */
    public void release(final UnsummonType unsummonType) {
        final Summon owner = getOwner();

        if (owner.getMode() == SummonMode.RELEASE)
            return;
        owner.setMode(SummonMode.RELEASE);

        final Player master = owner.getMaster();
        final int summonObjId = owner.getObjectId();
        removeSpiritSubstitution((Creature) master);

        switch (unsummonType) {
            case COMMAND:
                PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_UNSUMMON(getOwner().getNameId()));
                PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(getOwner()));
                break;
            case DISTANCE:
                PacketSendUtility.sendPacket(getOwner().getMaster(), SM_SYSTEM_MESSAGE
                        .SUMMON_UNSUMMON_BY_TOO_DISTANCE());
                PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(getOwner()));
                break;
            case LOGOUT:
            case UNSPECIFIED:
                break;
        }

        ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                owner.setMaster(null);
                master.setSummon(null);
                owner.getController().delete();

                switch (unsummonType) {
                    case COMMAND:
                    case DISTANCE:
                    case UNSPECIFIED:
                        PacketSendUtility
                                .sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_DISMISSED(getOwner().getNameId()));
                        PacketSendUtility.sendPacket(master, new SM_SUMMON_OWNER_REMOVE(summonObjId));

                        // TODO temp till found on retail
                        PacketSendUtility.sendPacket(master, new SM_SUMMON_PANEL_REMOVE());
                        break;
                    case LOGOUT:
                        break;
                }
            }
        }, 5000);
    }

    /**
     * Change to rest mode
     */
    public void restMode() {
        getOwner().getController().cancelTask(TaskId.RESTORE);
        getOwner().setMode(SummonMode.REST);
        Player master = getOwner().getMaster();
        PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_RESTMODE(getOwner().getNameId()));
        PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(getOwner()));
        checkCurrentHp();
    }

    private void checkCurrentHp() {
        if (!getOwner().getLifeStats().isFullyRestoredHp())
            getOwner().getController().addNewTask(TaskId.RESTORE,
                    LifeStatsRestoreService.getInstance().scheduleHpRestoreTask(getOwner().getLifeStats()));
    }

    /**
     * Change to guard mode
     */
    public void guardMode() {
        getOwner().setMode(SummonMode.GUARD);
        Player master = getOwner().getMaster();
        PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_GUARDMODE(getOwner().getNameId()));
        PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(getOwner()));
        getOwner().getController().cancelTask(TaskId.RESTORE);
    }

    /**
     * Change to attackMode
     */
    public void attackMode() {
        getOwner().setMode(SummonMode.ATTACK);
        Player master = getOwner().getMaster();
        PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_ATTACKMODE(getOwner().getNameId()));
        PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(getOwner()));
        getOwner().getController().cancelTask(TaskId.RESTORE);
    }

    @Override
    public void attackTarget(Creature target) {
        Summon summon = getOwner();
        Player master = getOwner().getMaster();
        if (!summon.canAttack())
            return;

        if (!RestrictionsManager.canAttack(master, target))
            return;

        if (!summon.isEnemy(target))
            return;

        int attackSpeed = summon.getGameStats().getCurrentStat(StatEnum.ATTACK_SPEED);
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

        List<AttackResult> attackList = AttackUtil.calculateAttackResult(summon, target);

        int damage = 0;
        for (AttackResult result : attackList) {
            damage += result.getDamage();
        }

        int attackType = 0;
        PacketSendUtility.broadcastPacket(summon, new SM_ATTACK(summon, target, summon.getGameStats()
                .getAttackCounter(), 274, attackType, attackList));

        target.getController().onAttack(summon, damage, true);
        summon.getGameStats().increaseAttackCounter();

    }

    @Override
    public void onAttack(Creature creature, int skillId, TYPE type, int damage, boolean notifyAttackedObservers) {
        if (getOwner().getLifeStats().isAlreadyDead())
            return;

        //temp
        if (getOwner().getMode() == SummonMode.RELEASE)
            return;

        super.onAttack(creature, skillId, type, damage, notifyAttackedObservers);
        getOwner().getLifeStats().reduceHp(damage, creature);
        PacketSendUtility.broadcastPacket(getOwner(), new SM_ATTACK_STATUS(getOwner(), TYPE.REGULAR, 0,
                damage));
        PacketSendUtility.sendPacket(getOwner().getMaster(), new SM_SUMMON_UPDATE(getOwner()));
    }

    @Override
    public void onDie(Creature lastAttacker) {
        super.onDie(lastAttacker);
        release(UnsummonType.UNSPECIFIED);
        Summon owner = getOwner();
        removeSpiritSubstitution((Creature) owner.getMaster());
        PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker
                .getObjectId()));
    }

    public void useSkill(int skillId, Creature target) {
        Creature creature = getOwner();

        Skill skill = SkillEngine.getInstance().getSkill(creature, skillId, 1, target);
        if (skill != null) {
            skill.useSkill();
        }
    }

    public void removeSpiritSubstitution(Creature master) {
        List<Integer> skillIds = new ArrayList<Integer>();
        for (int skillid = 18262; skillid < 18279; ++skillid) {
            skillIds.add(skillid);
        }
        master.getEffectController().removeEffects(skillIds);    
    }

    public static enum UnsummonType {
        LOGOUT,
        DISTANCE,
        COMMAND,
        UNSPECIFIED
    }
}
