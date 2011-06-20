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

/**
 * Client sends this only once.
 *
 * @author Luno
 */
public class CM_MAC_ADDRESS2 extends AionClientPacket {
    /**
     * Constructor
     *
     * @param opcode
     */
    public CM_MAC_ADDRESS2(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        @SuppressWarnings("unused")
        int objectId = readD(); // lol NC
        @SuppressWarnings("unused")
        byte[] macAddress = readB(6);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        // TODO server should response - find out response packet.
	}
}
