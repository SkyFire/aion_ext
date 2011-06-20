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
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.siege.Artifact;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

/**
 * @author alexa026, Avol
 *         modified by ATracer
 */
public class CM_SHOW_DIALOG extends AionClientPacket {
    private int targetObjectId;

    /**
     * Constructs new instance of <tt>CM_SHOW_DIALOG </tt> packet
     *
     * @param opcode
     */
    public CM_SHOW_DIALOG(int opcode) {
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
        AionObject targetObject = World.getInstance().findAionObject(targetObjectId);
        Player player = getConnection().getActivePlayer();

        if (targetObject == null || player == null)
            return;

        if (targetObject instanceof Artifact) {
            ((Artifact) targetObject).getController().onDialogRequest(player);
        } else if (targetObject instanceof Npc) {
            ((Npc) targetObject).setTarget(player);

            //TODO this is not needed for all dialog requests
            PacketSendUtility.broadcastPacket((Npc) targetObject,
                    new SM_LOOKATOBJECT((Npc) targetObject));

            ((Npc) targetObject).getController().onDialogRequest(player);
        }
    }
}