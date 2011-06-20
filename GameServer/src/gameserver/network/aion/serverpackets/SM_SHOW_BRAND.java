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
 * @author Sweetkr
 */
public class SM_SHOW_BRAND extends AionServerPacket {
    private int brandId;
    private int targetObjectId;

    public SM_SHOW_BRAND(int brandId, int targetObjectId) {
        this.brandId = brandId;
        this.targetObjectId = targetObjectId;
    }


    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {

        writeH(buf, 0x01); //unk
		writeD(buf, 0x01); //unk
        writeD(buf, brandId);
        writeD(buf, targetObjectId);

    }
}
