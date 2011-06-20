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

package gameserver.network.aion.serverpackets;

import gameserver.model.account.Account;
import gameserver.model.account.PlayerAccountData;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.PlayerInfo;

import java.nio.ByteBuffer;

/**
 * In this packet Server is sending Character List to client.
 *
 * @author Nemesiss, AEJTester
 */
public class SM_CHARACTER_LIST extends PlayerInfo {
    /**
     * PlayOk2 - we dont care...
     */
    private final int playOk2;

    /**
     * Constructs new <tt>SM_CHARACTER_LIST </tt> packet
     */
    public SM_CHARACTER_LIST(int playOk2) {
        this.playOk2 = playOk2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, playOk2);

        Account account = con.getAccount();
        writeC(buf, account.size());// characters count

        for (PlayerAccountData playerData : account.getSortedAccountsList()) {
            writePlayerInfo(buf, playerData);
            writeB(buf, new byte[14]);
        }
    }
}
