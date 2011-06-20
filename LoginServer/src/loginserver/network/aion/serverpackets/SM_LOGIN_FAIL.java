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
package loginserver.network.aion.serverpackets;

import loginserver.network.aion.AionAuthResponse;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author KID
 */
public class SM_LOGIN_FAIL extends AionServerPacket {
    /**
     * response - why login fail
     */
    private AionAuthResponse response;

    /**
     * Constructs new instance of <tt>SM_LOGIN_FAIL</tt> packet.
     *
     * @param response auth responce
     */
    public SM_LOGIN_FAIL(AionAuthResponse response) {
        super(0x01);

        this.response = response;
    }

    /**
     * {@inheritDoc}
     */
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, getOpcode());
        writeD(buf, response.getMessageId());
    }
}