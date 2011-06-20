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
import gameserver.network.aion.AionClientPacket;
import gameserver.services.DropService;

/**
 * @author alexa026, Correted by Metos, ATracer
 */
public class CM_START_LOOT extends AionClientPacket {
    /**
     * Target object id that client wants to TALK WITH or 0 if wants to unselect
     */

    private int targetObjectId;
    private int action;

    /**
     * Constructs new instance of <tt>CM_CM_REQUEST_DIALOG </tt> packet
     *
     * @param opcode
     */
    public CM_START_LOOT(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        targetObjectId = readD();// empty
        action = readC();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (player == null)
            return;

        if (action == 0) //open
        {
            DropService.getInstance().requestDropList(player, targetObjectId);
        } else if (action == 1) //close
        {
            DropService.getInstance().requestDropList(player, targetObjectId, true);
		}
	}
}