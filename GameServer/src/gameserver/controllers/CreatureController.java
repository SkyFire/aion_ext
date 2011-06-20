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

import com.aionemu.commons.utils.Rnd;
import gameserver.utils.MathUtil;
import gameserver.configs.main.GeoDataConfig;
import gameserver.controllers.movement.MovementType;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.*;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import gameserver.network.aion.serverpackets.SM_MOVE;
import gameserver.network.aion.serverpackets.SM_SKILL_CANCEL;
import gameserver.skillengine.SkillEngine;
import gameserver.skillengine.model.AttackType;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.HealType;
import gameserver.skillengine.model.Skill;
import gameserver.utils.PacketSendUtility;
import gameserver.world.Executor;
import gameserver.world.GeoData;
import gameserver.world.World;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.concurrent.Future;

/**
 * This class is for controlling Creatures [npc's, players etc]
 *
 * @author -Nemesiss-, ATracer(2009-09-29), Sarynth
 */
public abstract class CreatureController<T extends Creature> extends VisibleObjectController<Creature> {
    private static final Logger log = Logger.getLogger(CreatureController.class);
    private FastMap<Integer, Future<?>> tasks = new FastMap<Integer, Future<?>>().shared();

    private float healRate = 1.00f;
	private int protectionValue = 0;
	private int protectionRange = 0;
	private Creature protector = null;
	private Effect protectEffect = null;
	private AttackType attacktype;
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void notSee(VisibleObject object, boolean isOutOfRange) {
        super.notSee(object, isOutOfRange);
        if (object == getOwner().getTarget()) {
            getOwner().setTarget(null);
            PacketSendUtility.broadcastPacket(getOwner(), new SM_LOOKATOBJECT(getOwner()));
        }
    }

    /**
     * Perform tasks on Creature starting to move
     */
    public void onStartMove() {
        getOwner().getObserveController().notifyMoveObservers();
    }

    /**
     * Perform tasks on Creature move in progress
     */
    public void onMove() {
        getOwner().getObserveController().notifyMoveObservers();
    }

    /**
     * Perform tasks on Creature stop move
     */
    public void onStopMove() {
        getOwner().getObserveController().notifyMoveObservers();
    }

    /**
     * Perform tasks on Creature death
     */
    public void onDie(Creature lastAttacker) {
        this.getOwner().setCasting(null);
        this.getOwner().getEffectController().removeAllEffects();
        this.getOwner().getMoveController().stop();
        this.getOwner().setState(CreatureState.DEAD);
    }

    /**
     * Perform tasks on Creature respawn
     */
    @Override
    public void onRespawn() {
        getOwner().unsetState(CreatureState.DEAD);
        getOwner().getAggroList().clear();
    }

    /**
     * Perform tasks when Creature was attacked //TODO may be pass only Skill object - but need to add properties in it
     */
    public void onAttack(Creature creature, int skillId, TYPE type, int damage, boolean notifyAttackedObservers) {
        int oldDamage = damage;
        if (damage > getOwner().getLifeStats().getCurrentHp())
            damage = getOwner().getLifeStats().getCurrentHp() + 1;

        Skill skill = getOwner().getCastingSkill();
        if (skill != null && skill.getSkillTemplate().getCancelRate() > 0) {
            int cancelRate = skill.getSkillTemplate().getCancelRate();
            int conc = getOwner().getGameStats().getCurrentStat(StatEnum.CONCENTRATION) / 10;
            float maxHp = getOwner().getGameStats().getCurrentStat(StatEnum.MAXHP);
            float cancel = (cancelRate - conc) + (((float) damage) / maxHp * 50);
            if (Rnd.get(100) < cancel && damage > 0)
            if (creature.getLifeStats().getCurrentHp() == 0)
                cancelCurrentSkill();
        }

        if (protector != null && protectEffect != null) {
            if (getOwner() instanceof Player) {
                if (protector.getLifeStats().isAlreadyDead()) {
                    removeProtectState(true);
                }
                else if (MathUtil.isInRange(protector,getOwner(),protectionRange)) {
                    ((Player) getOwner()).setProtect(true);
                    checkForProtectState(creature, skillId, type, oldDamage);
                    return;
                }
                else {
                    ((Player) getOwner()).setProtect(false);
                }
            }
        }

        if (notifyAttackedObservers) {
            //getOwner().getLifeStats().reduceHp(damage, creature);
            getOwner().getObserveController().notifyAttackedObservers(creature);			
            getOwner().getAggroList().addDamage(creature, damage);
        }
    }

    /**
     * Perform tasks when Creature was attacked
     */
    public void onAttack(Creature creature, int damage, boolean notifyAttackedObservers) {
        this.onAttack(creature, 0, TYPE.REGULAR, damage, notifyAttackedObservers);
    }

