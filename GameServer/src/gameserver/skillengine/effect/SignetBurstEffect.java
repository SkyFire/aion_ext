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
import gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SignetBurstEffect")
public class SignetBurstEffect extends DamageEffect {
    @XmlAttribute
    protected int signetlvl;
    @XmlAttribute
    protected String signet;

    @Override
    public void calculate(Effect effect) {
        Creature effected = effect.getEffected();
        Effect signetEffect = effected.getEffectController().getAnormalEffect(signet);
        if (signetEffect == null)
            return;

        int level = signetEffect.getSkillLevel();
        int valueWithDelta = value + delta * effect.getSkillLevel();
        int finalDamage = valueWithDelta * level / 5;

        if(level <3){
            effect.setReserved1(finalDamage);
            signetEffect.endEffect();
             }
        else{
            effect.setReserved1(finalDamage);
            effect.addSucessEffect(this);
            signetEffect.endEffect();
            }
        }

    @Override
    public void startEffect(Effect effect) {
        if (effect.getSkillId() == 833 || effect.getSkillId() == 834 || effect.getSkillId() == 835) {
            effect.getEffected().getController().cancelCurrentSkill();
            effect.getEffected().getEffectController().setAbnormal(EffectId.STUN.getEffectId());
        }
    }

    @Override
    public void endEffect(Effect effect) {
        if (effect.getSkillId() == 833 || effect.getSkillId() == 834 || effect.getSkillId() == 835) {
            effect.getEffected().getEffectController().unsetAbnormal(EffectId.STUN.getEffectId());
        }
    }

}
