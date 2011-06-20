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
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Watson
 */
public class BanIP extends AdminCommand {
    public BanIP() {
        super("banip");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_BAN) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
            return;
        }

        if (params == null || params.length < 1) {
            PacketSendUtility.sendMessage(admin, "Syntax: //banip <mask> <time in minutes>");
            return;
        }

        String mask = params[0];

        int time = 0; // Default: infinity
        if (params.length > 1) {
            try {
                time = Integer.parseInt(params[1]);
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "Syntax: //banip <mask> <time in minutes>");
                return;
            }
        }

        LoginServer.getInstance().sendBanPacket((byte) 2, 0, mask, time, admin.getObjectId());
    }
}
