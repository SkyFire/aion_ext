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

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.configs.administration.AdminConfig;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.loginserver.LoginServer;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Watson
 */
public class Unban extends AdminCommand {
    public Unban() {
        super("unban");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_BAN) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
            return;
        }

        if (params == null || params.length < 1) {
            PacketSendUtility.sendMessage(admin, "Syntax: //unban <player name> <account | ip | full>");
            return;
        }

        // Banned player must be offline, so get his account ID from database
        String name = Util.convertName(params[0]);
        int accountId = DAOManager.getDAO(PlayerDAO.class).getAccountIdByName(name);
        if (accountId == 0) {
            PacketSendUtility.sendMessage(admin, "Player " + name + " was not found!");
            PacketSendUtility.sendMessage(admin, "Syntax: //unban <player name> <account | ip | full>");
            return;
        }

        byte type = 3; // Default: full
        if (params.length > 1) {
            // Smart Matching
            String stype = params[1].toLowerCase();
            if (("account").startsWith(stype))
                type = 1;
            else if (("ip").startsWith(stype))
                type = 2;
            else if (("full").startsWith(stype))
                type = 3;
            else {
                PacketSendUtility.sendMessage(admin, "Syntax: //unban <player name> <account | ip | full>");
                return;
            }
        }

        LoginServer.getInstance().sendBanPacket(type, accountId, "", -1, admin.getObjectId());
    }
}
