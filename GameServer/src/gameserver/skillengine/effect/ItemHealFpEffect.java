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
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemHealFpEffect")
public class ItemHealFpEffect extends AbstractHealEffect {

    @Override
    public void applyEffect(Effect effect) {
        effect.getEffected().getLifeStats().increaseFp(-effect.getReserved1());
    }

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect);
        effect.addSucessEffect(this);
    }

    @Override
    protected int getCurrentStatValue(Effect effect) {
        return effect.getEffected().getLifeStats().getCurrentFp();
    }

    @Override
    protected int getMaxStatValue(Effect effect) {
        return effect.getEffected().getGameStats().getCurrentStat(StatEnum.FLY_TIME);
    }

}
