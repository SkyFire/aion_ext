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

package loginserver.network.gameserver.serverpackets;

import loginserver.network.gameserver.GsAuthResponse;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.GsServerPacket;

import java.nio.ByteBuffer;

/**
 * This packet is response for CM_GS_AUTH its notify Gameserver if registration was ok or what was wrong.
 *
 * @author -Nemesiss-
 */
public class SM_GS_AUTH_RESPONSE extends GsServerPacket {
    /**
     * Response for Gameserver authentication
     */
    private final GsAuthResponse response;

    /**
     * Constructor.
     *
     * @param response
     */
    public SM_GS_AUTH_RESPONSE(GsAuthResponse response) {
        super(0x00);

        this.response = response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(GsConnection con, ByteBuffer buf) {
        writeC(buf, getOpcode());
        writeC(buf, response.getResponseId());
    }
}
