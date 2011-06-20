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

package usercommands;

import gameserver.model.gameobjects.player.Player;
import gameserver.services.DredgionInstanceService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;

/**
 *
 *
 */
public class Dredgion extends UserCommand {
    public Dredgion() {
        super("dredgion");
    }

    @Override
    public void executeCommand(Player player, String params) {
        String[] args = params.split(" ");

        if (args.length != 1) {
            PacketSendUtility.sendMessage(player, "syntax: .dredgion <register | unregister>");
            return;
        }

        if (args[0].equalsIgnoreCase("register")) {
            DredgionInstanceService.getInstance().registerPlayer(player);
            return;
        }

        if (args[0].equalsIgnoreCase("unregister")) {
            DredgionInstanceService.getInstance().unregisterPlayer(player);
            return;
        }

        PacketSendUtility.sendMessage(player, "syntax: .dredgion <register | unregister>");

    }

}
