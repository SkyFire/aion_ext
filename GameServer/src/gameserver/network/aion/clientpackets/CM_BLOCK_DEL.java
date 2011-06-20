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

import gameserver.model.gameobjects.player.BlockedPlayer;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.SocialService;
import org.apache.log4j.Logger;

/**
 * @author Ben
 */
public class CM_BLOCK_DEL extends AionClientPacket {
    private static Logger log = Logger.getLogger(CM_BLOCK_DEL.class);

    private String targetName;

    /**
     * @param opcode
     */
    public CM_BLOCK_DEL(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        targetName = readS();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();

        BlockedPlayer target = activePlayer.getBlockList().getBlockedPlayer(targetName);
        if (target == null) {
            sendPacket(SM_SYSTEM_MESSAGE.BUDDYLIST_NOT_IN_LIST);
        } else {
            if (!SocialService.deleteBlockedUser(activePlayer, target.getObjId())) {
                log.debug("Could not unblock " + targetName + " from " + activePlayer.getName() + " blocklist. Check database setup.");
            }
        }
    }
}
