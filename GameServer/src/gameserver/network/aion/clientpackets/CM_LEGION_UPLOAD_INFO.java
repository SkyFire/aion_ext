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
import gameserver.services.LegionService;

/**
 * @author Simple
 */
public class CM_LEGION_UPLOAD_INFO extends AionClientPacket {

    /**
     * Emblem related information *
     */
    private int totalSize;
    private int color_r;
    private int color_g;
    private int color_b;

    /**
     * @param opcode
     */
    public CM_LEGION_UPLOAD_INFO(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        totalSize = readD();
        readC();//0xFF not default emblem 0x00 default emblem(not in this packet)
        color_r = readC();
        color_g = readC();
        color_b = readC();
    }

    @Override
    protected void runImpl() {
        LegionService.getInstance().uploadEmblemInfo(getConnection().getActivePlayer(), totalSize, color_r, color_g, color_b);
	}
}
