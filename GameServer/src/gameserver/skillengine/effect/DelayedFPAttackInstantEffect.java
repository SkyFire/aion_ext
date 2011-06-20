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

import gameserver.model.gameobjects.player.Player;
import gameserver.skillengine.action.DamageType;
import gameserver.skillengine.model.Effect;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DelayedFPAttackInstantEffect")
public class DelayedFPAttackInstantEffect extends DamageEffect {
    @XmlAttribute
    protected int delay;
    @XmlAttribute
    protected boolean percent;

    @Override
    public void calculate(Effect effect) {
        if (!(effect.getEffected() instanceof Player))
            return;
        super.calculate(effect, DamageType.MAGICAL);
    }

    @Override
    public void applyEffect(final Effect effect) {
        final Player effected = (Player) effect.getEffected();
        int maxFP = effected.getLifeStats().getMaxFp();
        final int newValue = (percent) ? (int) ((maxFP * value) / 100) : value;

        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                effected.getLifeStats().reduceFp(newValue);
            }
        }, delay);
    }
}