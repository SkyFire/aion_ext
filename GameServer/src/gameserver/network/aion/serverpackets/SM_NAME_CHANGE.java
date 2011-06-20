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
 * @author Rhys2002
 */
public class SM_NAME_CHANGE extends AionServerPacket {

    private int playerObjectId;
    private String oldName;
    private String newName;

    public SM_NAME_CHANGE(int playerObjectId, String oldName, String newName) {
        this.playerObjectId = playerObjectId;
        this.oldName = oldName;
        this.newName = newName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, 0); //unk
        writeD(buf, 0); //unk - 0 or 3
        writeD(buf, playerObjectId);
        writeS(buf, oldName);
        writeS(buf, newName);
    }
}
