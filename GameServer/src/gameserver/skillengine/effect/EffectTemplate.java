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
package gameserver.skillengine.effect;

import com.aionemu.commons.utils.Rnd;
import gameserver.dataholders.DataManager;
import gameserver.model.SkillElement;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.templates.stats.NpcRank;
import gameserver.skillengine.change.Change;
import gameserver.skillengine.effect.modifier.ActionModifier;
import gameserver.skillengine.effect.modifier.ActionModifiers;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.HopType;
import gameserver.skillengine.model.SkillTemplate;
import gameserver.utils.stats.StatFunctions;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Effect")
public abstract class EffectTemplate {

    protected ActionModifiers modifiers;
    protected List<Change> change;
    @XmlAttribute
    protected int effectid;
    @XmlAttribute(required = true)
    protected int duration;
    @XmlAttribute(name = "randomtime")
    protected int randomTime;
    @XmlAttribute(name = "e")
    protected int position;
    @XmlAttribute(name = "basiclvl")
    protected int basicLvl;
    @XmlAttribute(name = "element")
    protected SkillElement element = SkillElement.NONE;
    @XmlElement(name = "subeffect")
    protected SubEffect subEffect;
    @XmlAttribute(name = "hoptype")
    protected HopType hopType;
    @XmlAttribute(name = "hopa")
    protected int hopA;
    @XmlAttribute(name = "hopb")
    protected int hopB;
    @XmlAttribute(name = "onfly")
    protected boolean onFly;

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @return the randomtime
     */
    public int getRandomTime() {
        return randomTime;
    }


    /**
     * @return the modifiers
     */
    public ActionModifiers getModifiers() {
        return modifiers;
    }


    /**
     * @return the change
     */
    public List<Change> getChange() {
        return change;
    }

