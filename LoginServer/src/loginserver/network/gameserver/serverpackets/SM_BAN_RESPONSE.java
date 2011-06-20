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

package loginserver.network.gameserver.serverpackets;

import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.GsServerPacket;

import java.nio.ByteBuffer;

/**
 * In this packet LoginServer is answering on GameServer ban request
 *
 * @author Watson
 */
public class SM_BAN_RESPONSE extends GsServerPacket {
    private final byte type;
    private final int accountId;
    private final String ip;
    private final int time;
    private final int adminObjId;
    private final boolean result;

    public SM_BAN_RESPONSE(byte type, int accountId, String ip, int time, int adminObjId, boolean result) {
        super(0x05);

        this.type = type;
        this.accountId = accountId;
        this.ip = ip;
        this.time = time;
        this.adminObjId = adminObjId;
        this.result = result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(GsConnection con, ByteBuffer buf) {
        writeC(buf, getOpcode());

        writeC(buf, type);
        writeD(buf, accountId);
        writeS(buf, ip);
        writeD(buf, time);
        writeD(buf, adminObjId);
        writeC(buf, result ? 1 : 0);
    }
}        
