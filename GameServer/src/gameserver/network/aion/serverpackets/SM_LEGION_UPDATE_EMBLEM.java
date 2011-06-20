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
 * @author Simple
 */
public class SM_LEGION_UPDATE_EMBLEM extends AionServerPacket {
    /**
     * Legion emblem information *
     */
    private int legionId;
    private int emblemVer;
    private int color_r;
    private int color_g;
    private int color_b;
    private boolean isCustom;

    /**
     * This constructor will handle legion emblem info
     *
     * @param legion
     */
    public SM_LEGION_UPDATE_EMBLEM(int legionId, int emblemVer, int color_r, int color_g, int color_b, boolean isCustom) {
        this.legionId = legionId;
        this.emblemVer = emblemVer;
        this.color_r = color_r;
        this.color_g = color_g;
        this.color_b = color_b;
        this.isCustom = isCustom;
    }

    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, legionId);
        writeC(buf, emblemVer);
        writeC(buf, isCustom ? 0x80 : 0x00);
        writeC(buf, 0xFF);//sets transparency 0xFF is default; but 0x00 is much better looking ;o)
        writeC(buf, color_r);
        writeC(buf, color_g);
        writeC(buf, color_b);
    }
}
