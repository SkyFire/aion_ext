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
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.SpellStatus;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StumbleEffect")
public class StumbleEffect extends EffectTemplate {
    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        if (calculateEffectResistRate(effect, StatEnum.STUMBLE_RESISTANCE)) {
            effect.addSucessEffect(this);
            effect.setSpellStatus(SpellStatus.STUMBLE);
        }
    }

    @Override
    public void startEffect(Effect effect) {
        final Creature effected = effect.getEffected();
        effected.getController().cancelCurrentSkill();
        effect.setAbnormal(EffectId.STUMBLE.getEffectId());
        effected.getEffectController().setAbnormal(EffectId.STUMBLE.getEffectId());
        PacketSendUtility.broadcastPacketAndReceive(effected, new SM_FORCED_MOVE(effect.getEffector(), effected));
    }

    @Override
    public void endEffect(Effect effect) {
        effect.getEffected().getEffectController().unsetAbnormal(EffectId.STUMBLE.getEffectId());
    }
}
