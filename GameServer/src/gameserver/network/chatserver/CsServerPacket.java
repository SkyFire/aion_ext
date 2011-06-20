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
package gameserver.network.chatserver;

import com.aionemu.commons.network.packet.BaseServerPacket;

import java.nio.ByteBuffer;

/**
 * @author ATracer
 */
public abstract class CsServerPacket extends BaseServerPacket {
    /**
     * constructs new server packet with specified opcode.
     *
     * @param opcode packet id
     */
    protected CsServerPacket(int opcode) {
        super(opcode);
    }

    /**
     * Write this packet data for given connection, to given buffer.
     *
     * @param con
     * @param buf
     */
    public final void write(ChatServerConnection con, ByteBuffer buf) {
        buf.putShort((short) 0);
        writeImpl(con, buf);
        buf.flip();
        buf.putShort((short) buf.limit());
        buf.position(0);
    }

    /**
     * Write data that this packet represents to given byte buffer.
     *
     * @param con
     * @param buf
     */
    protected abstract void writeImpl(ChatServerConnection con, ByteBuffer buf);
}
