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
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.Executor;
import gameserver.world.World;

/**
 * Admin announce faction
 *
 * @author Divinity
 */
public class AnnounceFaction extends AdminCommand {
    public AnnounceFaction() {
        super("announcefaction");
    }

    @Override
    public void executeCommand(Player admin, final String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_ANNOUNCE_FACTION) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
            return;
        }

        if (params.length < 2) {
            PacketSendUtility.sendMessage(admin, "Syntax: //announcefaction <ely | asmo> <message>");
        } else {
            String message = "";

            if (params[0].equals("ely"))
                message += "Elyos : ";
            else
                message += "Asmodians : ";

            // Add with space
            for (int i = 1; i < params.length - 1; i++)
                message += params[i] + " ";

            // Add the last without the end space
            message += params[params.length - 1];

            final String _message = message;
            World.getInstance().doOnAllPlayers(new Executor<Player>() {
                @Override
                public boolean run(Player player) {
                    if (player.getCommonData().getRace() == Race.ELYOS && params[0].equals("ely"))
                        PacketSendUtility.sendSysMessage(player, _message);
                    else if (player.getCommonData().getRace() == Race.ASMODIANS && params[0].equals("asmo"))
                        PacketSendUtility.sendSysMessage(player, _message);
                    return true;
                }
            });
        }
    }
}
