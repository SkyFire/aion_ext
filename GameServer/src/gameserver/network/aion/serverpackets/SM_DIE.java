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
 * @author orz
 * @author Sarynth thx Rhys2002 for packets. :)
 */
public class SM_DIE extends AionServerPacket {
    private boolean hasRebirth;
    private boolean hasItem;
    private int remainingKiskTime;

    public SM_DIE(boolean hasRebirth, boolean hasItem, int remainingKiskTime) {
        this.hasRebirth = hasRebirth;
        this.hasItem = hasItem;
        this.remainingKiskTime = remainingKiskTime;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, (hasRebirth ? 1 : 0)); // skillRevive
        writeC(buf, (hasItem ? 1 : 0)); // itemRevive
        writeD(buf, remainingKiskTime);
    }
}
