/*
 * This file is part of Aion X EMU <aionxemu>.
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.skillengine.model;

import com.aionemu.commons.utils.Rnd;
import gameserver.configs.main.CustomConfig;
import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.ActionObserver.ObserverType;
import gameserver.controllers.movement.StartMovingListener;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.serverpackets.SM_CASTSPELL;
import gameserver.network.aion.serverpackets.SM_CASTSPELL_END;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.questEngine.QuestEngine;
import gameserver.questEngine.model.QuestCookie;
import gameserver.restrictions.RestrictionsManager;
import gameserver.skillengine.SkillEngine;
import gameserver.skillengine.action.Action;
import gameserver.skillengine.action.Actions;
import gameserver.skillengine.condition.Condition;
import gameserver.skillengine.condition.Conditions;
import gameserver.skillengine.effect.EffectId;
import gameserver.skillengine.properties.*;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ATracer
 */
public class Skill {
    private List<Creature> effectedList;

    private int maxEffected = 1;

    private Creature firstTarget;

    private Creature effector;

    private int skillLevel;

    private int skillStackLvl;

    private StartMovingListener conditionChangeListener;

    private SkillTemplate skillTemplate;

    private boolean firstTargetRangeCheck = true;

    private ItemTemplate itemTemplate;

    private int targetType;

    private boolean chainSuccess = true;

    private boolean isCancelled = false;

    private float x;
    private float y;
    private float z;

    private int changeMpConsumptionValue;

    /**
     * Duration that depends on BOOST_CASTING_TIME
     */
    private int duration;

    private FirstTargetAttribute firstTargetAttribute;
    private TargetRangeAttribute targetRangeAttribute;

    @SuppressWarnings("unused")
    private TargetRelationAttribute targetRelationAttribute;


    public enum SkillType {
        CAST,
        ITEM,
        PASSIVE
    }

    /**
     * Each skill is a separate object upon invocation
     * Skill level will be populated from player SkillList
     *
     * @param skillTemplate
     * @param effector
     * @param world
     */
    public Skill(SkillTemplate skillTemplate, Player effector, Creature firstTarget) {
        this(skillTemplate, effector,
                effector.getSkillList().getSkillLevel(skillTemplate.getSkillId()), firstTarget);
    }

    /**
     * @param skillTemplate
     * @param effector
     * @param skillLvl
     * @param firstTarget
     */
    public Skill(SkillTemplate skillTemplate, Creature effector, int skillLvl, Creature firstTarget) {
        this.effectedList = new ArrayList<Creature>();
        this.conditionChangeListener = new StartMovingListener();
        this.firstTarget = firstTarget;
        this.skillLevel = skillLvl;
        this.skillStackLvl = skillTemplate.getLvl();
        this.skillTemplate = skillTemplate;
        this.effector = effector;
    }

    /**
     * Check if the skill can be used
     *
     * @return True if the skill can be used
     */
    public boolean canUseSkill() {

        if (!setProperties(skillTemplate.getInitproperties()))
            return false;

        if (!preCastCheck())
            return false;

        if (!setProperties(skillTemplate.getSetproperties()))
            return false;


        effector.setCasting(this);
        Iterator<Creature> effectedIter = effectedList.iterator();
        while (effectedIter.hasNext()) {
            Creature effected = effectedIter.next();
            if (effected == null)
                effected = effector;

            if (effector instanceof Player) {
                if ((!RestrictionsManager.canAffectBySkill((Player) effector, effected)) && (skillTemplate.getSkillId() != 1968))
                    effectedIter.remove();
            } else {
                if ((effector.getEffectController().isAbnormalState(EffectId.CANT_ATTACK_STATE)) && (skillTemplate.getSkillId() != 1968))
                    effectedIter.remove();
            }
        }
        effector.setCasting(null);

        //check for abyss skill, those can be used only in abyss or in balaurea
        if (skillTemplate.getStack().contains("ABYSS_RANKERSKILL"))
            if (((Player) effector).getWorldType() != WorldType.ABYSS && ((Player) effector).getWorldType() != WorldType.BALAUREA)
                return false;

        // TODO Enable non-targeted, non-point AOE skills to trigger.
        if (targetType == 0 && effectedList.size() == 0 && !checkNonTargetAOE()) {
            return false;
        }
        return true;
    }

