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
package loginserver.network.aion.serverpackets;

import loginserver.GameServerInfo;
import loginserver.GameServerTable;
import loginserver.controller.AccountController;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.AionServerPacket;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;

/**
 * @author -Nemesiss-
 */
public class SM_SERVER_LIST extends AionServerPacket {
    /**
     * Logger for this class.
     */
    protected static Logger log = Logger.getLogger(SM_SERVER_LIST.class);

    /**
     * Constructs new instance of <tt>SM_SERVER_LIST</tt> packet.
     */
    public SM_SERVER_LIST() {
        super(0x04);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        Collection<GameServerInfo> servers = GameServerTable.getGameServers();
        Map<Integer, Integer> charactersCountOnServer = null;

        int accountId = con.getAccount().getId();
        int maxId = 0;
        int accessLvl;

        charactersCountOnServer = AccountController.getCharacterCountsFor(accountId);

        writeC(buf, getOpcode());
        writeC(buf, servers.size());// servers
        writeC(buf, con.getAccount().getLastServer());// last server
        for (GameServerInfo gsi : servers) {
            accessLvl = (int) (con.getAccount().getAccessLevel());
            if (gsi.getId() > maxId) {
                maxId = gsi.getId();
            }
            writeC(buf, gsi.getId());// server id
            writeB(buf, gsi.getIPAddressForPlayer(con.getIP())); // server IP
            writeD(buf, gsi.getPort());// port
            writeC(buf, 0x00); // age limit
            writeC(buf, 0x01);// pvp=1
            writeH(buf, gsi.getCurrentPlayers());// currentPlayers
            writeH(buf, gsi.canAccess(accessLvl) ? gsi.getMaxPlayers() : 0);// maxPlayers
            writeC(buf, gsi.canAccess(accessLvl) ? (gsi.isOnline() ? 1 : 0) : 0);// ServerStatus, up=1
            writeD(buf, gsi.canAccess(accessLvl) ? 1 : 0);// bits);
            writeC(buf, 0);// server.brackets ? 0x01 : 0x00);
        }

        writeH(buf, maxId + 1);
        writeC(buf, 0x01); // 0x01 for autoconnect

        for (int i = 1; i <= maxId; i++) {
            if (charactersCountOnServer.containsKey(i)) {
                writeC(buf, charactersCountOnServer.get(i));
            } else {
                writeC(buf, 0);
            }
        }
    }
}