    /**
     * @param hopType
     * @param value
     */
    public void onRestore(HealType hopType, int value) {
        switch (hopType) {
            case HP:
                getOwner().getLifeStats().increaseHp(TYPE.HP, value);
                break;
            case MP:
                getOwner().getLifeStats().increaseMp(TYPE.MP, value);
                break;
            case FP:
                getOwner().getLifeStats().increaseFp(value);
                break;
        }
    }

    /**
     * Perform reward operation
     */
    public void doReward() {

    }

    /**
     * This method should be overriden in more specific controllers
     */
    public void onDialogRequest(Player player) {

    }

    /**
     * @param target
     */
    public void attackTarget(Creature target) {
        getOwner().getObserveController().notifyAttackObservers(target);
    }

    /**
     * Stops movements
     */
    public void stopMoving() {
        // purpose of log code is for debug only or verify z values after calc
        Creature owner = getOwner();
        /*
            * * try of GeoData fix
            */
        // float ownerX = owner.getX();
        // float ownerY = owner.getY();
        float fixZ = owner.getZ();
        if (GeoDataConfig.GEO_ENABLE) {
            switch (owner.getWorldId()) {
                // Terrestrial maps
                case 110010000:
                case 120010000:
                case 210010000:
                case 210030000:
                case 210020000:
                case 210060000:
                case 210040000:
                case 220010000:
                case 220030000:
                case 220020000:
                case 220050000:
                case 220040000:
                case 300010000:
                case 300020000:
                case 300030000:
                case 300040000:
                case 300050000:
                case 300060000:
                case 300070000:
                case 300080000:
                case 300090000:
                case 300100000:
                case 300110000:
                case 300120000:
                case 300130000:
                case 300140000:
                case 310010000:
                case 310020000:
                case 310030000:
                case 310040000:
                case 310050000:
                case 310060000:
                case 310070000:
                case 310080000:
                case 310090000:
                case 310100000:
                case 310110000:
                case 310120000:
                case 320010000:
                case 320020000:
                case 320030000:
                case 320040000:
                case 320050000:
                case 320060000:
                case 320070000:
                case 320080000:
                case 320090000:
                case 320100000:
                case 320110000:
                case 320120000:
                case 320130000:
                case 320140000:
                case 400010000:
                case 510010000:
                case 520010000:
                case 900020000:
                case 900030000:
                case 900100000:
                    fixZ = GeoData.getZ(owner.getWorldId(), owner.getX(), owner.getY(), owner.getZ());
                    if (owner instanceof Npc) {
                        Creature target = (Creature) owner.getTarget();
                        if (target != null && (target instanceof Player || target instanceof Summon)) {
                            // NPC/Monster is attacking when Target is player or summon
                            if (owner.getEffectController().getAbnormals() == 0) {
                                // NPC/Monster is at player position or in range of player
                                fixZ = owner.getZ();
                                log.info("stopMoving: " + owner.getName() + "" + owner.getTarget().getName()
                                        + " : GeoData OFF");
                            } else {
                                // NPC/Monster is under effect, apply GeoData
                                // log.info("stopMoving : "owner.getName()" target="owner.getTarget().getName()" : GeoData on (effect="String.valueOf(owner.getEffectController().getAbnormals())" ownerZ="String.valueOf(owner.getZ())" geoDataZ="String.valueOf(fixZ));
                                // }
                                //NPC/Monster is under effect, apply GeoData
                                log.debug("stopMoving: " + owner.getName() + " target=" + owner.getTarget().getName()
                                        + " : GeoData ON (effect="
                                        + String.valueOf(owner.getEffectController().getAbnormals()) + " ownerZ="
                                        + String.valueOf(owner.getZ()) + " geoDataZ=" + String.valueOf(fixZ));
                            }
                            //} else {
                            //   log.info("stopMoving : "owner.getName()" has no target or target isn't a player : GeoData on (ownerZ="String.valueOf(owner.getZ())" geoDataZ="String.valueOf(fixZ));
                        }
                    }
                    break;
                default:
                    // Other maps
                    break;
            }
        }
        World.getInstance().updatePosition(owner, owner.getX(), owner.getY(), fixZ, owner.getHeading());
        PacketSendUtility.broadcastPacket(owner, new SM_MOVE(owner.getObjectId(), owner.getX(), owner.getY(), owner
                .getZ(), owner.getHeading(), MovementType.MOVEMENT_STOP));
    }

