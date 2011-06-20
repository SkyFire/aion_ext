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
import gameserver.skillengine.model.SkillType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SilenceEffect")
public class SilenceEffect extends EffectTemplate {
    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        if (calculateEffectResistRate(effect, StatEnum.MAGICAL_RESIST) && calculateEffectResistRate(effect, StatEnum.SILENCE_RESISTANCE))
            effect.addSucessEffect(this);
    }

    @Override
    public void startEffect(Effect effect) {
        final Creature effected = effect.getEffected();
        effect.setAbnormal(EffectId.SILENCE.getEffectId());
        effected.getEffectController().setAbnormal(EffectId.SILENCE.getEffectId());
        if (effected.getCastingSkill() != null && effected.getCastingSkill().getSkillTemplate().getType() == SkillType.MAGICAL)
            effected.getController().cancelCurrentSkill();
    }

    @Override
    public void endEffect(Effect effect) {
        effect.getEffected().getEffectController().unsetAbnormal(EffectId.SILENCE.getEffectId());
    }
}
