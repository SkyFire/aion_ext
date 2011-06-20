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
package gameserver.network.aion;

import com.aionemu.commons.network.packet.BaseServerPacket;
import gameserver.network.Crypt;

import java.nio.ByteBuffer;

/**
 * Base class for every GS -> Aion Server Packet.
 *
 * @author -Nemesiss-
 */
public abstract class AionServerPacket extends BaseServerPacket {
    /**
     * ByteBuffer that contains this packet data
     */
    private ByteBuffer buf;

    /**
     * Constructs new server packet
     */
    protected AionServerPacket() {
        super();
        setOpcode(ServerPacketsOpcodes.getOpcode(getClass()));
    }

    /**
     * Write packet opcodec and two additional bytes
     *
     * @param buf
     * @param value
     */
    private final void writeOP(ByteBuffer buf, int value) {
        /** obfuscate packet id */
        byte op = Crypt.encodeOpcodec(value);
        buf.put(op);

        /** put static server packet code */
        buf.put(Crypt.staticServerPacketCode);

        /** for checksum? */
        buf.put((byte) ~op);
    }

    public final void write(AionConnection con) {
        write(con, buf);
    }

    /**
     * Write and encrypt this packet data for given connection, to given buffer.
     *
     * @param con
     * @param buf
     */
    public final void write(AionConnection con, ByteBuffer buf) {
        buf.putShort((short) 0);
        writeOP(buf, getOpcode());
        writeImpl(con, buf);
        buf.flip();
        buf.putShort((short) buf.limit());
        ByteBuffer b = buf.slice();
        buf.position(0);
        con.encrypt(b);
    }

    /**
     * Write data that this packet represents to given byte buffer.
     *
     * @param con
     * @param buf
     */
    protected void writeImpl(AionConnection con, ByteBuffer buf) {

    }

    /**
     * @return the buf
     */
    public ByteBuffer getBuf() {
        return buf;
    }

    /**
     * @param buf the buf to set
     */
    public void setBuf(ByteBuffer buf)
	{
		this.buf = buf;
	}
}
