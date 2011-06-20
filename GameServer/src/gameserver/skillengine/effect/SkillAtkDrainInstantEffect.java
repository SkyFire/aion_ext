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
import gameserver.skillengine.model.HealType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillAtkDrainInstantEffect")
public class SkillAtkDrainInstantEffect extends DamageEffect {
    @XmlAttribute
    protected int percent;
    @XmlAttribute(name = "heal_type")
    protected HealType healType;

    @Override
    public void applyEffect(Effect effect) {
        super.applyEffect(effect);
        int value = effect.getReserved1() * percent / 100;
        switch (healType) {
            case HP:
                effect.getEffector().getLifeStats().increaseHp(TYPE.NATURAL_HP, value);
                break;
            case MP:
                effect.getEffector().getLifeStats().increaseMp(TYPE.NATURAL_MP, value);
                break;
        }
    }

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect, DamageType.PHYSICAL);
    }
}
