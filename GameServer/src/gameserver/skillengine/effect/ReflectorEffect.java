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
import gameserver.skillengine.model.Effect;
import gameserver.utils.MathUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ginho1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReflectorEffect")
public class ReflectorEffect extends EffectTemplate {
    @XmlAttribute
    protected int value;
    @XmlAttribute
    protected int delta;
    @XmlAttribute
    protected int radius;

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
        ActionObserver oldObserver = effect.getActionObserver(position);
        if (oldObserver != null)
            return;

        final Creature effected = effect.getEffected();
        ActionObserver observer = null;

        observer = new ActionObserver(ObserverType.ATTACKED) {
            @Override
            public void attacked(Creature creature) {
                //TODO: correct reflect server message
                if (MathUtil.isInRange(creature, effected, radius))
                    creature.getController().onAttack(creature, (value + delta), false);
            }
        };

        effect.setActionObserver(observer, position);
        effected.getObserveController().addObserver(observer);
    }

    @Override
    public void endEffect(Effect effect) {
        ActionObserver observer = effect.getActionObserver(position);
        if (observer != null)
            effect.getEffected().getObserveController().removeObserver(observer);
    }
}
