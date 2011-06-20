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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Packet with macro list.
 *
 * @author -Nemesiss-
 */
public class SM_MACRO_LIST extends AionServerPacket {
    private Player player;

    /**
     * Constructs new <tt>SM_MACRO_LIST </tt> packet
     */
    public SM_MACRO_LIST(Player player) {
        this.player = player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, player.getObjectId());// player oid
        writeC(buf, 0x01);// unk

        final int size = player.getMacroList().getSize();

        writeH(buf, -size);// (-)count

        if (size > 0) {
            for (Map.Entry<Integer, String> entry : player.getMacroList().entrySet()) {
                writeC(buf, entry.getKey());// order
                writeS(buf, entry.getValue());// xml
            }
        }
    }
}
