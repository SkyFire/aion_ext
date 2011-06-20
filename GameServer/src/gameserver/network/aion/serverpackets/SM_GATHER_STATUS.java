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
 */
public class SM_GATHER_STATUS extends AionServerPacket {
    private int status;
    private int playerobjid;
    private int gatherableobjid;


    public SM_GATHER_STATUS(int playerobjid, int gatherableobjid, int status) {
        this.playerobjid = playerobjid;
        this.gatherableobjid = gatherableobjid;
        this.status = status;
    }

    /**
     * {@inheritDoc}
     */

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {

        writeD(buf, playerobjid);
        writeD(buf, gatherableobjid);
        writeH(buf, 0); //unk
        writeC(buf, status);

    }
}
