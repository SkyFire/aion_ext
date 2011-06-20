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

import gameserver.controllers.attack.AttackUtil;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.skillengine.model.Effect;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcAtkInstantEffect")
public class ProcAtkInstantEffect extends DamageEffect {
    @Override
    public void applyEffect(final Effect effect) {
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                effect.getEffected().getController().onAttack(effect.getEffector(), effect.getSkillId(), TYPE.DAMAGE, effect.getReserved1(), false);
            }
        }, 1000);
    }

    @Override
    public void calculate(Effect effect) {
        int valueWithDelta = value + delta * effect.getSkillLevel();
        AttackUtil.calculateMagicalSkillAttackResult(effect, valueWithDelta, getElement());
        effect.addSucessEffect(this);
    }
}
