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
package loginserver.network.aion.clientpackets;

import loginserver.controller.AccountController;
import loginserver.network.aion.AionClientPacket;
import loginserver.network.aion.AionConnection;

import java.nio.ByteBuffer;

/**
 * This packet is send when client was connected to game server and now is reconnection to login server.
 *
 * @author -Nemesiss-
 */
public class CM_UPDATE_SESSION extends AionClientPacket {
    /**
     * accountId is part of session key - its used for security purposes
     */
    private int accountId;
    /**
     * loginOk is part of session key - its used for security purposes
     */
    private int loginOk;
    /**
     * reconectKey is key that server sends to client for fast reconnection to login server - we will check if this key
     * is valid.
     */
    private int reconnectKey;

    /**
     * Constructs new instance of <tt>CM_UPDATE_SESSION </tt> packet.
     *
     * @param buf    packet data
     * @param client client
     */
    public CM_UPDATE_SESSION(ByteBuffer buf, AionConnection client) {
        super(buf, client, 0x08);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        accountId = readD();
        loginOk = readD();
        reconnectKey = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        AccountController.authReconnectingAccount(accountId, loginOk, reconnectKey, getConnection());
    }
}
