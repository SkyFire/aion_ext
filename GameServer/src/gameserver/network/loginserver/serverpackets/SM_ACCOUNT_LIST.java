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

package gameserver.network.loginserver.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * GameServer packet that sends list of logged in accounts
 *
 * @author SoulKeeper
 */
public class SM_ACCOUNT_LIST extends LsServerPacket {

    /**
     * Map with loaded accounts
     */
    private final Map<Integer, AionConnection> accounts;

    /**
     * constructs new server packet with specified opcode.
     */
    public SM_ACCOUNT_LIST(Map<Integer, AionConnection> accounts) {
        super(0x04);
        this.accounts = accounts;
    }

    @Override
    protected void writeImpl(LoginServerConnection con, ByteBuffer buf) {
        writeC(buf, getOpcode());
        writeD(buf, accounts.size());
        for (AionConnection ac : accounts.values()) {
            writeS(buf, ac.getAccount().getName());
        }
    }
}
