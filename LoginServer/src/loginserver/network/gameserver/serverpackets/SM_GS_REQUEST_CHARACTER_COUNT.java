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
 * Packet sent to gameserver to request characters count for account
 *
 * @author xavier
 */
public class SM_GS_REQUEST_CHARACTER_COUNT extends GsServerPacket {
    private int accountId;

    public SM_GS_REQUEST_CHARACTER_COUNT(int accountId) {
        super(0x06);

        this.accountId = accountId;
    }

    @Override
    protected void writeImpl(GsConnection con, ByteBuffer buf) {
        writeC(buf, getOpcode());
        writeD(buf, accountId);
    }

}
