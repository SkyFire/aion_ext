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
package gameserver.model.gameobjects.stats;

import gameserver.model.SkillElement;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.id.ItemStatEffectId;
import gameserver.model.gameobjects.stats.id.StatEffectId;
import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.items.ItemSlot;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author xavier
 */
public class CreatureGameStats<T extends Creature> {
    protected static final Logger log = Logger
            .getLogger(CreatureGameStats.class);

    private static final int ATTACK_MAX_COUNTER = Integer.MAX_VALUE;

    protected Map<StatEnum, Stat> stats;
    protected Map<StatEffectId, TreeSet<StatModifier>> statsModifiers;

    private int attackCounter = 0;
    protected T owner = null;

    /**
     * @param owner
     */
    protected CreatureGameStats(T owner) {
        this.owner = owner;
        this.stats = Collections.synchronizedMap(new HashMap<StatEnum, Stat>());
        this.statsModifiers = Collections.synchronizedMap(new HashMap<StatEffectId, TreeSet<StatModifier>>());
    }

    /**
     * @return the atcount
     */
    public int getAttackCounter() {
        return attackCounter;
    }

    /**
     * @param atcount the atcount to set
     */
    protected void setAttackCounter(int attackCounter) {
        if (attackCounter <= 0) {
            this.attackCounter = 1;
        } else {
            this.attackCounter = attackCounter;
        }
    }

    public void increaseAttackCounter() {
        if (attackCounter == ATTACK_MAX_COUNTER) {
            this.attackCounter = 1;
        } else {
            this.attackCounter++;
        }
    }

    /**
     * @param stat
     * @param value
     */
    public void setStat(StatEnum stat, int value) {
        setStat(stat, value, false);
    }

    /**
     * @param stat
     * @return
     */
    public int getBaseStat(StatEnum stat) {
        if (stats.containsKey(stat))
            return stats.get(stat).getBase();
        else
            return 0;
    }

    /**
     * @param stat
     * @return
     */
    public int getStatBonus(StatEnum stat) {
        if (stats.containsKey(stat))
            return stats.get(stat).getBonus();
        else
            return 0;
    }

    /**
     * @param stat
     * @return
     */
    public int getCurrentStat(StatEnum stat) {
        if (stats.containsKey(stat)) {
            Stat statObject = stats.get(stat);
            if (statObject == null)
                return 0;
            else
                return statObject.getCurrent();
        }
        else
            return 0;
    }

    /**
     * @param stat
     * @return
     */
    public int getOldStat(StatEnum stat) {
        if (stats.containsKey(stat))
            return stats.get(stat).getOld();
        else
            return 0;
    }

    /**
     * @param id
     * @param modifiers
     */
    public void addModifiers(StatEffectId id, TreeSet<StatModifier> modifiers) {
        if (modifiers == null || statsModifiers.containsKey(id))
            return;

        statsModifiers.put(id, modifiers);
        recomputeStats();
    }

    /**
     * @return True if the StatEffectId is already added
     */
    public boolean effectAlreadyAdded(StatEffectId id) {
        return statsModifiers.containsKey(id);
    }

    /**
     * Recomputation of all stats
     */
    public void recomputeStats() {
        resetStats();
        Map<StatEnum, StatModifiers> orderedModifiers = new HashMap<StatEnum, StatModifiers>();

        synchronized (statsModifiers) {
            for (Entry<StatEffectId, TreeSet<StatModifier>> modifiers : statsModifiers.entrySet()) {
                StatEffectId eid = modifiers.getKey();
                int slots = 0;

                if (modifiers.getValue() == null)
                    continue;

                for (StatModifier modifier : modifiers.getValue()) {
                    if (eid instanceof ItemStatEffectId) {
                        slots = ((ItemStatEffectId) eid).getSlot();
                    }
                    if (slots == 0)
                        slots = ItemSlot.NONE.getSlotIdMask();
                    if (modifier.getStat().isMainOrSubHandStat() && owner instanceof Player) {
                        if (slots != ItemSlot.MAIN_HAND.getSlotIdMask() && slots != ItemSlot.SUB_HAND.getSlotIdMask()) {
                            if (((Player) owner).getEquipment().getOffHandWeaponType() != null)
                                slots = ItemSlot.MAIN_OR_SUB.getSlotIdMask();
                            else {
                                slots = ItemSlot.MAIN_HAND.getSlotIdMask();
                                setStat(StatEnum.OFF_HAND_ACCURACY, 0, false);
                            }
                        } else if (slots == ItemSlot.MAIN_HAND.getSlotIdMask())
                            setStat(StatEnum.MAIN_HAND_POWER, 0);
                    }

                    List<ItemSlot> oSlots = ItemSlot.getSlotsFor(slots);
                    for (ItemSlot slot : oSlots) {
                        List<StatEnum> statToModifies = new ArrayList<StatEnum>();
                        if(modifier.getStatToModifies().size() > 0){
                            statToModifies = modifier.getStatToModifies();//for WeaponMastery
                        }else{
                        	statToModifies.add(modifier.getStat().getMainOrSubHandStat(slot));
                        }
                        
                        for(StatEnum statToModify : statToModifies){
	                        if ((slot == ItemSlot.SUB_HAND && statToModify == StatEnum.PARRY && !modifier.isBonus())
	                            || (slot == ItemSlot.SUB_HAND && statToModify == StatEnum.MAGICAL_ACCURACY && !modifier.isBonus()))
	                           continue;
	
	                        if (!orderedModifiers.containsKey(statToModify)) {
	                            orderedModifiers.put(statToModify, new StatModifiers());
	                        }
	                    	if(!modifier.isCanDuplicate() && orderedModifiers.get(statToModify).getModifiers(modifier.getPriority()).contains(modifier)){
	                    		continue;
	                        }
                            orderedModifiers.get(statToModify).add(modifier);
                        }
                    }
                }
            }
        }

        for (Entry<StatEnum, StatModifiers> entry : orderedModifiers.entrySet()) {
            applyModifiers(entry.getKey(), entry.getValue());
        }

        setStat(StatEnum.ATTACK_SPEED, Math.round(getBaseStat(StatEnum.MAIN_HAND_ATTACK_SPEED)
                + getBaseStat(StatEnum.OFF_HAND_ATTACK_SPEED) * 0.25f), false);

        setStat(StatEnum.ATTACK_SPEED, getStatBonus(StatEnum.MAIN_HAND_ATTACK_SPEED)
                + getStatBonus(StatEnum.OFF_HAND_ATTACK_SPEED), true);

        orderedModifiers.clear();
    }

