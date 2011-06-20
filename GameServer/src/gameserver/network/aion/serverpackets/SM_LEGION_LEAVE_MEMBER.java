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
 * @author Simple
 */
public class SM_LEGION_LEAVE_MEMBER extends AionServerPacket {
    private String name;
    private String name1;
    private int playerObjId;
    private int msgId;

    public SM_LEGION_LEAVE_MEMBER(int msgId, int playerObjId, String name) {
        this.msgId = msgId;
        this.playerObjId = playerObjId;
        this.name = name;
    }

    public SM_LEGION_LEAVE_MEMBER(int msgId, int playerObjId, String name, String name1) {
        this.msgId = msgId;
        this.playerObjId = playerObjId;
        this.name = name;
        this.name1 = name1;
    }

    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, playerObjId);
        writeC(buf, 0x00); // isMember ? 1 : 0
        writeD(buf, 0x00); // unix time for log off
        writeD(buf, msgId);
        writeS(buf, name);
        writeS(buf, name1);
    }
}