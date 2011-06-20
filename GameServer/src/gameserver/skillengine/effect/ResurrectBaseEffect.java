/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.skillengine.effect;

import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.ActionObserver.ObserverType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author xavier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResurrectBaseEffect")
public class ResurrectBaseEffect extends BufEffect {
    @Override
    public void calculate(Effect effect) {
        super.calculate(effect);
    }

    @Override
    public void applyEffect(final Effect effect) {
        super.applyEffect(effect);

        Creature effected = effect.getEffected();

        if (effected instanceof Player) {
            ActionObserver observer = new ActionObserver(ObserverType.DEATH) {
                @Override
                public void died(Creature creature) {
                    if (creature instanceof Player) {
                        ((Player) creature).getReviveController().kiskRevive();
                    }
                }
            };
            effect.getEffected().getObserveController().attach(observer);
            effect.setActionObserver(observer, position);
        }
    }

    @Override
    public void endEffect(Effect effect) {
        super.endEffect(effect);

        if (!effect.getEffected().getLifeStats().isAlreadyDead() && effect.getActionObserver(position) != null) {
            effect.getEffected().getObserveController().removeDeathObserver(effect.getActionObserver(position));
        }
    }
}
