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

import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractHealEffect")
public abstract class AbstractHealEffect extends EffectTemplate {
    @XmlAttribute(required = true)
    protected int value;

    @XmlAttribute
    protected int delta;

    @XmlAttribute
    protected boolean percent;

    @Override
    public void calculate(Effect effect) {
        int valueWithDelta = value + delta * effect.getSkillLevel();

        int healValue = valueWithDelta;
        if (percent) {
            int currentValue = getCurrentStatValue(effect);
            int maxValue = getMaxStatValue(effect);
            int possibleHealValue = maxValue * valueWithDelta / 100;
            healValue = maxValue - currentValue < possibleHealValue ? maxValue - currentValue : possibleHealValue;
        }
        // +100 = 100% heal min value for all class.
        // Boost heal formula = boost heal value / 10 = additional % heal value.
        //each player start with boost_heal 100, therefore -100
        float boostHeal = ((float) (effect.getEffector().getGameStats().getCurrentStat(StatEnum.BOOST_HEAL) - 100) / 1000f);
        float healRate = effect.getEffector().getController().getHealRate();
		if (effect.isItemheal())
			effect.setReserved1(-healValue);
		else
			effect.setReserved1(Math.round(-healValue * (healRate + boostHeal)));

    }

    /**
     * @param effect
     * @return
     */
    protected int getCurrentStatValue(Effect effect) {
        return effect.getEffected().getLifeStats().getCurrentHp();
    }

    /**
     * @param effect
     * @return
     */
    protected int getMaxStatValue(Effect effect) {
        return effect.getEffected().getGameStats().getCurrentStat(StatEnum.MAXHP);
    }

}