    /**
     * Check if Creature is under Protect state
     */
    private void checkForProtectState(Creature creature, int skillId, TYPE type, int damage) {
        int damageTaken = damage;
        int damageReflected = damage * ((int)(100/protectionValue));

        switch(attacktype) {
            case EVERYHIT:
                protector.getLifeStats().reduceHp(damageReflected,creature);
                damageTaken = (damage - damageReflected);
                break;
            case PHYSICAL_SKILL:
                // TODO
                break;
            case MAGICAL_SKILL:
                // TODO
                break;
            case ALL_SKILL:
                if (skillId != 0) {
                    protector.getLifeStats().reduceHp(damageReflected,creature);
                    damageTaken = (damage - damageReflected);
                }
                break;
        }
        getOwner().getLifeStats().reduceHp(damageTaken, creature);
    }
	
    /**
     * Set the Protect state of this Creature
     */
    public void setProtectState(Creature creatureProtector, final Effect protectEffect, final int value, final int range, final AttackType type) {
        this.protector = creatureProtector;
        this.protectEffect = protectEffect;
        this.protectionValue = value;
        this.protectionRange = range;
        this.attacktype = type;
    }

    /**
     * Remove the Protect state from Creature
     */
    public void removeProtectState(boolean endEffect) {
        //end protect effect if a protector dies
        if (endEffect) {
            protectEffect.endEffect();
            return;
        }
        setProtectState(null,null,0,0,null);
    }	
  	
    /**
     * Handle Dialog_Select
     *
     * @param dialogId
     * @param player
     * @param questId
     */
    public void onDialogSelect(int dialogId, Player player, int questId) {
        // TODO Auto-generated method stub
    }

    /**
     * @param taskId
     * @return
     */
    public Future<?> getTask(TaskId taskId) {
        return tasks.get(taskId.ordinal());
    }

    /**
     * @param taskId
     * @return
     */
    public boolean hasTask(TaskId taskId) {
        return tasks.containsKey(taskId.ordinal());
    }

    /**
     * @param taskId
     */
    public void cancelTask(TaskId taskId) {
        Future<?> task = tasks.remove(taskId.ordinal());
        if (task != null) {
            task.cancel(false);
        }
    }

    /**
     * If task already exist - it will be canceled
     *
     * @param taskId
     * @param task
     */
    public void addTask(TaskId taskId, Future<?> task) {
        cancelTask(taskId);
        tasks.put(taskId.ordinal(), task);
    }

    /**
     * If task already exist - it will not be replaced
     *
     * @param taskId
     * @param task
     */
    public void addNewTask(TaskId taskId, Future<?> task) {
        tasks.putIfAbsent(taskId.ordinal(), task);
    }

    /**
     * Cancel all tasks associated with this controller
     * (when deleting object)
     */
    public void cancelAllTasks() {
        for (Future<?> task : tasks.values()) {
            if (task != null) {
                task.cancel(true);
            }
        }
        // FIXME: This can fill error logs with NPE if left null. Should never happen...
        tasks = new FastMap<Integer, Future<?>>().shared();
    }

    @Override
    public void delete() {
        cancelAllTasks();
        super.delete();
    }

    /**
     * Die by reducing HP to 0
     */
    public void die() {
        getOwner().getLifeStats().reduceHp(getOwner().getLifeStats().getCurrentHp() + 1, null);
    }

    /**
     * @param skillId
     */
    public void useSkill(int skillId) {
        Creature creature = getOwner();

        Skill skill = SkillEngine.getInstance().getSkill(creature, skillId, 1, creature.getTarget());
        Logger.getLogger(getClass()).debug(creature.getName() + "using skill #" + skillId + ":" + skill);
        if (skill != null) {
            skill.useSkill();
        }
    }

    /**
     * Notify hate value to all visible creatures
     *
     * @param value
     */
    public void broadcastHate(final int value) {
        getOwner().getKnownList().doOnAllObjects(new Executor<AionObject>() {
            @Override
            public boolean run(AionObject visibleObject) {
                if (visibleObject instanceof Creature) {
                    ((Creature) visibleObject).getAggroList().notifyHate(getOwner(), value);
                }
                return true;
            }
        });
    }

    public void abortCast() {
        Creature creature = getOwner();
        Skill skill = creature.getCastingSkill();
        if (skill == null)
            return;
        creature.setCasting(null);
    }

    /**
     * Cancel current skill and remove cooldown
     */
    public void cancelCurrentSkill() {
        Creature creature = getOwner();
        Skill castingSkill = creature.getCastingSkill();
        if (castingSkill != null) {
            castingSkill.cancelCast();
            creature.removeSkillCoolDown(castingSkill.getSkillTemplate().getSkillId());
            creature.setCasting(null);
            PacketSendUtility.broadcastPacketAndReceive(creature, new SM_SKILL_CANCEL(creature, castingSkill.getSkillTemplate().getSkillId()));
        }
    }

    /**
     * @param npcId
     */
    public void createSummon(int npcId, int skillLvl) {
        // TODO Auto-generated method stub

    }

    public float getHealRate() {
        return healRate;
    }

    public void setHealRate(float healRate) {
        this.healRate = healRate;
    }
}
