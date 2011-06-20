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
 * This packet is used to update current exp / recoverable exp / max exp values.
 *
 * @author Luno
 * @updated by alexa026
 */
public class SM_STATUPDATE_EXP extends AionServerPacket {
    private long currentExp;
    private long recoverableExp;
    private long maxExp;

    private long curBoostExp = 0;
    private long maxBoostExp = 0;

    /**
     * @param currentExp
     * @param recoverableExp
     * @param maxExp
     */
    public SM_STATUPDATE_EXP(long currentExp, long recoverableExp, long maxExp) {
        this.currentExp = currentExp;
        this.recoverableExp = recoverableExp;
        this.maxExp = maxExp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeQ(buf, currentExp);
        writeQ(buf, recoverableExp);
        writeQ(buf, maxExp);
        writeQ(buf, curBoostExp);
        writeQ(buf, maxBoostExp);
	}

}
