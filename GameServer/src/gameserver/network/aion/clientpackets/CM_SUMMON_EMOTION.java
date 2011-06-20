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

package gameserver.network.aion.clientpackets;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

/**
 * @author ATracer
 */
public class CM_SUMMON_EMOTION extends AionClientPacket {
    private static final Logger log = Logger.getLogger(CM_SUMMON_EMOTION.class);

    @SuppressWarnings("unused")
    private int objId;

    private int emotionTypeId;

    public CM_SUMMON_EMOTION(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        objId = readD();
        emotionTypeId = readC();
    }

    @Override
    protected void runImpl() {
        EmotionType emotionType = EmotionType.getEmotionTypeById(emotionTypeId);

        // Unknown Summon Emotion Type
        if (emotionType == EmotionType.UNK)
            log.error("Unknown emotion type? 0x" + Integer.toHexString(emotionTypeId).toUpperCase());

        Player activePlayer = getConnection().getActivePlayer();
        if (activePlayer == null) return;

        Summon summon = activePlayer.getSummon();
        if (summon == null) return;

        switch (emotionType) {
            case FLY:
            case LAND:
                PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
                break;
            case ATTACKMODE: //start attacking
                summon.setState(CreatureState.WEAPON_EQUIPPED);
                PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
                break;
            case NEUTRALMODE: //stop attacking
                summon.unsetState(CreatureState.WEAPON_EQUIPPED);
                PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
                break;
        }
    }
}
