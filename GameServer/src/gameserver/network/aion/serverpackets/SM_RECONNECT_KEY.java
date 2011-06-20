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
 * Response for CM_RECONNECT_AUTH with key that will be use for authentication at LoginServer.
 *
 * @author -Nemesiss-
 */
public class SM_RECONNECT_KEY extends AionServerPacket {
    /**
     * key for reconnection - will be used for authentication
     */
    private final int key;

    /**
     * Constructs new <tt>SM_RECONNECT_KEY</tt> packet
     *
     * @param key key for reconnection
     */
    public SM_RECONNECT_KEY(int key) {
        this.key = key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, 0x00);
        writeD(buf, key);
	}
}
