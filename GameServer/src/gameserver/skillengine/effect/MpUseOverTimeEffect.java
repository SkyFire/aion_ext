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
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MpUseOverTimeEffect")
public class MpUseOverTimeEffect extends EffectTemplate {
    @XmlAttribute(required = true)
    protected int checktime;
    @XmlAttribute
    protected int value;

    @Override
    public void applyEffect(final Effect effect) {
        Creature effected = effect.getEffected();
        int maxMp = effected.getGameStats().getCurrentStat(StatEnum.MAXMP);
        final int requiredMp = maxMp * value / 100;

        Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {

            @Override
            public void run() {
                onPeriodicAction(effect, requiredMp);
            }
        }, 0, checktime);
        effect.setMpUseTask(task);
    }

    public void onPeriodicAction(Effect effect, int value) {
        Creature effected = effect.getEffected();
        if (effected.getLifeStats().getCurrentMp() < value)
            effect.endEffect();

        effected.getLifeStats().reduceMp(value);
    }

    @Override
    public void calculate(Effect effect) {
        Creature effected = effect.getEffected();
        int maxMp = effected.getGameStats().getCurrentStat(StatEnum.MAXMP);
        int requiredMp = maxMp * value / 100;
        if (effected.getLifeStats().getCurrentMp() < requiredMp)
            return;

        effect.addSucessEffect(this);
    }

}
