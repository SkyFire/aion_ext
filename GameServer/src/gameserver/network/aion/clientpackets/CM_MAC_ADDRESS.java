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
 * In this packet client is sending Mac Address - haha.
 *
 * @author -Nemesiss-
 */
public class CM_MAC_ADDRESS extends AionClientPacket {
    /**
     * Mac Addres send by client in the same format as: ipconfig /all [ie: xx-xx-xx-xx-xx-xx]
     */
    @SuppressWarnings("unused")
    private String macAddress;

    /**
     * Constructs new instance of <tt>CM_MAC_ADDRESS </tt> packet
     *
     * @param opcode
     */
    public CM_MAC_ADDRESS(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        readB(22);// some shit
        macAddress = readS();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
    }
}