    /**
     * @return the effectid
     */
    public int getEffectid() {
        return effectid;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return the basicLvl
     */
    public int getBasicLvl() {
        return basicLvl;
    }

    /**
     * @return the element
     */
    public SkillElement getElement() {
        return element;
    }

    /**
     * @return the onFly
     */
    public boolean isOnFly() {
        return onFly;
    }

    /**
     * @param value
     * @return
     */
    protected int applyActionModifiers(Effect effect, int value) {
        if (modifiers == null)
            return value;

        /**
         * Only one of modifiers will be applied now
         */
        for (ActionModifier modifier : modifiers.getActionModifiers()) {
            if (modifier.check(effect))
                return modifier.analyze(effect, value);
        }

        return value;
    }

    /**
     * Calculate effect result
     *
     * @param effect
     */
    public abstract void calculate(Effect effect);

    /**
     * Apply effect to effected
     *
     * @param effect
     */
    public abstract void applyEffect(Effect effect);

    /**
     * Start effect on effected
     *
     * @param effect
     */
    public void startEffect(Effect effect) {
    }

    ;

    /**
     * @param effect
     */
    public void calculateSubEffect(Effect effect) {
        if (subEffect == null)
            return;

        SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(subEffect.getSkillId());
        int duration = template.getEffectsDuration();
        Effect newEffect = new Effect(effect.getEffector(), effect.getEffected(), template, template.getLvl(), duration);
        newEffect.initialize();
        effect.setSpellStatus(newEffect.getSpellStatus());
        effect.setSubEffect(newEffect);
    }

    /**
     * Hate will be added to result value only if particular
     * effect template has success result
     *
     * @param effect
     */
    public void calculateHate(Effect effect) {
        if (hopType == null)
            return;

        if (effect.getSuccessEffect().isEmpty())
            return;

        int currentHate = effect.getEffectHate();
        if (hopType != null) {
            switch (hopType) {
                case DAMAGE:
                    currentHate += effect.getReserved1();
                    break;
                case SKILLLV:
                    int skillLvl = effect.getSkillLevel();
                    currentHate += hopB + hopA * skillLvl;
                default:
                    break;
            }
        }
        if (currentHate == 0)
            currentHate = 1;
        effect.setEffectHate(StatFunctions.calculateHate(effect.getEffector(), currentHate));
    }

    /**
     * @param effect
     */
    public void startSubEffect(Effect effect) {
        if (subEffect == null)
            return;

        effect.getSubEffect().applyEffect();
    }

    /**
     * Do periodic effect on effected
     *
     * @param effect
     */
    public void onPeriodicAction(Effect effect) {
    }

    ;

    /**
     * End effect on effected
     *
     * @param effect
     */
    public void endEffect(Effect effect) {
    }

    ;

    public boolean calculateEffectResistRate(Effect effect, StatEnum statEnum) {
        // TODO: Need correct value in client. 1000 = 100%
        int effectPower = 1000;

        //first resist?
        if (statEnum != null) {
            if (effect.getEffected() == null || effect.getEffected().getGameStats() == null) {
                return false;
            }

            //magical resist calc
            if (statEnum == StatEnum.MAGICAL_RESIST) {
                return !(Rnd.get(0, 100) < StatFunctions.calculateMagicalResistRate(effect.getEffector(), effect.getEffected()));
            }

            switch (statEnum) {
                case BLIND_RESISTANCE:
                case CHARM_RESISTANCE:
                case CONFUSE_RESISTANCE:
                case CURSE_RESISTANCE:
                case DISEASE_RESISTANCE:
                case FEAR_RESISTANCE:
                case OPENAREIAL_RESISTANCE:
                case PARALYZE_RESISTANCE:
                case PERIFICATION_RESISTANCE:
                case ROOT_RESISTANCE:
                case SILENCE_RESISTANCE:
                case SLEEP_RESISTANCE:
                case SLOW_RESISTANCE:
                case SNARE_RESISTANCE:
                case SPIN_RESISTANCE:
                case STAGGER_RESISTANCE:
                case STUMBLE_RESISTANCE:
                case STUN_RESISTANCE: {
                    //resist for bosses	// one of these is not needed?
                    if (effect.getEffected() instanceof Npc) {
                        NpcRank rank = ((Npc) effect.getEffected()).getObjectTemplate().getRank();
                        if (rank == NpcRank.HERO || rank == NpcRank.LEGENDARY)
                            return false;
                    }
                    //resist for bosses // one of these is not needed?
                    if (effect.getEffected().getGameStats().getCurrentStat(StatEnum.ALLRESIST) > 0)
                        return false;
                    if (effect.getEffected().getGameStats().getCurrentStat(StatEnum.ABNORMAL_RESISTANCE_ALL) > 0)
                        effectPower -= effect.getEffected().getGameStats().getCurrentStat(StatEnum.ABNORMAL_RESISTANCE_ALL);
                }
                case BLEED_RESISTANCE:
                case POISON_RESISTANCE:
                    break;
            }

            int stat = effect.getEffected().getGameStats().getCurrentStat(statEnum);
            effectPower -= stat;

            int attackerLevel = effect.getEffector().getLevel();
            int targetLevel = effect.getEffected().getLevel();

            /*
                 float multipler = 0.0f;
                int differ = (targetLevel - attackerLevel);
                //lvl mod
                 if(differ > 0 && differ < 8 )
                {
                    multipler = differ / 10f;
                     effectPower -= Math.round((effectPower * multipler));
                }
                else if (differ >= 8)
                 {
                    effectPower -= Math.round((effectPower * 0.80f));
                }
                if (effect.getEffected() instanceof Npc)
                {
                    float hpGaugeMod = ((Npc) effect.getEffected()).getObjectTemplate().getHpGauge();
                    effectPower -= (200*(1+(hpGaugeMod/10)));
                }
                */
            if (targetLevel > attackerLevel) {
                int differ = targetLevel - attackerLevel;
                effectPower -= effectPower * (differ / 10);
            }

            boolean result = (Rnd.get(0, 1000) < effectPower);
			
 	 		return result;
 		} 
  		
 		return true;
	} 
}