    /**
     * Skill entry point
     */
    public void useSkill() {
        if (!canUseSkill())
            return;
        if (skillTemplate == null)
            return;

        if (effector instanceof Player) {
            QuestEngine.getInstance().onSkillUse(new QuestCookie(effector, (Player) effector, 0, -1), skillTemplate.getSkillId());
        }

        changeMpConsumptionValue = 0;

        effector.getObserveController().notifySkilluseObservers(this);

        //start casting
        effector.setCasting(this);

        int skillDuration = skillTemplate.getDuration();
        int currentStat = effector.getGameStats().getCurrentStat(StatEnum.BOOST_CASTING_TIME);
        this.duration = skillDuration + Math.round(skillDuration * (100 - currentStat) / 100f);

        int cooldown = skillTemplate.getCooldown();
        if (cooldown != 0)
            effector.setSkillCoolDown(skillTemplate.getSkillId(), cooldown * 100 + this.duration + System.currentTimeMillis());

        if (duration < 0)
            duration = 0;

        // Summoning Alacrity
        // http://www.aiondatabase.com/skill/1778
        if (skillTemplate.getName().contains("Elemental_") || skillTemplate.getName().contains("Servent_")) {
            if (effector instanceof Player) {
                Player p = (Player) effector;
                if (p.getEffectController() != null && p.getEffectController().hasAbnormalEffect(1778))
                    duration = 0;
            }
		}

		//locked up skill duration
		switch(skillTemplate.getSkillId()){
			case (8198): //move scroll
			case (1443): //Sleeping Strom
			case (1497): //Tranquilzing Cloud
			case (1495): //Sleep
			case (1454): //Curse of Roots
			case (1685): //Fear Shriek
			case (1636): //Fear
			case (2006): //Hand of Torpor
			case (1803): //Bandage Heal

			case (1804): //Herb Treatment
			case (1805):
			case (1825):
			case (1827):

			case (1823): //Mana Treatment
			case (1824):
			case (1826):
			case (1828):
				duration = skillDuration;
				break;
			default:
				break;
		}

        if (skillTemplate.isActive() || skillTemplate.isToggle()) {
            startCast();
        }

        if (skillTemplate.isStance()) {
            final int skillId = skillTemplate.getSkillId();

            final ActionObserver jumpObserver = new ActionObserver(ObserverType.JUMP) {
                @Override
                public void jump() {
                    effector.getEffectController().removeNoshowEffect(skillId);
                }
            };
            final ActionObserver skillObserver = new ActionObserver(ObserverType.SKILLUSE) {
                @Override
                public void skilluse(Skill skill) {
                    effector.getEffectController().removeNoshowEffect(skillId);
                    effector.getObserveController().removeObserver(jumpObserver);
                }
            };

            effector.getObserveController().attach(skillObserver);
            effector.getObserveController().attach(jumpObserver);
        }

        effector.getObserveController().attach(conditionChangeListener);

        if (this.duration > 0) {
            schedule(this.duration);
        } else {
            endCast();
        }
    }

    /**
     * Penalty success skill
     */
    private void startPenaltySkill() {
        if (skillTemplate.getPenaltySkillId() == 0)
            return;

        Skill skill = SkillEngine.getInstance().getSkill(effector, skillTemplate.getPenaltySkillId(), 1, firstTarget);
        skill.useSkill();
    }

    /**
     * Start casting of skill
     */
    private void startCast() {
        int targetObjId = firstTarget != null ? firstTarget.getObjectId() : 0;

        switch (targetType) {
            case 0: // PlayerObjectId as Target
                PacketSendUtility.broadcastPacketAndReceive(effector,
                        new SM_CASTSPELL(
                                effector.getObjectId(),
                                skillTemplate.getSkillId(),
                                skillLevel,
                                targetType,
                                targetObjId,
                                this.duration));
                break;

            case 1: // XYZ as Target
                PacketSendUtility.broadcastPacketAndReceive(effector,
                        new SM_CASTSPELL(
                                effector.getObjectId(),
                                skillTemplate.getSkillId(),
                                skillLevel,
                                targetType,
                                x, y, z,
                                this.duration));
				break;
				
			case 3: // Target not in sight?
				PacketSendUtility.broadcastPacketAndReceive(effector,
					new SM_CASTSPELL(
						effector.getObjectId(),
						skillTemplate.getSkillId(),
						skillLevel,
						targetType,
						0,
						this.duration));
                break;
        }
    }

    public void cancelCast() {
        isCancelled = true;
    }

