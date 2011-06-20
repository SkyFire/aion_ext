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

package gameserver.skillengine.properties;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.GroupService;
import gameserver.skillengine.model.Skill;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FirstTargetProperty")
public class FirstTargetProperty
        extends Property {
	private int visibleDistance = 95;
    @XmlAttribute(required = true)
    protected FirstTargetAttribute value;

    /**
     * Gets the value of the value property.
     *
     * @return possible object is
     *         {@link FirstTargetAttribute }
     */
    public FirstTargetAttribute getValue() {
        return value;
    }

    @Override
    public boolean set(Skill skill) {
        skill.setFirstTargetProperty(value);
        switch (value) {
            case ME:
                skill.setFirstTarget(skill.getEffector());
                break;
            case TARGETORME:
                if (skill.getFirstTarget() == null)
                    skill.setFirstTarget(skill.getEffector());
                break;
            case TARGETNOTME:
                if (skill.getFirstTarget() == null)
                    return false;
                else if (skill.getEffector() instanceof Player && skill.getFirstTarget() == skill.getEffector()) {
                    PacketSendUtility.sendPacket((Player) skill.getEffector(), SM_SYSTEM_MESSAGE.INVALID_TARGET());
                    return false;
                }
                break;
            case TARGET:
                if (skill.getFirstTarget() == null)
                    return false;
                break;
            case MYPET:
                Creature effector = skill.getEffector();
                if (effector instanceof Player) {
                    Summon summon = ((Player) effector).getSummon();
                    if (summon != null)
                        skill.setFirstTarget(summon);
                    else
                        return false;
                } else {
                    return false;
                }
                break;
            case PASSIVE:
                skill.setFirstTarget(skill.getEffector());
                break;
			case TARGET_MYPARTY_NONVISIBLE:
				Creature effected = skill.getFirstTarget();
				if(effected == null || MathUtil.isIn3dRange( skill.getEffector(), effected, visibleDistance) || !GroupService.getInstance().isGroupMember(effected.getObjectId()))
					return false;
				skill.setFirstTargetRangeCheck(false);
				break;
            case POINT:
                // TODO: Implement Range Check for Point
                skill.setFirstTargetRangeCheck(false);
                return true;
        }

        if (skill.getFirstTarget() != null)
            skill.getEffectedList().add(skill.getFirstTarget());
        return true;
    }
}
