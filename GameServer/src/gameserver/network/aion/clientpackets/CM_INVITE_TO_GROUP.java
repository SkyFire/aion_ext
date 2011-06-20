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

import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.DeniedStatus;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.AllianceService;
import gameserver.services.GroupService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.world.World;

/**
 * @author Lyahim, ATracer
 *         Modified by Simple
 */
public class CM_INVITE_TO_GROUP extends AionClientPacket {

    private String name;
    private int inviteType;

    public CM_INVITE_TO_GROUP(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        inviteType = readC();
        name = readS();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        if (CustomConfig.GMTAG_DISPLAY) {
            name = name.replaceAll(CustomConfig.GM_LEVEL1, "");
            name = name.replaceAll(CustomConfig.GM_LEVEL2, "");
            name = name.replaceAll(CustomConfig.GM_LEVEL3, "");
            name = name.replaceAll(CustomConfig.GM_LEVEL4, "");
            name = name.replaceAll(CustomConfig.GM_LEVEL5, "");
        }
        
        final String playerName = Util.convertName(name);

        final Player inviter = getConnection().getActivePlayer();
        final Player invited = World.getInstance().findPlayer(playerName);

        if (invited != null) {
            if (invited.getPlayerSettings().isInDeniedStatus(DeniedStatus.GROUP)) {
                sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_INVITE_PARTY(invited.getName()));
                return;
            }
            if (inviteType == 0)
                GroupService.getInstance().invitePlayerToGroup(inviter, invited);
            else if (inviteType == 10)
                AllianceService.getInstance().invitePlayerToAlliance(inviter, invited);
            else
                PacketSendUtility.sendMessage(inviter, "You used an unknown invite type: " + inviteType);
        } else
            inviter.getClientConnection().sendPacket(SM_SYSTEM_MESSAGE.PLAYER_IS_OFFLINE(name));
    }
}
