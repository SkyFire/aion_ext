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
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureSeeState;
import gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import gameserver.skillengine.model.Effect;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sweetkr
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchEffect")
public class SearchEffect extends EffectTemplate {
    @XmlAttribute
    protected int value;

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        effect.addSucessEffect(this);
    }

    @Override
    public void endEffect(Effect effect) {
        Creature effected = effect.getEffected();

        CreatureSeeState seeState;

        switch (value) {
            case 1:
                seeState = CreatureSeeState.SEARCH1;
                break;
            case 2:
                seeState = CreatureSeeState.SEARCH2;
                break;
            default:
                seeState = CreatureSeeState.NORMAL;
                break;
        }
        effected.unsetSeeState(seeState);

        if (effected instanceof Player) {
            PacketSendUtility.broadcastPacket((Player) effected, new SM_PLAYER_STATE((Player) effected), true);
        }
    }

    @Override
    public void startEffect(final Effect effect) {
        final Creature effected = effect.getEffected();

        CreatureSeeState seeState;

        switch (value) {
            case 1:
                seeState = CreatureSeeState.SEARCH1;
                break;
            case 2:
                seeState = CreatureSeeState.SEARCH2;
                break;
            default:
                seeState = CreatureSeeState.NORMAL;
                break;
        }
        effected.setSeeState(seeState);

        if (effected instanceof Player) {
            PacketSendUtility.broadcastPacket((Player) effected, new SM_PLAYER_STATE((Player) effected), true);
        }
    }
}
