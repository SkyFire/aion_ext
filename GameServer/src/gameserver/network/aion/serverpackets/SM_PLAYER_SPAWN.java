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

/**
 * This packet is notify client what map should be loaded.
 *
 * @author -Nemesiss-
 */
public class SM_PLAYER_SPAWN extends AionServerPacket {
    /**
     * Player that is entering game.
     */
    private final Player player;

    /**
     * Constructor.
     *
     * @param player
     */
    public SM_PLAYER_SPAWN(Player player) {
        super();
        this.player = player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, player.getWorldId());
        writeD(buf, player.getWorldId());//world + chnl
        writeD(buf, 0x00);// unk
        writeC(buf, 0x00);// unk
        writeF(buf, player.getX());// x
        writeF(buf, player.getY());// y
        writeF(buf, player.getZ());// z
        writeC(buf, player.getHeading());// heading
	}
}
