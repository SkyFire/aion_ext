/*
 *  This file is part of aionxemu <http://www.aionxemu.com>.
 *
 *  This software is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

/**
 * @author Untamed
 *
 */
public class GMList extends AdminCommand {
    public GMList() {
        super("gmlist");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_GMLIST) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        //Message[s] before start of list
        String msg = "Online GMs:\n";
        msg += "--------------------\n";

        //Iterate through all players online
        for(Player player : World.getInstance().getPlayers())
        {
            if (player.isGM())//If player is GM, list them
            {
                msg += player.getName() + "\n";
            }
        }

        msg += "--------------------\n";

        //Mesage[s] after end of list
        PacketSendUtility.sendMessage(admin, msg);
    }
}
