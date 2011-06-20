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
 * Sends Survey HTML data to the client.
 * This packet can be splitted over max 255 packets
 * The max length of the HTML may therefore be 255 * 65525 byte
 *
 * @author lhw, Kaipo and ginho1
 */
public class SM_QUESTIONNAIRE extends AionServerPacket {
    private int messageId;
    private byte chunk;
    private byte count;
    private String html;

    public SM_QUESTIONNAIRE(int messageId, byte chunk, byte count, String html) {
        this.messageId = messageId;
        this.chunk = chunk;
        this.count = count;
        this.html = html;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, messageId);
        writeC(buf, chunk);
        writeC(buf, count);
        writeH(buf, html.length() * 2);
        writeS(buf, html);
    }
}
