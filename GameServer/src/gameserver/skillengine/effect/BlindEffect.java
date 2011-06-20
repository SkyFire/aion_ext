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

import com.aionemu.commons.utils.Rnd;
import gameserver.controllers.attack.AttackStatus;
import gameserver.controllers.movement.AttackCalcObserver;
import gameserver.controllers.movement.AttackStatusObserver;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BlindEffect")
public class BlindEffect extends EffectTemplate {
    @XmlAttribute
    private int value;

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

   @Override
    public void calculate(Effect effect) {
        if (calculateEffectResistRate(effect, StatEnum.MAGICAL_RESIST) && calculateEffectResistRate(effect, StatEnum.BLIND_RESISTANCE))
            effect.addSucessEffect(this);
    }


    @Override
    public void startEffect(Effect effect) {
        AttackCalcObserver acObserver = new AttackStatusObserver(value, AttackStatus.DODGE) {

            @Override
            public boolean checkAttackerStatus(AttackStatus status) {
                return Rnd.get(0, value) <= value;
            }

        };
        effect.getEffected().getObserveController().addAttackCalcObserver(acObserver);
        effect.setAttackStatusObserver(acObserver, position);
        effect.getEffected().getEffectController().setAbnormal(EffectId.BLIND.getEffectId());
    }

    @Override
    public void endEffect(Effect effect) {
        effect.getEffected().getEffectController().unsetAbnormal(EffectId.BLIND.getEffectId());
        AttackCalcObserver acObserver = effect.getAttackStatusObserver(position);
        if (acObserver != null)
            effect.getEffected().getObserveController().removeAttackCalcObserver(acObserver);
    }

}
