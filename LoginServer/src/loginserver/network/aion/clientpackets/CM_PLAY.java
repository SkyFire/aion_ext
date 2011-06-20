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

import loginserver.GameServerInfo;
import loginserver.GameServerTable;
import loginserver.network.aion.AionAuthResponse;
import loginserver.network.aion.AionClientPacket;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.SessionKey;
import loginserver.network.aion.serverpackets.SM_LOGIN_FAIL;
import loginserver.network.aion.serverpackets.SM_PLAY_FAIL;
import loginserver.network.aion.serverpackets.SM_PLAY_OK;

import java.nio.ByteBuffer;

/**
 * @author -Nemesiss-
 */
public class CM_PLAY extends AionClientPacket {
    /**
     * accountId is part of session key - its used for security purposes
     */
    private int accountId;
    /**
     * loginOk is part of session key - its used for security purposes
     */
    private int loginOk;
    /**
     * Id of game server that this client is trying to play on.
     */
    private byte servId;

    /**
     * Constructs new instance of <tt>SM_PLAY_FAIL</tt> packet.
     *
     * @param buf    packet data
     * @param client client
     */
    public CM_PLAY(ByteBuffer buf, AionConnection client) {
        super(buf, client, 0x02);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        accountId = readD();
        loginOk = readD();
        servId = (byte) readC();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        AionConnection con = getConnection();
        SessionKey key = con.getSessionKey();

        if (key.checkLogin(accountId, loginOk)) {
            GameServerInfo gsi = GameServerTable.getGameServerInfo(servId);
            if (gsi == null || !gsi.isOnline())
                con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.SERVER_DOWN));
                // else if(serv gm only)
                // con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.GM_ONLY));
            else if (gsi.isFull())
                con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.SERVER_FULL));
            else {
                con.setJoinedGs();
                sendPacket(new SM_PLAY_OK(key, servId));
            }
        } else
            con.close(new SM_LOGIN_FAIL(AionAuthResponse.SYSTEM_ERROR), true);
    }
}
