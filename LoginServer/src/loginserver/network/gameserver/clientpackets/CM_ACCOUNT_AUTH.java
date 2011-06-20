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

package loginserver.network.gameserver.clientpackets;

import loginserver.controller.AccountController;
import loginserver.network.aion.SessionKey;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;

import java.nio.ByteBuffer;

/**
 * In this packet Gameserver is asking if given account sessionKey is valid at Loginserver side. [if user that is
 * authenticating on Gameserver is already authenticated on Loginserver]
 *
 * @author -Nemesiss-
 */
public class CM_ACCOUNT_AUTH extends GsClientPacket {
    /**
     * SessionKey that GameServer needs to check if is valid at Loginserver side.
     */
    private SessionKey sessionKey;

    /**
     * Constructor.
     *
     * @param buf
     * @param client
     */
    public CM_ACCOUNT_AUTH(ByteBuffer buf, GsConnection client) {
        super(buf, client, 0x01);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        int accountId = readD();
        int loginOk = readD();
        int playOk1 = readD();
        int playOk2 = readD();

        sessionKey = new SessionKey(accountId, loginOk, playOk1, playOk2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        AccountController.checkAuth(sessionKey, getConnection());
    }
}
