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
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author -Nemesiss-
 */
public class SM_L2AUTH_LOGIN_CHECK extends AionServerPacket {
    /**
     * True if client is authed.
     */
    private final boolean ok;

    /**
     * Constructs new <tt>SM_L2AUTH_LOGIN_CHECK </tt> packet
     *
     * @param ok
     */
    public SM_L2AUTH_LOGIN_CHECK(boolean ok) {
        this.ok = ok;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        Account account = con.getAccount();

        writeD(buf, ok ? 0x00 : 0x01);
        writeS(buf, account.getName());
    }
}
