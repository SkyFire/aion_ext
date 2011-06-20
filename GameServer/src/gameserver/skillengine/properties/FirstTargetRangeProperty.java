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
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.skillengine.model.Skill;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FirstTargetRangeProperty")
public class FirstTargetRangeProperty extends Property {
    /**
     * Logger
     */
    private static final Logger log = Logger.getLogger(FirstTargetRangeProperty.class);

    @XmlAttribute(required = true)
    protected int value;

    @Override
    public boolean set(Skill skill) {
        if (!skill.isFirstTargetRangeCheck())
            return true;

        Creature effector = skill.getEffector();
        Creature firstTarget = skill.getFirstTarget();
        if (firstTarget == null)
            return false;

        if (firstTarget.getPosition().getMapId() == 0)
            log.warn("FirstTarget has mapId of 0. (" + firstTarget.getName() + ")");

        float distance = (float) value;

        //addweaponrange
        if (skill.getSkillTemplate().getInitproperties() != null) {
            for (Property prop : skill.getSkillTemplate().getInitproperties().getProperties()) {
                if (prop instanceof AddWeaponRangeProperty)
                    distance += (float) skill.getEffector().getGameStats().getCurrentStat(StatEnum.ATTACK_RANGE) / 1000;
            }
        }
        //tolerance
        distance += 1.5;

        //testing new firsttargetrangeproperty
        if ((float) (MathUtil.getDistance(effector, firstTarget)) <= distance) {
            return true;
        } else {
            if (effector instanceof Player) {
                PacketSendUtility.sendPacket((Player) effector, SM_SYSTEM_MESSAGE.STR_ATTACK_TOO_FAR_FROM_TARGET());
            }
            return false;
        }
    }

    public int getValue() {
        return value;
    }

}
