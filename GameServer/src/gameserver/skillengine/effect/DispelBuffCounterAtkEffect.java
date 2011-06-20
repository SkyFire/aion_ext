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

import gameserver.controllers.attack.AttackStatus;
import gameserver.controllers.attack.AttackUtil;
import gameserver.model.gameobjects.Creature;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.SkillTargetSlot;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author ViAl
 */
public class DispelBuffCounterAtkEffect extends DamageEffect {
    @XmlAttribute
    protected int count;

    private int i = 0;

    @Override
    public void applyEffect(Effect effect) {
        Creature effected = effect.getEffected();
        Creature effector = effect.getEffector();
        effected.getEffectController().removeEffectByTargetSlot(SkillTargetSlot.BUFF, i);
        effected.getController().onAttack(effector, effect.getSkillId(), TYPE.REGULAR, effect.getReserved1(), true);
    }

    @Override
    public void calculate(Effect effect) {
        i = 0;
        for (Effect ef : effect.getEffected().getEffectController().getAbnormalEffects()) {
            if (ef.getTargetSlot() == SkillTargetSlot.BUFF.ordinal() && ef.getTargetSlotLevel() == 0) {
                if (i == count)
                    break;
                i++;
            }
        }

        int newValue = 0;
        if (i == 1)
            newValue = value;
        else if (i > 1)
            newValue = value + ((value / 2) * (i - 1));

        int valueWithDelta = newValue + delta * effect.getSkillLevel();

        valueWithDelta = applyActionModifiers(effect, valueWithDelta);

        AttackUtil.calculateMagicalSkillAttackResult(effect, valueWithDelta, getElement());

        if (effect.getAttackStatus() != AttackStatus.RESIST)
            effect.addSucessEffect(this);
    }

}
