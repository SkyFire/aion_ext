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
import gameserver.model.gameobjects.stats.CreatureLifeStats;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.Skill;
import gameserver.skillengine.model.SkillType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ViAl
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MagicCounterAtkEffect")
public class MagicCounterAtkEffect extends EffectTemplate {
    @XmlAttribute
    protected int percent;
    @XmlAttribute
    protected int maxdmg;

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
    public void startEffect(final Effect effect) {
        final Creature effector = effect.getEffector();
        final Creature effected = effect.getEffected();
        final CreatureLifeStats<? extends Creature> cls = effect.getEffected().getLifeStats();
        ActionObserver observer = null;

        observer = new ActionObserver(ObserverType.SKILLUSE) {
            @Override
            public void skilluse(Skill skill) {
                if (skill.getSkillTemplate().getType() == SkillType.MAGICAL) {
                    if (cls.getMaxHp() / 100 * percent <= maxdmg)
                        effected.getController().onAttack(effector, effect.getSkillId(), TYPE.DAMAGE, cls.getMaxHp() / 100 * percent, true);
                    else
                        effected.getController().onAttack(effector, maxdmg, true);
                }

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
