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
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_WINDSTREAM;
import gameserver.utils.PacketSendUtility;

public class CM_WINDSTREAM extends AionClientPacket {
    int teleportId;
    int distance;
    int validatePos;
    int unk;

    /**
     * Constructs new instance of <tt>CM_WINDSTREAM </tt> packet
     *
     * @param opcode
     * @author Vyaslav, Ares/Kaipo
     */
    public CM_WINDSTREAM(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        teleportId = readD();
        distance = readD();
        validatePos = readH();
        unk = readH();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        AionConnection client = getConnection();
        Player player = client.getActivePlayer();
        EmotionType emotionType;

        if (player != null) {
            if (validatePos == SM_WINDSTREAM.C_VALIDATED) {
                emotionType = EmotionType.START_WINDSTREAM;
                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.JUMP, teleportId, distance, true)/**/);
                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, emotionType, teleportId, distance), /**/true);
            } else if (validatePos == SM_WINDSTREAM.C_START_BOOST) {
                emotionType = EmotionType.BOOST_WINDSTREAM;
                client.sendPacket(new SM_EMOTION(player, emotionType, teleportId, distance));
            }
            client.sendPacket(new SM_WINDSTREAM(validatePos));
        }
    }
}
