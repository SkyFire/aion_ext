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
import gameserver.model.gameobjects.state.CreatureVisualState;
import gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.Skill;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sweetkr
 * @author Cura
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HideEffect")
public class HideEffect extends BufEffect {
    @XmlAttribute
    protected int value;

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        //TODO calc probability
        effect.addSucessEffect(this);
    }

    @Override
    public void endEffect(Effect effect) {
        super.endEffect(effect);

        Creature effected = effect.getEffected();
        effected.getEffectController().unsetAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());

        CreatureVisualState visualState;

        switch (value) {
            case 1:
                visualState = CreatureVisualState.HIDE1;
                break;
            case 2:
                visualState = CreatureVisualState.HIDE2;
                break;
            case 3:
                visualState = CreatureVisualState.HIDE3;
                break;
            case 10:
                visualState = CreatureVisualState.HIDE10;
                break;
            case 13:
                visualState = CreatureVisualState.HIDE13;
                break;
            case 20:
                visualState = CreatureVisualState.HIDE20;
                break;
            default:
                visualState = CreatureVisualState.VISIBLE;
                break;
        }
        effected.unsetVisualState(visualState);

        if (effected instanceof Player) {
            PacketSendUtility.broadcastPacket((Player) effected, new SM_PLAYER_STATE((Player) effected), true);
        }
    }

    @Override
    public void startEffect(final Effect effect) {
        super.startEffect(effect);

        final Creature effected = effect.getEffected();
        effect.setAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());
        effected.getEffectController().setAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());

        CreatureVisualState visualState;

        switch (value) {
            case 1:
                visualState = CreatureVisualState.HIDE1;
                break;
            case 2:
                visualState = CreatureVisualState.HIDE2;
                break;
            case 3:
                visualState = CreatureVisualState.HIDE3;
                break;
            case 10:
                visualState = CreatureVisualState.HIDE10;
                break;
            case 13:
                visualState = CreatureVisualState.HIDE13;
                break;
            case 20:
                visualState = CreatureVisualState.HIDE20;
                break;
            default:
                visualState = CreatureVisualState.VISIBLE;
                break;
        }
        effected.setVisualState(visualState);

        if (effected instanceof Player) {
            PacketSendUtility.broadcastPacket((Player) effected, new SM_PLAYER_STATE((Player) effected), true);
        }

        //Remove Hide when use skill
        effected.getObserveController().attach(
                new ActionObserver(ObserverType.SKILLUSE) {
                    @Override
                    public void skilluse(Skill skill) {
                        effected.getEffectController().removeEffect(effect.getSkillId());
                    }
                }
        );

        // Remove Hide when attacked
        effected.getObserveController().attach(
                new ActionObserver(ObserverType.ATTACKED) {
                    @Override
                    public void attacked(Creature creature) {
                        effected.getEffectController().removeEffect(effect.getSkillId());
                    }
                }
        );

        // Remove Hide when attacking
        effected.getObserveController().attach(
                new ActionObserver(ObserverType.ATTACK) {
                    @Override
                    public void attack(Creature creature) {
                        effected.getEffectController().removeEffect(effect.getSkillId());
                    }
                }
        );
    }
}
