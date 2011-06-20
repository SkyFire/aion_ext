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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * Notifies players when their friends log in, out, or delete them
 *
 * @author Ben
 */
public class SM_FRIEND_NOTIFY extends AionServerPacket {
    /**
     * Buddy has logged in
     * (Or become visible)
     */
    public static final int LOGIN = 0;
    /**
     * Buddy has logged out
     * (Or become invisible)
     */
    public static final int LOGOUT = 1;
    /**
     * Buddy has deleted you
     */
    public static final int DELETED = 2;

    private final int code;
    private final String name;

    /**
     * Constructs a new notify packet
     *
     * @param code Message code
     * @param name Name of friend
     */
    public SM_FRIEND_NOTIFY(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeS(buf, name);
        writeC(buf, code);
	}
}
