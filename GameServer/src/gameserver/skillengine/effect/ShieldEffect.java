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

import gameserver.controllers.movement.AttackCalcObserver;
import gameserver.controllers.movement.AttackShieldObserver;
import gameserver.model.gameobjects.Npc;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.SkillTargetRace;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShieldEffect")
public class ShieldEffect extends EffectTemplate {

    @XmlAttribute
    protected int hitdelta;
    @XmlAttribute
    protected int hitvalue;
    @XmlAttribute
    protected boolean percent;
    @XmlAttribute
    protected int delta;
    @XmlAttribute
    protected int value;
    @XmlAttribute(name = "cond_race")
    protected SkillTargetRace cond_race;

    @Override
    public void applyEffect(Effect effect) {
        if (cond_race == null || effect.getEffected() instanceof Npc && ((Npc) effect.getEffected()).getObjectTemplate().getRace().toString().equals(cond_race.toString()))
            effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        int skillLvl = effect.getSkillLevel();
        int valueWithDelta = value + delta * skillLvl;
        int hitValueWithDelta = hitvalue + hitdelta * skillLvl;
        effect.setReserved2(valueWithDelta);
        effect.setReserved3(hitValueWithDelta);

        effect.addSucessEffect(this);
    }

    @Override
    public void startEffect(final Effect effect) {
        AttackShieldObserver asObserver = new AttackShieldObserver(effect.getReserved3(),
                effect.getReserved2(), percent, effect);

        effect.getEffected().getObserveController().addAttackCalcObserver(asObserver);
        effect.setAttackShieldObserver(asObserver, position);
    }

    @Override
    public void endEffect(Effect effect) {
        AttackCalcObserver acObserver = effect.getAttackShieldObserver(position);
        if (acObserver != null)
            effect.getEffected().getObserveController().removeAttackCalcObserver(acObserver);
    }
}
