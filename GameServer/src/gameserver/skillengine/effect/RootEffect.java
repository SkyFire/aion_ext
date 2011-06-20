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

import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.ActionObserver.ObserverType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.SM_TARGET_IMMOBILIZE;
import gameserver.skillengine.model.Effect;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RootEffect")
public class RootEffect extends EffectTemplate {
    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

   @Override
    public void calculate(Effect effect) {
        if (calculateEffectResistRate(effect, StatEnum.MAGICAL_RESIST) && calculateEffectResistRate(effect, StatEnum.ROOT_RESISTANCE))
            effect.addSucessEffect(this);
    }
    @Override
    public void startEffect(final Effect effect) {
        final Creature effected = effect.getEffected();
        effect.setAbnormal(EffectId.ROOT.getEffectId());
        effected.getEffectController().setAbnormal(EffectId.ROOT.getEffectId());
        PacketSendUtility.broadcastPacketAndReceive(effected, new SM_TARGET_IMMOBILIZE(effected));

        switch (effect.getSkillId()) {
            /*Temporary hack for unbreakable roots*/
            case 322: //Ankle Snare
			case 1973: //punishing wave I
		    case 227: //Tendon Slice I
		    case 228: //Tendon Slice II
			case 1974: //punishing wave II
            case 2008: //Flames of Anguish I
            case 8537:
                break;
            default:
                effected.getObserveController().attach(
                        new ActionObserver(ObserverType.ATTACKED) {
                            @Override
                            public void attacked(Creature creature) {
                                effected.getEffectController().removeEffect(effect.getSkillId());
                            }
                        }
                );
        }
    }

    @Override
    public void endEffect(Effect effect) {
        effect.getEffected().getEffectController().unsetAbnormal(EffectId.ROOT.getEffectId());
    }
}
