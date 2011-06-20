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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_PLAYER_MOVE;
import gameserver.skillengine.action.DamageType;
import gameserver.skillengine.model.Effect;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sarynth
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MoveBehindEffect")
public class MoveBehindEffect extends DamageEffect {
    @Override
    public void applyEffect(Effect effect) {
        final Player effector = (Player) effect.getEffector();
        final Creature effected = effect.getEffected();

        // Move Effector to Effected
        double radian = Math.toRadians(MathUtil.convertHeadingToDegree(effected.getHeading()));
        float x1 = (float) (Math.cos(Math.PI + radian) * 1.3F);
        float y1 = (float) (Math.sin(Math.PI + radian) * 1.3F);
        World.getInstance().updatePosition(
                effector,
                effected.getX() + x1,
                effected.getY() + y1,
                effected.getZ() + 0.25F,
                effected.getHeading());

        PacketSendUtility.sendPacket(effector,
                new SM_PLAYER_MOVE(
                        effector.getX(),
                        effector.getY(),
                        effector.getZ(),
                        effector.getHeading()
                )
        );
        super.applyEffect(effect);
    }

    @Override
    public void calculate(Effect effect) {
        if (effect.getEffector() instanceof Player && effect.getEffected() != null) {
            super.calculate(effect, DamageType.PHYSICAL);
        }
    }

}