    /**
     * Apply effects and perform actions specified in skill template
     */
    private void endCast() {
        if (!effector.isCasting() || isCancelled)
            return;

        // if target out of range
        if (skillTemplate == null)
            return;
        if (effector instanceof Player && firstTarget != null && firstTarget != effector) {
            float distance = 0;
            if (skillTemplate.getSetproperties() != null) {
                //add firsttargetrangeproperty
                for (Property prop : skillTemplate.getSetproperties().getProperties()) {
                    if (prop instanceof FirstTargetRangeProperty)
                        distance = (float) ((FirstTargetRangeProperty) prop).getValue();
                }
            }
            //add weaponrange if needed
            if (skillTemplate.getInitproperties() != null) {
                for (Property prop : skillTemplate.getInitproperties().getProperties()) {
                    if (prop instanceof AddWeaponRangeProperty)
                        distance += (float) ((Player) effector).getGameStats().getCurrentStat(StatEnum.ATTACK_RANGE) / 1000;
                }
            }
            //tolerance
            distance += 1.5;

            boolean firstTargetIsInSameMapInstance = true;
            if (effector.getInstanceId() != firstTarget.getInstanceId())
                firstTargetIsInSameMapInstance = false;
            if (((float) MathUtil.getDistance(effector, firstTarget) > distance || (!firstTargetIsInSameMapInstance))) {
                ((Player) effector).getController().cancelCurrentSkill();
                PacketSendUtility.sendPacket((Player) effector, SM_SYSTEM_MESSAGE.STR_ATTACK_TOO_FAR_FROM_TARGET());
                return;
            }
        }

        //stop casting must be before preUsageCheck()
        effector.setCasting(null);

        checkSkillSetException();

        if (!preUsageCheck())
            return;

        /**
         * Create effects and precalculate result
         */
        int spellStatus = 0;

        List<Effect> effects = new ArrayList<Effect>();
        if (skillTemplate.getEffects() != null) {
            for (Creature effected : effectedList) {
                Effect effect = new Effect(effector, effected, skillTemplate, skillLevel, 0, itemTemplate);
                effect.initialize();
                spellStatus = effect.getSpellStatus().getId();
                effects.add(effect);
            }
        }

        // Check if Chain Skill Trigger Rate is Enabled
        if (CustomConfig.SKILL_CHAIN_TRIGGERRATE) {
            // Check Chain Skill Result
            int chainProb = skillTemplate.getChainSkillProb();
            if (chainProb != 0) {
                if (Rnd.get(100) < chainProb)
                    this.chainSuccess = true;
                else
                    this.chainSuccess = false;
            }
        } else {
            this.chainSuccess = true;
        }

        /**
         * If castspell - send SM_CASTSPELL_END packet
         */
        if (skillTemplate.isActive() || skillTemplate.isToggle()) {
            sendCastspellEnd(spellStatus, effects);
        }

        /**
         * Perform necessary actions (use mp,dp items etc)
         */
        Actions skillActions = skillTemplate.getActions();
        if (skillActions != null) {
            for (Action action : skillActions.getActions()) {
                action.act(this);
            }
        }

        /**
         * Apply effects to effected objects
         */
        for (Effect effect : effects) {
            effect.applyEffect();
        }

        /**
         * Use penalty skill (now 100% success)
         */
        startPenaltySkill();
    }

    /**
     * @param spellStatus
     * @param effects
     */
    private void sendCastspellEnd(int spellStatus, List<Effect> effects) {
        switch (targetType) {
            case 0: // PlayerObjectId as Target
                PacketSendUtility.broadcastPacketAndReceive(effector,
                        new SM_CASTSPELL_END(
                                effector,
                                firstTarget, // Need all targets...
                                effects,
                                skillTemplate.getSkillId(),
                                skillLevel,
                                skillTemplate.getCooldown(),
                                chainSuccess,
                                spellStatus));
                break;

            case 1: // XYZ as Target
                PacketSendUtility.broadcastPacketAndReceive(effector,
                        new SM_CASTSPELL_END(
                                effector,
                                firstTarget, // Need all targets...
                                effects,
                                skillTemplate.getSkillId(),
                                skillLevel,
                                skillTemplate.getCooldown(),
                                chainSuccess,
                                spellStatus, x, y, z));
                break;

			case 3: // Target not in sight?
				PacketSendUtility.broadcastPacketAndReceive(effector,
					new SM_CASTSPELL_END(
						effector,
						firstTarget,
						effects,
						skillTemplate.getSkillId(),
						skillLevel,
						skillTemplate.getCooldown(),
						chainSuccess,
						spellStatus));
				break;
        }
    }

