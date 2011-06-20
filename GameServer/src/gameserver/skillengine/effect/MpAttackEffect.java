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

import gameserver.skillengine.model.Effect;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.concurrent.Future;

/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MpAttackEffect")
public class MpAttackEffect extends DamageEffect {
    @XmlAttribute(required = true)
    protected int checktime;

    @XmlAttribute
    protected boolean percent;

    @Override
    public void calculate(Effect effect) {
        if (calculateEffectResistRate(effect, null))
            effect.addSucessEffect(this);
    }

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void endEffect(Effect effect) {

    }

    @Override
    public void onPeriodicAction(Effect effect) {
        int maxMP = effect.getEffected().getLifeStats().getMaxMp();
        int newValue = value;
        // Support for values in percentage
        if (percent)
            newValue = (int) ((maxMP * value) / 100);
        effect.getEffected().getLifeStats().reduceMp(newValue);
    }

    @Override
    public void startEffect(final Effect effect) {
        Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {

            @Override
            public void run() {
                onPeriodicAction(effect);
            }
        }, checktime, checktime);
        effect.setPeriodicTask(task, position);
    }
}