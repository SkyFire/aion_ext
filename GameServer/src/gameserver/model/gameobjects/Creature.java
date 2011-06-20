/**
 * This file is part of Aion X Emu <aionxemu.com>
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.model.gameobjects;

import gameserver.ai.AI;
import gameserver.controllers.CreatureController;
import gameserver.controllers.MoveController;
import gameserver.controllers.ObserveController;
import gameserver.controllers.attack.AggroList;
import gameserver.controllers.effect.EffectController;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureSeeState;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.state.CreatureVisualState;
import gameserver.model.gameobjects.stats.CreatureGameStats;
import gameserver.model.gameobjects.stats.CreatureLifeStats;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.skillengine.effect.EffectId;
import gameserver.skillengine.model.Skill;
import gameserver.taskmanager.tasks.PacketBroadcaster;
import gameserver.taskmanager.tasks.PacketBroadcaster.BroadcastMode;
import gameserver.world.WorldPosition;
import javolution.util.FastMap;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * This class is representing movable objects, its base class for all in game objects that may move
 *
 * @author -Nemesiss-
 */
public abstract class Creature extends VisibleObject {
    /**
     * Reference to AI
     */
    protected AI<? extends Creature> ai;

    private CreatureLifeStats<? extends Creature> lifeStats;
    private CreatureGameStats<? extends Creature> gameStats;

    private EffectController effectController;
    private MoveController moveController;

    private int state = CreatureState.ACTIVE.getId();
    private int visualState = CreatureVisualState.VISIBLE.getId();
    private int seeState = CreatureSeeState.NORMAL.getId();

    private Skill castingSkill;
    private Map<Integer, Long> skillCoolDowns;
    private int transformedModelId;
    private ObserveController observeController;

    private AggroList aggroList;
	
    private long lastAttack = 0;
	
	private int			isAdminNeutral = 0;
	private int			isAdminEnmity = 0;

    /**
     * @param objId
     * @param controller
     * @param spawnTemplate
     * @param objectTemplate
     * @param position
     */
    public Creature(int objId, CreatureController<? extends Creature> controller,
                    SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate, WorldPosition position) {
        super(objId, controller, spawnTemplate, objectTemplate, position);
        initializeAi();
        this.moveController = new MoveController(this);
        this.observeController = new ObserveController();

        this.aggroList = new AggroList(this);
    }

    /**
     * Return CreatureController of this Creature object.
     *
     * @return CreatureController.
     */
    @SuppressWarnings("unchecked")
    @Override
    public CreatureController getController() {
        return (CreatureController) super.getController();
    }

    /**
     * @return the lifeStats
     */
    public CreatureLifeStats<? extends Creature> getLifeStats() {
        return lifeStats;
    }

    /**
     * @param lifeStats the lifeStats to set
     */
    public void setLifeStats(CreatureLifeStats<? extends Creature> lifeStats) {
        this.lifeStats = lifeStats;
    }

    /**
     * @return the gameStats
     */
    public CreatureGameStats<? extends Creature> getGameStats() {
        return gameStats;
    }

    /**
     * @param gameStats the gameStats to set
     */
    public void setGameStats(CreatureGameStats<? extends Creature> gameStats) {
        this.gameStats = gameStats;
    }

    public abstract byte getLevel();

    public abstract void initializeAi();

    /**
     * @return the effectController
     */
    public EffectController getEffectController() {
        return effectController;
    }

    /**
     * @param effectController the effectController to set
     */
    public void setEffectController(EffectController effectController) {
        this.effectController = effectController;
    }

    /**
     * @return the npcAi
     */
    public AI<? extends Creature> getAi() {
        return ai != null ? ai : AI.dummyAi();
    }

    /**
     * @param ai the ai to set
     */
    public void setAi(AI<? extends Creature> ai) {
        this.ai = ai;
    }

    /**
     * Is creature casting some skill
     *
     * @return
     */
    public boolean isCasting() {
        return castingSkill != null;
    }

    /**
     * Set current casting skill or null when skill ends
     *
     * @param castingSkill
     */
    public void setCasting(Skill castingSkill) {
        this.castingSkill = castingSkill;
    }

    /**
     * Current casting skill id
     *
     * @return
     */
    public int getCastingSkillId() {
        return castingSkill != null ? castingSkill.getSkillTemplate().getSkillId() : 0;
    }

