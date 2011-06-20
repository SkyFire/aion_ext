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

import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_TARGET_SELECTED;
import gameserver.network.aion.serverpackets.SM_TARGET_UPDATE;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

/**
 * Client Sends this packet when /Select NAME is typed.<br>
 * I believe it's the same as mouse click on a character.<br>
 * If client want's to select target - d is object id.<br>
 * If client unselects target - d is 0;
 *
 * @author SoulKeeper, Sweetkr
 */
public class CM_TARGET_SELECT extends AionClientPacket {
    /**
     * Target object id that client wants to select or 0 if wants to unselect
     */
    private int targetObjectId;
    private int type;

    /**
     * Constructs new client packet instance.
     *
     * @param opcode
     */
    public CM_TARGET_SELECT(int opcode) {
        super(opcode);
    }

    /**
     * Read packet.<br>
     * d - object id;
     * c - selection type;
     */
    @Override
    protected void readImpl() {
        targetObjectId = readD();
        type = readC();
    }

    /**
     * Do logging
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (player == null)
            return;

        AionObject obj = World.getInstance().findAionObject(targetObjectId);
        if (obj != null && obj instanceof VisibleObject) {
            if (type == 1) {
                if (((VisibleObject) obj).getTarget() == null)
                    return;
                player.setTarget(((VisibleObject) obj).getTarget());
            } else {
                player.setTarget(((VisibleObject) obj));
            }
        } else {
            player.setTarget(null);
        }
        sendPacket(new SM_TARGET_SELECTED(player));
        PacketSendUtility.broadcastPacket(player, new SM_TARGET_UPDATE(player));
	}
}
