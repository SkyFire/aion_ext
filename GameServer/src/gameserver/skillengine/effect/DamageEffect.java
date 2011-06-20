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
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.skillengine.action.DamageType;
import gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DamageEffect")
public abstract class DamageEffect
        extends EffectTemplate {

    @XmlAttribute(required = true)
    protected int value;

    @XmlAttribute
    protected int delta;

    @Override
    public void applyEffect(Effect effect) {
        if (effect.getAttackStatus() == AttackStatus.RESIST || effect.getAttackStatus() == AttackStatus.DODGE)
            return;
        effect.getEffected().getController().onAttack(effect.getEffector(),
                effect.getSkillId(), TYPE.REGULAR, effect.getReserved1(), true);
        effect.getEffector().getObserveController().notifyAttackObservers(effect.getEffected());
    }

    public void calculate(Effect effect, DamageType damageType) {
        int skillLvl = effect.getSkillLevel();
        int valueWithDelta = value + delta * skillLvl;
        valueWithDelta = applyActionModifiers(effect, valueWithDelta);

        // apply pvp damage ratio
        if (effect.getEffected() instanceof Player && effect.getPvpDamage() != 0)
            valueWithDelta = Math.round(valueWithDelta * (effect.getPvpDamage() / 100f));

        switch (damageType) {
            case PHYSICAL:
                AttackUtil.calculatePhysicalSkillAttackResult(effect, valueWithDelta);
                break;
            case MAGICAL:
                AttackUtil.calculateMagicalSkillAttackResult(effect, valueWithDelta, getElement());
                break;
            default:
                AttackUtil.calculatePhysicalSkillAttackResult(effect, 0);
        }

        if (effect.getAttackStatus() != AttackStatus.RESIST && effect.getAttackStatus() != AttackStatus.DODGE || this instanceof MoveBehindEffect)
            effect.addSucessEffect(this);
    }

}
