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

import gameserver.model.gameobjects.Kisk;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/*
 *
 * @author sweetkr
 * @author Sarynth
 *
 */

public class SM_SET_BIND_POINT extends AionServerPacket {

    private final int mapId;
    private final float x;
    private final float y;
    private final float z;
    private final Kisk kisk;

    public SM_SET_BIND_POINT(int mapId, float x, float y, float z, Player player) {
        this.mapId = mapId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.kisk = player.getKisk();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        // Appears 0x04 if bound to a kisk. 0x00 if not.
        writeC(buf, (kisk == null ? 0x00 : 0x04));

        writeC(buf, 0x01);// unk
        writeD(buf, mapId);// map id
        writeF(buf, x); // coordinate x
        writeF(buf, y); // coordinate y
        writeF(buf, z); // coordinate z
        writeD(buf, (kisk == null ? 0x00 : kisk.getObjectId())); // kisk object id
    }
}
