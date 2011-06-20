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
package gameserver.skillengine.condition;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.skillengine.model.Skill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetCondition")
public class TargetCondition
        extends Condition {

    @XmlAttribute(required = true)
    protected TargetAttribute value;

    /**
     * Gets the value of the value property.
     *
     * @return possible object is
     *         {@link TargetAttribute }
     */
    public TargetAttribute getValue() {
        return value;
    }

    @Override
    public boolean verify(Skill skill) {
        if (value != TargetAttribute.NONE && skill.getFirstTarget() == null) {
            return false;
        }
        switch (value) {
            case NPC:
                return skill.getFirstTarget() instanceof Npc;
            case PC:
                return skill.getFirstTarget() instanceof Player;
            default:
                return false;
        }
    }
}
