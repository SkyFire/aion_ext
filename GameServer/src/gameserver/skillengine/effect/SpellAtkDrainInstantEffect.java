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

import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.skillengine.action.DamageType;
import gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpellAtkDrainInstantEffect")
public class SpellAtkDrainInstantEffect extends DamageEffect {
    @XmlAttribute(name = "hp_percent")
    protected int HPpercent;
    @XmlAttribute(name = "mp_percent")
    protected int MPpercent;

    @Override
    public void applyEffect(Effect effect) {
        super.applyEffect(effect);
        if (HPpercent > 0)
            effect.getEffector().getLifeStats().increaseHp(TYPE.NATURAL_HP, effect.getReserved1() * HPpercent / 100);

        if (MPpercent > 0)
            effect.getEffector().getLifeStats().increaseMp(TYPE.NATURAL_MP, effect.getReserved1() * MPpercent / 100);
    }

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect, DamageType.MAGICAL);
    }

}
