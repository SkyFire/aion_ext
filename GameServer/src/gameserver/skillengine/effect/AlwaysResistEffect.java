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

import gameserver.controllers.attack.AttackStatus;
import gameserver.controllers.movement.AttackCalcObserver;
import gameserver.controllers.movement.AttackStatusObserver;
import gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author ATracer
 */
public class AlwaysResistEffect extends EffectTemplate {
    @XmlAttribute
    protected int value;

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        effect.addSucessEffect(this);
    }

    @Override
    public void startEffect(final Effect effect) {
        AttackCalcObserver acObserver = new AttackStatusObserver(value, AttackStatus.RESIST) {

            @Override
            public boolean checkStatus(AttackStatus status) {
                if (status == AttackStatus.RESIST && value > 0) {
                    value -= 1;

                    return true;
                } else if (status == AttackStatus.RESIST && value == 0) {
                    effect.endEffect();
                }
                return false;
            }

        };
        effect.getEffected().getObserveController().addAttackCalcObserver(acObserver);
        effect.setAttackStatusObserver(acObserver, position);
    }

    @Override
    public void endEffect(Effect effect) {
        AttackCalcObserver acObserver = effect.getAttackStatusObserver(position);
        if (acObserver != null)
            effect.getEffected().getObserveController().removeAttackCalcObserver(acObserver);
    }
}
