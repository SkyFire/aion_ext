/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.loginserver.serverpackets;

import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;

import java.nio.ByteBuffer;

/**
 * @author xavier
 */
public class SM_LS_CHARACTER_COUNT extends LsServerPacket {
    private int accountId;
    private int characterCount;

    public SM_LS_CHARACTER_COUNT(int accountId, int characterCount) {
        super(0x07);

        this.accountId = accountId;
        this.characterCount = characterCount;
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.network.loginserver.LsServerPacket#writeImpl(com.aionemu.gameserver.network.loginserver.LoginServerConnection, java.nio.ByteBuffer)
      */

    @Override
    protected void writeImpl(LoginServerConnection con, ByteBuffer buf) {
        writeC(buf, getOpcode());
        writeD(buf, accountId);
        writeC(buf, characterCount);
    }

}
