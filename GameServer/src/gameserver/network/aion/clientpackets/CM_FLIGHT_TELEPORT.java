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

import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.network.aion.AionClientPacket;
import gameserver.world.World;

/**
 * Packet about player flying teleport movement.
 *
 * @author -Nemesiss-, Sweetkr
 */
public class CM_FLIGHT_TELEPORT extends AionClientPacket {
    float x, y, z;
    int distance;

    /**
     * Constructs new instance of <tt>CM_FLIGHT_TELEPORT </tt> packet
     *
     * @param opcode
     */
    public CM_FLIGHT_TELEPORT(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        readD(); // mapId
        x = readF();
        y = readF();
        z = readF();
        readC(); // locationId
        distance = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();

        if (player != null && player.isInState(CreatureState.FLIGHT_TELEPORT)) {
            player.setFlightDistance(distance);
            World.getInstance().updatePosition(player, x, y, z, (byte) 0);
        }
    }
}
