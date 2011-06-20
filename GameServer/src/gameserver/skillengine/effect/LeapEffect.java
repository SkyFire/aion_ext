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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_PLAYER_MOVE;
import gameserver.skillengine.model.Effect;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

/**
 * @author Bio
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LeapEffect")
public class LeapEffect extends EffectTemplate
{
	@XmlAttribute(name = "distance")
	private float distance;
	@XmlAttribute(name = "direction")
	private float direction;
	@XmlAttribute(name = "up")
	private float up;

	
	@Override
	public void applyEffect(Effect effect)
	{
		final Player effector = (Player)effect.getEffector();
		
		// Move Effector backwards direction=1 or frontwards direction=0
		double radian = Math.toRadians(MathUtil.convertHeadingToDegree(effector.getHeading()));
		float x1 = (float)(Math.cos(Math.PI * direction + radian) * distance);
		float y1 = (float)(Math.sin(Math.PI * direction + radian) * distance);
		World.getInstance().updatePosition(
			effector,
			effector.getX() + x1,
			effector.getY() + y1,
			effector.getZ() + up,
			effector.getHeading());
		
		PacketSendUtility.sendPacket(effector,
			new SM_PLAYER_MOVE(
				effector.getX(),
				effector.getY(),
				effector.getZ(),
				effector.getHeading()
			)
		);
	}
	
	@Override
	public void calculate(Effect effect)
	{
		effect.addSucessEffect(this);
	}
}