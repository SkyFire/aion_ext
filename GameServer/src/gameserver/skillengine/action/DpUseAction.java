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

import gameserver.model.gameobjects.player.Player;
import gameserver.skillengine.model.Skill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 *         <p/>
 *         Effector: Player only
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DpUseAction")
public class DpUseAction extends Action {
    @XmlAttribute(required = true)
    protected int value;

    @Override
    public void act(Skill skill) {
        Player effector = (Player) skill.getEffector();
        int currentDp = effector.getCommonData().getDp();

        if (currentDp <= 0 || currentDp < value)
            return;

        effector.getCommonData().setDp(currentDp - value);
    }
}
