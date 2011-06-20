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
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.Skill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rama and Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChangeMpConsumptionEffect")
public class ChangeMpConsumptionEffect extends BufEffect {
    @XmlAttribute
    protected boolean percent;
    @XmlAttribute
    protected int value;

    @Override
    public void startEffect(final Effect effect) {
        super.startEffect(effect);

        ActionObserver observer = new ActionObserver(ObserverType.SKILLUSE) {

            @Override
            public void skilluse(Skill skill) {
                skill.setChangeMpConsumption(value);
            }
        };

        effect.getEffected().getObserveController().addObserver(observer);
        effect.setActionObserver(observer, position);
    }

    @Override
    public void endEffect(Effect effect) {
        super.endEffect(effect);
        ActionObserver observer = effect.getActionObserver(position);
        effect.getEffected().getObserveController().removeObserver(observer);
    }

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect);
    }
}
