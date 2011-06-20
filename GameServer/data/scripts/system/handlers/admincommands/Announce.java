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
import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.Executor;
import gameserver.world.World;

/**
 * @author Ben, Ritsu
 *         <p/>
 *         Smart Matching Enabled
 *         //announce a message
 *         The above example will work for "anonymous" flag.
 *         //announce name message
 *         The above example will show "[GM] Name: message".
 */
public class Announce extends AdminCommand {
    public Announce() {
        super("announce");
    }

    @Override
    public int getSplitSize() {
        return 2;
    }

    @Override
    public void executeCommand(Player admin, String[] params) {

        if (admin.getAccessLevel() < AdminConfig.COMMAND_ANNOUNCE) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
            return;
        }

        if (params == null || params.length < 2) {
            PacketSendUtility.sendMessage(admin, "Syntax: //announce <anonymous|name> \"<message>\"");
            return;
        }

        String message = "";

        if (("anonymous").startsWith(params[0].toLowerCase())) {
            message += "Announce: ";
        } else if (("name").startsWith(params[0].toLowerCase())) {
            if (CustomConfig.GMTAG_DISPLAY) {
                if (admin.getAccessLevel() == 1) {
                    message += CustomConfig.GM_LEVEL1.trim();
                } else if (admin.getAccessLevel() == 2) {
                    message += CustomConfig.GM_LEVEL2.trim();
                } else if (admin.getAccessLevel() == 3) {
                    message += CustomConfig.GM_LEVEL3.trim();
                }
            }

            message += admin.getName() + ": ";
        } else {
            PacketSendUtility.sendMessage(admin, "Syntax: //announce <anonymous|name> \"<message>\"");
            return;
        }

        if (params.length > 1) {
            for(int i = 1; i < params.length; ++i) {
                message += params[i] + " ";
            }
        }
        else {
            message += params[1];
        }

        final String _message = message;

        World.getInstance().doOnAllPlayers(new Executor<Player>() {
            @Override
            public boolean run(Player player) {
                PacketSendUtility.sendSysMessage(player, _message);
                return true;
            }
        });
    }
}
