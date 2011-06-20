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

package gameserver.network.aion.clientpackets;

import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.AionConnection;
import gameserver.network.loginserver.LoginServer;

/**
 * In this packets aion client is asking for fast reconnection to LoginServer.
 *
 * @author -Nemesiss-
 */
public class CM_RECONNECT_AUTH extends AionClientPacket {

    /**
     * Constructs new instance of <tt>CM_RECONNECT_AUTH </tt> packet
     *
     * @param opcode
     */
    public CM_RECONNECT_AUTH(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        AionConnection client = getConnection();
        // TODO! check if may reconnect
        LoginServer.getInstance().requestAuthReconnection(client);
	}
}
