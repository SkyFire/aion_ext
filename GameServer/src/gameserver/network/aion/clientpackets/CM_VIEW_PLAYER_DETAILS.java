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

import gameserver.model.gameobjects.player.DeniedStatus;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_VIEW_PLAYER_DETAILS;
import gameserver.world.World;
import org.apache.log4j.Logger;

/**
 * @author Avol
 */
public class CM_VIEW_PLAYER_DETAILS extends AionClientPacket {
    private static final Logger log = Logger.getLogger(CM_VIEW_PLAYER_DETAILS.class);

    private int targetObjectId;

    public CM_VIEW_PLAYER_DETAILS(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        targetObjectId = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player player = World.getInstance().findPlayer(targetObjectId);
        if (player == null) {
            //probably targetObjectId can be 0
            log.warn("CHECKPOINT: can't show player details for " + targetObjectId);
            return;
        }

        if (player.getPlayerSettings().isInDeniedStatus(DeniedStatus.VEIW_DETAIL)) {
            sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_WATCH(player.getName()));
            return;
        }
        sendPacket(new SM_VIEW_PLAYER_DETAILS(targetObjectId, player.getEquipment().getEquippedItemsWithoutStigma()));
    }
}
