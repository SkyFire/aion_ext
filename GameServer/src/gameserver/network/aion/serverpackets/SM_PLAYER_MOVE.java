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
 * @author cura
 */
public class SM_PLAYER_MOVE extends AionServerPacket {
    private float x;
    private float y;
    private float z;
    private byte heading;

    public SM_PLAYER_MOVE(float x, float y, float z, byte heading) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.heading = heading;
    }

    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        writeF(buf, x);
        writeF(buf, y);
        writeF(buf, z);
        writeC(buf, heading);
    }
}