    /**
     * @param id
     */
    public void endEffect(StatEffectId id) {
        statsModifiers.remove(id);
        recomputeStats();
    }

    /**
     * @param element
     * @return
     */
    public int getMagicalDefenseFor(SkillElement element) {
        switch (element) {
            case EARTH:
                return getCurrentStat(StatEnum.EARTH_RESISTANCE);
            case FIRE:
                return getCurrentStat(StatEnum.FIRE_RESISTANCE);
            case WATER:
                return getCurrentStat(StatEnum.WATER_RESISTANCE);
            case WIND:
                return getCurrentStat(StatEnum.WIND_RESISTANCE);
            default:
                return 0;
        }
    }

    /**
     * Reset all stats
     */
    protected void resetStats() {
        synchronized (stats) {
            for (Stat stat : stats.values()) {
                stat.reset();
            }
        }
    }

    /**
     * @param stat
     * @param modifiers
     */
    protected void applyModifiers(final StatEnum stat, StatModifiers modifiers) {
        if (modifiers == null)
            return;

        if (!stats.containsKey(stat)) {
            initStat(stat, 0);
        }

        Stat oStat = stats.get(stat);
        int newValue;

        for (StatModifierPriority priority : StatModifierPriority.values()) {
            for (StatModifier modifier : modifiers.getModifiers(priority)) {
                newValue = modifier.apply(oStat.getBase(), oStat.getCurrent());
                oStat.increase(newValue, modifier.isBonus());
            }
        }

        if (stat == StatEnum.MAXHP || stat == StatEnum.MAXMP) {
            final int oldValue = getOldStat(stat);
            final int newVal = getCurrentStat(stat);
            if (oldValue == newVal) {
                return;
            }
            final CreatureLifeStats<? extends Creature> lifeStats = owner.getLifeStats();
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    switch (stat) {
                        case MAXHP:
                            if (oldValue == 0 || newVal == 0) {
                                lifeStats.setCurrentHp(newVal);
                                break;
                            }
                            int hp = lifeStats.getCurrentHp();
                            hp = (int) (hp * ((float) newVal / oldValue));
                            lifeStats.setCurrentHp(hp);
                            break;
                        case MAXMP:
                            if (oldValue == 0 || newVal == 0) {
                                lifeStats.setCurrentMp(newVal);
                                break;
                            }
                            int mp = lifeStats.getCurrentMp();
                            mp = (int) (mp * ((float) newVal / oldValue));
                            lifeStats.setCurrentMp(mp);
                            break;
                    }
                }
            }, 0);
        }
    }


    /**
     * @param stat
     * @param value
     */
    protected void initStat(StatEnum stat, int value) {
        if (!stats.containsKey(stat))
            stats.put(stat, new Stat(stat, value));
        else {
            stats.get(stat).reset();
            stats.get(stat).set(value, false);
        }
    }

    /**
     * @param stat
     * @param value
     * @param bonus
     */
    protected void setStat(StatEnum stat, int value, boolean bonus) {
        if (!stats.containsKey(stat)) {
            stats.put(stat, new Stat(stat, 0));
        }
        stats.get(stat).set(value, bonus);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("owner:" + owner.getObjectId());
        for(Stat stat : stats.values())
		{
			sb.append(stat);
		}
		sb.append('}');
		return sb.toString();
	}
}
