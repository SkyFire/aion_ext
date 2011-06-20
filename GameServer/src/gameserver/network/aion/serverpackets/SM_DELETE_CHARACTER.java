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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * In this packet Server is sending response for CM_DELETE_CHARACTER.
 *
 * @author -Nemesiss-
 */
public class SM_DELETE_CHARACTER extends AionServerPacket {
    private int playerObjId;
    private int deletionTime;

    /**
     * Constructs new <tt>SM_DELETE_CHARACTER </tt> packet
     */
    public SM_DELETE_CHARACTER(int playerObjId, int deletionTime) {
        this.playerObjId = playerObjId;
        this.deletionTime = deletionTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        if (playerObjId != 0) {
            writeD(buf, 0x00);// unk
            writeD(buf, playerObjId);
            writeD(buf, deletionTime);
        } else {
            writeD(buf, 0x10);// unk
            writeD(buf, 0x00);
            writeD(buf, 0x00);
        }
    }
}
