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
 * This class is holding Info packets for Windstream
 *
 * @authors Vyaslav, Ares/Kaipo
 */
public class SM_WINDSTREAM extends AionServerPacket {
    public static int C_VALIDATE = 0x00;
    public static int C_VALIDATED = 0x01;
    public static int C_START_BOOST = 7;
    public static int C_END_BOOST = 8;

    private int validatePos;


    public SM_WINDSTREAM(int validatePos) {
        this.validatePos = validatePos;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeH(buf, validatePos);
        writeH(buf, 0);
        writeC(buf, 0x01);

    }
}
