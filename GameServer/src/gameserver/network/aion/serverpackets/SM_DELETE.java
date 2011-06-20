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

import gameserver.model.gameobjects.AionObject;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * This packet is informing client that some AionObject is no longer visible.
 *
 * @author -Nemesiss-
 */
public class SM_DELETE extends AionServerPacket {
    /**
     * Object that is no longer visible.
     */
    private final int objectId;
    private final int time;

    /**
     * Constructor.
     *
     * @param object
     */

    public SM_DELETE(AionObject object, int time) {
        this.objectId = object.getObjectId();
        this.time = time;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        int action = 0;
        if (action != 1) {
            writeD(buf, objectId);
            writeC(buf, time); // removal animation speed
		}
	}
}
