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
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.skillengine.model.Effect;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.stats.StatFunctions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.concurrent.Future;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BleedEffect")
public class BleedEffect extends EffectTemplate {
    @XmlAttribute(required = true)
    protected int checktime;
    @XmlAttribute
    protected int value;
    @XmlAttribute
    protected int delta;

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        if (calculateEffectResistRate(effect, StatEnum.BLEED_RESISTANCE))
            effect.addSucessEffect(this);
    }

    @Override
    public void endEffect(Effect effect) {
        Creature effected = effect.getEffected();
        effected.getEffectController().unsetAbnormal(EffectId.BLEED.getEffectId());
    }

    @Override
    public void onPeriodicAction(Effect effect) {
        Creature effected = effect.getEffected();
        Creature effector = effect.getEffector();
        int valueWithDelta = value + delta * effect.getSkillLevel();
        int damage = StatFunctions.calculateMagicDamageToTarget(effector, effected, valueWithDelta, getElement());
        effected.getController().onAttack(effector, effect.getSkillId(), TYPE.DAMAGE, damage, true);
    }

    @Override
    public void startEffect(final Effect effect) {
        final Creature effected = effect.getEffected();

        effect.setAbnormal(EffectId.BLEED.getEffectId());
        effected.getEffectController().setAbnormal(EffectId.BLEED.getEffectId());

        Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {

            @Override
            public void run() {
                onPeriodicAction(effect);
            }
        }, checktime, checktime);
        effect.setPeriodicTask(task, position);
    }

}
