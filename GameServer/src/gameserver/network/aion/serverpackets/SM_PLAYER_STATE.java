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


package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * So far I've found only one usage for this packet - to stop character blinking ( just after login into game, player's
 * character is blinking )
 *
 * @author Luno, Sweetkr
 *         <p/>
 *         states:
 *         0 - normal char
 *         1- crounched invisible char
 *         64 - standing blinking char
 *         128- char is invisible
 */
public class SM_PLAYER_STATE extends AionServerPacket {
    private int playerObjId;
    private int visualState;
    private int seeState;

    public SM_PLAYER_STATE(Player player) {
        this.playerObjId = player.getObjectId();
        this.visualState = player.getVisualState();
        this.seeState = player.getSeeState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, playerObjId);
        writeC(buf, visualState);
        writeC(buf, seeState);
        if (visualState == 64)
            writeC(buf, 0x01);
        else
            writeC(buf, 0x00);
    }
}
