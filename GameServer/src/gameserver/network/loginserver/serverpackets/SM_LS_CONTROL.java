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

import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;

import java.nio.ByteBuffer;

/**
 * @author Aionchs-Wylovech
 */
public class SM_LS_CONTROL extends LsServerPacket {
    private final String accountName;

    private final String adminName;

    private final String playerName;

    private final int param;

    private final int type;

    public SM_LS_CONTROL(String accountName, String playerName, String adminName, int param, int type) {

        super(0x05);
        this.accountName = accountName;
        this.param = param;
        this.playerName = playerName;
        this.adminName = adminName;
        this.type = type;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(LoginServerConnection con, ByteBuffer buf) {
        writeC(buf, getOpcode());
        writeC(buf, type);
        writeS(buf, adminName);
        writeS(buf, accountName);
        writeS(buf, playerName);
        writeC(buf, param);
    }
}