    /**
     * Current casting skill
     *
     * @return
     */
    public Skill getCastingSkill() {
        return castingSkill;
    }

    /**
     * All abnormal effects are checked that disable movements
     *
     * @return
     */
    public boolean canPerformMove() {
        return !(getEffectController().isAbnormalState(EffectId.CANT_MOVE_STATE) || !isSpawned());
    }

    /**
     * All abnormal effects are checked that disable attack
     *
     * @return
     */
    public boolean canAttack() {
        return !(getEffectController().isAbnormalState(EffectId.CANT_ATTACK_STATE) || isCasting() || isInState(CreatureState.RESTING) || isInState(CreatureState.PRIVATE_SHOP));
    }

    /**
     * @return state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(CreatureState state) {
        this.state |= state.getId();
        observeController.notifyStateChangeObservers(state, true);
    }

    /**
     * @param state taken usually from templates
     */
    public void setState(int state) {
        this.state = state;
    }

    public void unsetState(CreatureState state) {
        this.state &= ~state.getId();
        observeController.notifyStateChangeObservers(state, false);
    }

    public boolean isInState(CreatureState state) {
        int isState = this.state & state.getId();

        if (isState == state.getId())
            return true;

        return false;
    }

    /**
     * @return visualState
     */
    public int getVisualState() {
        return visualState;
    }

    /**
     * @param visualState the visualState to set
     */
    public void setVisualState(CreatureVisualState visualState) {
        this.visualState |= visualState.getId();
    }

    public void unsetVisualState(CreatureVisualState visualState) {
        this.visualState &= ~visualState.getId();
    }

    public boolean isInVisualState(CreatureVisualState visualState) {
        int isVisualState = this.visualState & visualState.getId();

        if (isVisualState == visualState.getId())
            return true;

        return false;
    }

    /**
     * @return seeState
     */
    public int getSeeState() {
        return seeState;
    }

    /**
     * @param seeState the seeState to set
     */
    public void setSeeState(CreatureSeeState seeState) {
        this.seeState |= seeState.getId();
    }

    public void unsetSeeState(CreatureSeeState seeState) {
        this.seeState &= ~seeState.getId();
    }

    public boolean isInSeeState(CreatureSeeState seeState) {
        int isSeeState = this.seeState & seeState.getId();

        if (isSeeState == seeState.getId())
            return true;

        return false;
    }

    /**
     * @return the transformedModelId
     */
    public int getTransformedModelId() {
        return transformedModelId;
    }

    /**
     * @param transformedModelId the transformedModelId to set
     */
    public void setTransformedModelId(int transformedModelId) {
        this.transformedModelId = transformedModelId;
    }

    /**
     * @return the moveController
     */
    public MoveController getMoveController() {
        return moveController;
    }

    /**
     * @return the aggroList
     */
    public AggroList getAggroList() {
        return aggroList;
    }

    /**
     * PacketBroadcasterMask
     */
    private volatile byte packetBroadcastMask;

    /**
     * This is adding broadcast to player.
     */
    public final void addPacketBroadcastMask(BroadcastMode mode) {
        packetBroadcastMask |= mode.mask();

        PacketBroadcaster.getInstance().add(this);
    }

    /**
     * This is removing broadcast from player.
     */
    public final void removePacketBroadcastMask(BroadcastMode mode) {
        packetBroadcastMask &= ~mode.mask();
    }

    /**
     * Broadcast getter.
     */
    public final byte getPacketBroadcastMask() {
        return packetBroadcastMask;
    }

    /**
     * @return the observeController
     */
    public ObserveController getObserveController() {
        return observeController;
    }

    /**
     * @param visibleObject
     * @return
     */
    public boolean isEnemy(VisibleObject visibleObject) {
        if (visibleObject instanceof Npc)
            return isEnemyNpc((Npc) visibleObject);
        else if (visibleObject instanceof Player)
            return isEnemyPlayer((Player) visibleObject);
        else if (visibleObject instanceof Summon)
            return isEnemySummon((Summon) visibleObject);

        return false;
    }

    /**
     * @param summon
     * @return
     */
    protected boolean isEnemySummon(Summon summon) {
        return false;
    }

    /**
     * @param player
     * @return
     */
    protected boolean isEnemyPlayer(Player player) {
        return false;
    }

