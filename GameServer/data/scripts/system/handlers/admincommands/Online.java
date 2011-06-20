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
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * Admin online command.
 *
 * @author Kamui & Xavier
 */

public class Online extends AdminCommand {
    public Online() {
        super("online");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_ONLINE) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
            return;
        }

        int pCount = DAOManager.getDAO(PlayerDAO.class).getOnlinePlayerCount();

        if (pCount == 1) {
            PacketSendUtility.sendMessage(admin, "There is only " + (pCount) + " player online now !");
        } else {
            PacketSendUtility.sendMessage(admin, "There are " + (pCount) + " players online now !");
        }
    }
}