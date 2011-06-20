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

import loginserver.GameServerInfo;
import loginserver.controller.AccountController;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;

import java.nio.ByteBuffer;

/**
 * @author xavier
 *         <p/>
 *         Packet sent to game server to request account characters count
 *         When all characters count have been received, send server list to client
 */
public class CM_GS_CHARACTER_COUNT extends GsClientPacket {
    private int accountId;
    private int characterCount;

    /**
     * @param buf
     * @param client
     */
    public CM_GS_CHARACTER_COUNT(ByteBuffer buf, GsConnection client) {
        super(buf, client, 0x07);
    }

    @Override
    protected void readImpl() {
        accountId = readD();
        characterCount = readC();
    }

    @Override
    protected void runImpl() {
        GameServerInfo gsi = getConnection().getGameServerInfo();

        AccountController.addCharacterCountFor(accountId, gsi.getId(), characterCount);

        if (AccountController.hasAllCharacterCounts(accountId)) {
            AccountController.sendServerListFor(accountId);
        }
    }
}