    /**
     * @param npc
     * @return
     */
    protected boolean isEnemyNpc(Npc npc) {
        return false;
    }

    public String getTribe() {
        return StringUtils.EMPTY;
    }

    /**
     * @param creature
     * @return
     */
    public boolean isAggressiveTo(Creature creature) {
        return false;
    }

    /**
     * @param npc
     * @return
     */
    public boolean isAggroFrom(Npc npc) {
        return false;
    }

    /**
     * @param npc
     * @return
     */
    public boolean isHostileFrom(Npc npc) {
        return false;
    }


    public boolean isSupportFrom(Npc npc) {
        return false;
    }

    /**
     * @param player
     * @return
     */
    public boolean isAggroFrom(Player player) {
        return false;
    }

    /**
     * @param summon
     * @return
     */
    public boolean isAggroFrom(Summon summon) {
        return isAggroFrom(summon.getMaster());
    }

    /**
     * @param visibleObject
     * @return
     */
    public boolean canSee(VisibleObject visibleObject) {
        if (visibleObject instanceof Npc)
            return canSeeNpc((Npc) visibleObject);
        else if (visibleObject instanceof Player)
            return canSeePlayer((Player) visibleObject);

        return true;
    }

    /**
     * @param visibleObject
     * @return
     */
    protected boolean canSeePlayer(Player visibleObject) {
        return true;
    }

    /**
     * @param visibleObject
     * @return
     */
    protected boolean canSeeNpc(Npc visibleObject) {
        return true;
    }

    /**
     * @return NpcObjectType.NORMAL
     */
    public NpcObjectType getNpcObjectType() {
        return NpcObjectType.NORMAL;
    }

    /**
     * For summons and different kind of servants<br>
     * it will return currently acting player.<br>
     * <p/>
     * This method is used for duel and enemy relations,<br>
     * rewards<br>
     *
     * @return Master of this creature or self
     */
    public Creature getMaster() {
        return this;
    }

    /**
     * For summons it will return summon object and for <br>
     * servants - player object.<br>
     * <p/>
     * Used to find attackable target for npcs.<br>
     *
     * @return acting master - player in case of servants
     */
    public Creature getActingCreature() {
        return this;
    }

    /**
     * @param skillId
     * @return
     */
    public boolean isSkillDisabled(int skillId) {
        if (skillCoolDowns == null)
            return false;

        Long coolDown = skillCoolDowns.get(skillId);
        if (coolDown == null)
            return false;


        if (coolDown < System.currentTimeMillis()) {
            skillCoolDowns.remove(skillId);
            return false;
        }

        return true;
    }

    /**
     * @param skillId
     * @return
     */
    public long getSkillCoolDown(int skillId) {
        if (skillCoolDowns == null || !skillCoolDowns.containsKey(skillId))
            return 0;

        return skillCoolDowns.get(skillId);
    }

    /**
     * @param skillId
     * @param time
     */
    public void setSkillCoolDown(int skillId, long time) {
        if (skillCoolDowns == null)
            skillCoolDowns = new FastMap<Integer, Long>().shared();

        skillCoolDowns.put(skillId, time);
    }

    /**
     * @return the skillCoolDowns
	 */
	public Map<Integer, Long> getSkillCoolDowns()
	{
		return skillCoolDowns;
	}
	
	/**
     * @param skillId
     */
	public void removeSkillCoolDown(int skillId)
	{
		if(skillCoolDowns == null)
			return;
		skillCoolDowns.remove(skillId);
	}

	/**
	 * @return isAdminNeutral value
	 */
	public int getAdminNeutral()
	{
		return isAdminNeutral;
	}
 	
 	/**
	 * @param newValue
	 */
	public void setAdminNeutral(int newValue)
	{
		isAdminNeutral = newValue;
	}


	/**
	 * @return isAdminEnmity value
	 */
	public int getAdminEnmity()
	{
		return isAdminEnmity;
	}
 	
 	/**
	 * @param newValue
	 */
	public void setAdminEnmity(int newValue)
	{
		isAdminEnmity = newValue;
	}

	/**
	 * @param lastAttack the lastAttack to set
	 */
	public void setLastAttack(long lastAttack)
	{
		this.lastAttack = lastAttack;
	}

	/**
	 * @return the lastAttack
	 */
	public long getLastAttack()
	{
		return lastAttack;
	}
}
