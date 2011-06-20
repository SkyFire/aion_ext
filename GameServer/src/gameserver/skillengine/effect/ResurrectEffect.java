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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_RESURRECT;
import gameserver.services.TeleportService;
import gameserver.skillengine.model.Effect;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResurrectEffect")
public class ResurrectEffect extends EffectTemplate {
	@XmlAttribute
	protected String teleport = "";
	
    @Override
    public void applyEffect(Effect effect) {
        Player effector = (Player) effect.getEffector();
        Player effected = (Player) effect.getEffected();
        
        if(!(effected instanceof Player) || !effected.getLifeStats().isAlreadyDead())
        	return;
        
        if(teleport.equals("self")) {
        	effected.getReviveController().setTeleportTarget(effector);
        	effected.getReviveController().setToBeTeleported(true);
        }
        
        PacketSendUtility.sendPacket(effected, new SM_RESURRECT(effector, effect.getSkillId()));
    }

    @Override
    public void calculate(Effect effect) {
        effect.addSucessEffect(this);
    }
}
