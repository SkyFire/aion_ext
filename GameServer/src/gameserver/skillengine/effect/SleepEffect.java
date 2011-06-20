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
import gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SleepEffect")
public class SleepEffect extends EffectTemplate {
    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

     @Override
    public void calculate(Effect effect) {
        if (calculateEffectResistRate(effect, StatEnum.MAGICAL_RESIST) && calculateEffectResistRate(effect, StatEnum.SLEEP_RESISTANCE))
            effect.addSucessEffect(this);
    }

    @Override
    public void startEffect(final Effect effect) {
        final Creature effected = effect.getEffected();
        effected.getController().cancelCurrentSkill();
        effect.setAbnormal(EffectId.SLEEP.getEffectId());
        effected.getEffectController().setAbnormal(EffectId.SLEEP.getEffectId());

        effected.getObserveController().attach(
                new ActionObserver(ObserverType.ATTACKED) {
                    @Override
                    public void attacked(Creature creature) {
                        effected.getEffectController().removeEffect(effect.getSkillId());
                    }
                }
        );
        effected.getObserveController().attach(
                new ActionObserver(ObserverType.DOT) {
                    @Override
                    public void onDot(Creature creature) {
                        effected.getEffectController().removeEffect(effect.getSkillId());
                    }
                }
        );
    }

    @Override
    public void endEffect(Effect effect) {
        effect.getEffected().getEffectController().unsetAbnormal(EffectId.SLEEP.getEffectId());
    }
}
