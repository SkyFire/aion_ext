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
import gameserver.network.aion.serverpackets.SM_TIME_CHECK;

/**
 * I dont know what this packet is doing - probably its ping/pong packet
 *
 * @author -Nemesiss-
 */
public class CM_TIME_CHECK extends AionClientPacket {
    /**
     * Nano time / 1000000
     */
    private int nanoTime;

    /**
     * Constructs new instance of <tt>CM_VERSION_CHECK </tt> packet
     *
     * @param opcode
     */
    public CM_TIME_CHECK(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        nanoTime = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        AionConnection client = getConnection();
        int timeNow = (int) (System.nanoTime() / 1000000);
        @SuppressWarnings("unused")
        int diff = timeNow - nanoTime;
        client.sendPacket(new SM_TIME_CHECK(nanoTime));

        //log.info("CM_TIME_CHECK: " + nanoTime + " =?= " + timeNow + " dif: " + diff);
	}
}
