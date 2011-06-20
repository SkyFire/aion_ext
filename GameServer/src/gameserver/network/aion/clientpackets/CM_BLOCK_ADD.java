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
import gameserver.network.aion.serverpackets.SM_BLOCK_RESPONSE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.SocialService;
import gameserver.world.World;
import org.apache.log4j.Logger;

/**
 * @author Ben
 */
public class CM_BLOCK_ADD extends AionClientPacket {
    private static Logger log = Logger.getLogger(CM_BLOCK_ADD.class);

    private String targetName;
    private String reason;

    public CM_BLOCK_ADD(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        targetName = readS();
        reason = readS();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {

        Player activePlayer = getConnection().getActivePlayer();

        Player targetPlayer = World.getInstance().findPlayer(targetName);

        //Trying to block self
        if (activePlayer.getName().equalsIgnoreCase(targetName)) {
            sendPacket(new SM_BLOCK_RESPONSE(SM_BLOCK_RESPONSE.CANT_BLOCK_SELF, targetName));
        }

        //List full
        else if (activePlayer.getBlockList().isFull()) {
            sendPacket(new SM_BLOCK_RESPONSE(SM_BLOCK_RESPONSE.LIST_FULL, targetName));
        }

        //Player offline
        else if (targetPlayer == null) {
            sendPacket(new SM_BLOCK_RESPONSE(SM_BLOCK_RESPONSE.TARGET_NOT_FOUND, targetName));
        }

        //Player is your friend
        else if (activePlayer.getFriendList().getFriend(targetPlayer.getObjectId()) != null) {
            sendPacket(SM_SYSTEM_MESSAGE.BLOCKLIST_NO_BUDDY);
        }

        //Player already blocked
        else if (activePlayer.getBlockList().contains(targetPlayer.getObjectId())) {
            sendPacket(SM_SYSTEM_MESSAGE.BLOCKLIST_ALREADY_BLOCKED);
        }

        //Try and block player
        else if (!SocialService.addBlockedUser(activePlayer, targetPlayer, reason)) {
            log.error("Failed to add " + targetPlayer.getName() + " to the block list for " + activePlayer.getName() + " - check database setup.");
        }


    }

}