    /**
     * Schedule actions/effects of skill (channeled skills)
     */
    private void schedule(int delay) {
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            public void run() {
                endCast();
            }
        }, delay);
    }

    /**
     * Check all conditions before starting cast
     */
    private boolean preCastCheck() {
        Conditions skillConditions = skillTemplate.getStartconditions();
        return checkConditions(skillConditions);
    }

    /**
     * Check all conditions before using skill
     */
    private boolean preUsageCheck() {
        Conditions skillConditions = skillTemplate.getUseconditions();
        return checkConditions(skillConditions);
    }

    private boolean checkConditions(Conditions conditions) {
        if (conditions != null) {
            for (Condition condition : conditions.getConditions()) {
                if (!condition.verify(this)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean setProperties(Properties properties) {
        if (properties != null) {
            for (Property property : properties.getProperties()) {
                if (!property.set(this)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param FirstTargetAttributethe firstTargetAttribute to set
     */
    public void setFirstTargetProperty(FirstTargetAttribute firstTargetAttribute) {
        this.firstTargetAttribute = firstTargetAttribute;
    }

    /**
     * @param targetRangeAttribute the targetRangeAttribute to set
     */
    public void setTargetRangeAttribute(TargetRangeAttribute targetRangeAttribute) {
        this.targetRangeAttribute = targetRangeAttribute;
    }

    /**
     * @param targetRangeAttribute the targetRelationAttribute to set
     */
    public void setTargetRelationAttribute(TargetRelationAttribute targetRelationAttribute) {
        this.targetRelationAttribute = targetRelationAttribute;
    }

    /**
     * @return true if the present skill is a non-targeted, non-point AOE skill
     */
    public boolean checkNonTargetAOE() {
        return (firstTargetAttribute == FirstTargetAttribute.ME
                && targetRangeAttribute == TargetRangeAttribute.AREA);
    }

    /**
     * Check for skillset_exception
     */
    private void checkSkillSetException() {
        if (effector instanceof Player) {
            if (skillTemplate.getSkillSetException() != 0) {
                if (isPartySkill() && ((Player) effector).getPlayerGroup() != null) {
                    for (Player p : ((Player) effector).getPlayerGroup().getMembers()) {
                        //+4 because of targetrangeproperty
                        if (MathUtil.isIn3dRange(p, effector, skillTemplate.getTargetRangeProperty().getDistance() + 4))
                            p.getEffectController().removeEffectBySetNumber(skillTemplate.getSkillSetException());
                    }
                } else
                    effector.getEffectController().removeEffectBySetNumber(skillTemplate.getSkillSetException());
            }
        }
    }

    /**
     * @param value is the changeMpConsumptionValue to set
     */
    public void setChangeMpConsumption(int value) {
        changeMpConsumptionValue = value;
    }

    /**
     * @return the changeMpConsumptionValue
     */
    public int getChangeMpConsumption() {
        return changeMpConsumptionValue;
    }

    /**
     * @return the effectedList
     */
    public List<Creature> getEffectedList() {
        return effectedList;
    }

    /**
     * Set the maximum number of effected targets.
     *
     * @param maxEffected
     */
    public void setMaxEffected(int maxEffected) {
        this.maxEffected = maxEffected;
    }

    /**
     * @return The maximum number of effected targets.
     */
    public int getMaxEffected() {
        return maxEffected;
    }

    /**
     * @return the effector
     */
    public Creature getEffector() {
        return effector;
    }

    /**
     * @return the skillLevel
     */
    public int getSkillLevel() {
        return skillLevel;
    }

    /**
     * @return the skillStackLvl
     */
    public int getSkillStackLvl() {
        return skillStackLvl;
    }

    /**
     * @return the conditionChangeListener
     */
    public StartMovingListener getConditionChangeListener() {
        return conditionChangeListener;
    }

    /**
     * @return the skillTemplate
     */
    public SkillTemplate getSkillTemplate() {
        return skillTemplate;
    }

    /**
     * @return the firstTarget
     */
    public Creature getFirstTarget() {
        return firstTarget;
    }

    /**
     * @param firstTarget the firstTarget to set
     */
    public void setFirstTarget(Creature firstTarget) {
        this.firstTarget = firstTarget;
    }

    /**
     * @return true or false
     */
    public boolean isPassive() {
        return skillTemplate.getActivationAttribute() == ActivationAttribute.PASSIVE;
    }

    /**
     * @return the firstTargetRangeCheck
     */
    public boolean isFirstTargetRangeCheck() {
        return firstTargetRangeCheck;
    }

    /**
     * @return boolean if its skill applied to party
     */
    public boolean isPartySkill() {
        return (targetRangeAttribute == TargetRangeAttribute.PARTY);
    }

    /**
     * @return boolean if its an Area of Effect Enemy skill.
     */
    public boolean isAreaEnemySkill() {
        return (targetRangeAttribute == TargetRangeAttribute.AREA &&
            targetRelationAttribute == TargetRelationAttribute.ENEMY);
    }

    /**
     * @param firstTargetRangeCheck the firstTargetRangeCheck to set
     */
    public void setFirstTargetRangeCheck(boolean firstTargetRangeCheck) {
        this.firstTargetRangeCheck = firstTargetRangeCheck;
    }

    /**
     * @param itemTemplate the itemTemplate to set
     */
    public void setItemTemplate(ItemTemplate itemTemplate) {
        this.itemTemplate = itemTemplate;
    }

    /**
     * @param targetType
     * @param x
     * @param y
     * @param z
     */
    public void setTargetType(int targetType, float x, float y, float z)
	{
		this.targetType = targetType;
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
