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
package gameserver.skillengine.action;

import gameserver.model.gameobjects.Creature;
import gameserver.skillengine.model.Skill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HpUseAction")
public class HpUseAction extends Action {
    @XmlAttribute(required = true)
    protected int value;

    @XmlAttribute
    protected int delta;

    @Override
    public void act(Skill skill) {
        Creature effector = skill.getEffector();
        int valueWithDelta = value + delta * skill.getSkillLevel();

        effector.getLifeStats().reduceHp(valueWithDelta, null);
    }

}
