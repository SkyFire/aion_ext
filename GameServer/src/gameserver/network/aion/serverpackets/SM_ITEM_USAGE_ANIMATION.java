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
 * @author ATracer
 */
public class SM_ITEM_USAGE_ANIMATION extends AionServerPacket {
    private int playerObjId;
    private int itemObjId;
    private int itemId;
    private int time;
    private int end;
    private int unk;

    public SM_ITEM_USAGE_ANIMATION(int playerObjId, int itemObjId, int itemId) {
        this.playerObjId = playerObjId;
        this.itemObjId = itemObjId;
        this.itemId = itemId;
        this.time = 0;
        this.end = 1;
        this.unk = 1;
    }

    public SM_ITEM_USAGE_ANIMATION(int playerObjId, int itemObjId, int itemId, int time, int end, int unk) {
        this.playerObjId = playerObjId;
        this.itemObjId = itemObjId;
        this.itemId = itemId;
        this.time = time;
        this.end = end;
        this.unk = unk;
    }

    public SM_ITEM_USAGE_ANIMATION(int playerObjId, int itemObjId, int itemId, int time, int end) {
        this.playerObjId = playerObjId;
        this.itemObjId = itemObjId;
        this.itemId = itemId;
        this.time = time;
        this.end = end;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, playerObjId); // player obj id
        writeD(buf, playerObjId); // player obj id 2x - other player? maybe item can be used on other player.

        writeD(buf, itemObjId); // itemObjId
        writeD(buf, itemId); // item id

        writeD(buf, time); // unk
        writeC(buf, end); // unk
        writeC(buf, 1); // unk
        writeC(buf, 0);
        writeD(buf, unk);
    }
}