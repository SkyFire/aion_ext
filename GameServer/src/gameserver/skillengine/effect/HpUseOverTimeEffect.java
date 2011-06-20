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
import gameserver.skillengine.model.Effect;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.concurrent.Future;

/**
 * @author ATracer, ZeroSignal
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HpUseOverTimeEffect")
public class HpUseOverTimeEffect extends EffectTemplate {
    @XmlAttribute(required = true)
    protected int checktime;
    @XmlAttribute
    protected int value;
    @XmlAttribute
    protected int delta;

    @Override
    public void applyEffect(final Effect effect) {
        Creature effected = effect.getEffected();
        final int requiredHp = value + (delta * effect.getSkillLevel());

        Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {

            @Override
            public void run() {
                onPeriodicAction(effect, requiredHp);
            }
        }, 0, checktime);
        effect.setHpUseTask(task);
    }

    public void onPeriodicAction(Effect effect, int value) {
        Creature effected = effect.getEffected();
        if (effected.getLifeStats().getCurrentHp() < value)
            effect.endEffect();

        effected.getLifeStats().reduceHp(value, effected);
    }

    @Override
    public void calculate(Effect effect) {
        Creature effected = effect.getEffected();

        effect.addSucessEffect(this);
    }

}
