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

import gameserver.GameServer;
import gameserver.configs.main.GSConfig;
import gameserver.configs.network.NetworkConfig;
import gameserver.model.Race;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.ChatService;

import java.nio.ByteBuffer;

/**
 * @author -Nemesiss- CC fix modified by Novo
 */

public class SM_VERSION_CHECK extends AionServerPacket {

    /**
     * @param chatService
     */
    public SM_VERSION_CHECK() {
    }

    /**
     * {@inheritDoc}
     */

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, 0x00);
        writeC(buf, NetworkConfig.GAMESERVER_ID);
        writeD(buf, 0x000188AD);// unk
        writeD(buf, 0x000188A6);// unk
        writeD(buf, 0x00000000);// unk
        writeD(buf, 0x00018898);// unk
        writeD(buf, 0x4C346D9D);// unk
        writeC(buf, 0x00);// unk
        writeC(buf, GSConfig.SERVER_COUNTRY_CODE);// country code;
        writeC(buf, 0x00);// unk
        if (GSConfig.FACTIONS_RATIO_LIMITED) {
            if (GameServer.getRatiosFor(Race.ELYOS) > GSConfig.FACTIONS_RATIO_VALUE) {
                writeC(buf, GSConfig.SERVER_MODE | 0x04); // limit elyos creation
            } else if (GameServer.getRatiosFor(Race.ASMODIANS) > GSConfig.FACTIONS_RATIO_VALUE) {
                writeC(buf, GSConfig.SERVER_MODE | 0x08); // limit asmos creation
            } else {
                writeC(buf, GSConfig.SERVER_MODE);
            }
        } else {
            writeC(buf, GSConfig.SERVER_MODE); // Server mode : 0x80 = one race / 0x01 = free race / 0x22 = Character
        }
        writeD(buf, (int) (System.currentTimeMillis() / 1000));
        writeH(buf, 0x015E);
        writeH(buf, 0x0A01);
        writeH(buf, 0x0A01);
        writeH(buf, 0x020A);
        writeC(buf, 0x00);
		writeC(buf, 0x14);
        writeC(buf, 0x01);
        writeC(buf, 0x00);
        writeC(buf, 0x00);
        writeB(buf, ChatService.getIp());
        writeH(buf, ChatService.getPort());
    }
}
