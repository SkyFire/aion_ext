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

import com.aionemu.commons.network.IPRange;
import loginserver.GameServerTable;
import loginserver.network.gameserver.GsAuthResponse;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.GsConnection.State;
import loginserver.network.gameserver.serverpackets.SM_GS_AUTH_RESPONSE;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * This is authentication packet that gs will send to login server for registration.
 *
 * @author -Nemesiss-
 */
public class CM_GS_AUTH extends GsClientPacket {
    /**
     * Password for authentication
     */
    private String password;

    /**
     * Id of GameServer
     */
    private byte gameServerId;

    /**
     * Maximum number of players that this Gameserver can accept.
     */
    private int maxPlayers;

    /**
     * Required access level to login
     */
    private int requiredAccess;

    /**
     * Port of this Gameserver.
     */
    private int port;

    /**
     * Default address for server
     */
    private byte[] defaultAddress;

    /**
     * List of IPRanges for this gameServer
     */
    private List<IPRange> ipRanges;

    /**
     * Constructor.
     *
     * @param buf
     * @param client
     */
    public CM_GS_AUTH(ByteBuffer buf, GsConnection client) {
        super(buf, client, 0x00);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        gameServerId = (byte) readC();

        defaultAddress = readB(readC());
        int size = readD();
        ipRanges = new ArrayList<IPRange>(size);
        for (int i = 0; i < size; i++) {
            ipRanges.add(new IPRange(readB(readC()), readB(readC()), readB(readC())));
        }

        port = readH();
        maxPlayers = readD();
        requiredAccess = readD();
        password = readS();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        GsConnection client = getConnection();

        GsAuthResponse resp = GameServerTable.registerGameServer(client, gameServerId, defaultAddress, ipRanges, port,
                maxPlayers, requiredAccess, password);

        switch (resp) {
            case AUTHED:
                getConnection().setState(State.AUTHED);
                sendPacket(new SM_GS_AUTH_RESPONSE(resp));
                break;

            default:
                client.close(new SM_GS_AUTH_RESPONSE(resp), true);
        }
    }
}
