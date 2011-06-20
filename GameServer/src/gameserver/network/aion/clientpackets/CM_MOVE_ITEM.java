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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

/**
 * @author alexa026, kosyachok
 */
public class CM_MOVE_ITEM extends AionClientPacket {

    /**
     * Target object id that client wants to TALK WITH or 0 if wants to unselect
     */
    private int targetObjectId;
    private int source;
    private int destination;
    private int slot;

    /**
     * Constructs new instance of <tt>CM_CM_REQUEST_DIALOG </tt> packet
     *
     * @param opcode
     */
    public CM_MOVE_ITEM(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        targetObjectId = readD();// empty
        source = readC();        //FROM (0 - player inventory, 1 - regular warehouse, 2 - account warehouse, 3 - legion warehouse)
        destination = readC();   //TO
        slot = readH();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();

        if (player != null) {
            ItemService.moveItem(player, targetObjectId, source, destination, slot);

            PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_LOOT, 0, 0));
        }

        //		sendPacket(new SM_LOOT_STATUS(targetObjectId,3));
    }
}
