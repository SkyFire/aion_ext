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
package gameserver.skillengine.effect.modifier;

import gameserver.skillengine.model.Effect;
import gameserver.utils.PositionUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FrontDamageModifier")
public class FrontDamageModifier
        extends ActionModifier {

    @XmlAttribute(required = true)
    protected int delta;
    @XmlAttribute(required = true)
    protected int value;

    @Override
    public int analyze(Effect effect, int originalValue) {
        return originalValue + value + effect.getSkillLevel() * delta;
    }

    @Override
    public boolean check(Effect effect) {
        return PositionUtil.isInFrontOfTarget(effect.getEffector(), effect.getEffected());
    }


}
