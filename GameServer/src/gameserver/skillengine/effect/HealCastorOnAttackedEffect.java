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
import gameserver.model.gameobjects.player.Player;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.HealType;
import gameserver.utils.MathUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealCastorOnAttackedEffect")
public class HealCastorOnAttackedEffect extends EffectTemplate {
    @XmlAttribute
    protected int value;
    @XmlAttribute
    protected int delta;
    @XmlAttribute
    protected HealType type;

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        // The skill can be used only by a player
        if (!(effect.getEffector() instanceof Player))
            return;
        effect.addSucessEffect(this);
    }

    @Override
    public void startEffect(final Effect effect) {
        super.startEffect(effect);

        final Player player = (Player) effect.getEffector();
        final int valueWithDelta = value + delta * effect.getSkillLevel();

        ActionObserver observer = new ActionObserver(ObserverType.ATTACKED) {

            @Override
            public void attacked(Creature creature) {
                if (player.getPlayerGroup() != null) {
                    for (Player p : player.getPlayerGroup().getMembers()) {
                        if (MathUtil.isIn3dRange(effect.getEffected(), p, 5.0f))
                            p.getController().onRestore(type, valueWithDelta);
                    }
                }
                //TODO alliance
                else {
                    if (MathUtil.isIn3dRange(effect.getEffected(), player, 5.0f))
                        player.getController().onRestore(type, valueWithDelta);
                }
            }
        };

        effect.getEffected().getObserveController().addObserver(observer);
        effect.setActionObserver(observer, position);
    }

    @Override
    public void endEffect(Effect effect) {
        super.endEffect(effect);
        ActionObserver observer = effect.getActionObserver(position);
        effect.getEffected().getObserveController().removeObserver(observer);
    }
}