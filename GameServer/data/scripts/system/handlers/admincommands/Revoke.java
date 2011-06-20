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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.loginserver.LoginServer;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

/**
 * Admin revoke command.
 *
 * @author Cyrakuse
 * @modified By Aionchs-Wylovech
 */

public class Revoke extends AdminCommand {

    public Revoke() {
        super("revoke");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_REVOKE) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        if (params.length != 2) {
            PacketSendUtility.sendMessage(admin, "syntax //revoke <player name> <accesslevel | membership>");
            return;
        }

        int type = 0;
        if (params[1].toLowerCase().equals("accesslevel")) {
            type = 1;
        } else if (params[1].toLowerCase().equals("membership")) {
            type = 2;
        } else {
            PacketSendUtility.sendMessage(admin, "syntax //revoke <player name> <accesslevel | membership>");
            return;
        }

        Player player = World.getInstance().findPlayer(Util.convertName(params[0]));
        if (player == null) {
            PacketSendUtility.sendMessage(admin, "the specified player is not online.");
            return;
        }
        LoginServer.getInstance().sendLsControlPacket(player.getAcountName(), player.getName(), admin.getName(), 0, type);
    }
}
