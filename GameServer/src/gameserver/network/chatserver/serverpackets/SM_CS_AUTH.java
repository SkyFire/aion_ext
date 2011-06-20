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
package gameserver.network.chatserver.serverpackets;

import gameserver.configs.network.IPConfig;
import gameserver.configs.network.NetworkConfig;
import gameserver.network.chatserver.ChatServerConnection;
import gameserver.network.chatserver.CsServerPacket;

import java.nio.ByteBuffer;

/**
 * @author ATracer
 */
public class SM_CS_AUTH extends CsServerPacket {

    public SM_CS_AUTH() {
        super(0x00);
    }

    @Override
    protected void writeImpl(ChatServerConnection con, ByteBuffer buf) {
        writeC(buf, getOpcode());
        writeC(buf, NetworkConfig.GAMESERVER_ID);
        writeC(buf, IPConfig.getDefaultAddress().length);
        writeB(buf, IPConfig.getDefaultAddress());
        writeS(buf, NetworkConfig.CHAT_PASSWORD);
    }
}
