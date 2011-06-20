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
 * In this packet Server is sending response for CM_RESTORE_CHARACTER.
 *
 * @author -Nemesiss-
 */
public class SM_RESTORE_CHARACTER extends AionServerPacket {
    /**
     * Character object id.
     */
    private final int chaOid;
    /**
     * True if player was restored.
     */
    private final boolean success;

    /**
     * Constructs new <tt>SM_RESTORE_CHARACTER </tt> packet
     */
    public SM_RESTORE_CHARACTER(int chaOid, boolean success) {
        this.chaOid = chaOid;
        this.success = success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, success ? 0x00 : 0x10);// unk
        writeD(buf, chaOid);
    }
}
