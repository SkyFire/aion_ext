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

import gameserver.model.Race;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author Sweetkr
 */
public class SM_RIFT_ANNOUNCE extends AionServerPacket {
    private Race race;

    /**
     * Constructs new <tt>SM_RIFT_ANNOUNCE</tt> packet
     *
     * @param player
     */
    public SM_RIFT_ANNOUNCE(Race race) {
        this.race = race;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, 0); // unk 1.9
        switch (race) //destination
        {
            //master rift announcements
            case ASMODIANS:
                writeD(buf, 1);
                writeD(buf, 0);
                break;
            case ELYOS:
                writeD(buf, 1);
                writeD(buf, 0);
                break;
        }

        // [1.9] Need Extra writeD
        // Sample 08 50 A7 00 00 00 00 01 00 00 00 00 00 00 00
        // Sample 08 50 A7 00 00 00 00 00 00 00 00 00 00 00 00
        // Sample 08 50 A7 00 00 00 00 00 00 00 00 01 00 00 00
        // Sample 08 50 A7 01 00 2E 00 00 00 00 00 00 00 00 00
        // Sample 08 50 A7 00 A5 B3 43 00 00 00 00 00 00 00 00
        // Sample 08 50 A7 01 2C 0B 8E 00 00 00 00 00 00 00 00
        // Delete these once this packet is fixed for 1.9

        // Old data?
        // ELYSEA:
        // 1 0 -> to asmodae
        // 0 1 -> to elysea
        // ASMODAE
        // 1 0 -> to elysea
        // 0 1 -> to asmodae
	}
}
