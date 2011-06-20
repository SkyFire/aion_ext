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
import gameserver.model.items.ItemId;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

/**
 * @author Sarynth
 *         <p/>
 *         Simple admin assistance command for adding kinah to self, named player or target player.
 *         <p/>
 *         Kinah Item Id - 182400001 (Using ItemId.KINAH.value())
 */
public class Kinah extends AdminCommand {
    public Kinah() {
        super("kinah");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_KINAH) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
            return;
        }

        if (params == null || params.length < 1 || params.length > 2) {
            PacketSendUtility.sendMessage(admin, "syntax //kinah <player name> <quantity>");
            return;
        }

        long kinahCount;
        Player receiver;

        if (params.length == 1) {
            receiver = admin;
            try {
                kinahCount = Integer.parseInt(params[0]);
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "Kinah value must be an integer.");
                return;
            }
        } else {
            receiver = World.getInstance().findPlayer(Util.convertName(params[0]));

            if (receiver == null) {
                PacketSendUtility.sendMessage(admin, "Could not find an online player with that name.");
                return;
            }

            try {
                kinahCount = Integer.parseInt(params[1]);
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "Kinah value must be an integer.");
                return;
            }
        }

        long count = ItemService.addItem(receiver, ItemId.KINAH.value(), kinahCount);

        if (count == 0) {
            PacketSendUtility.sendMessage(admin, "Kinah given successfully to player " + receiver.getName());
            PacketSendUtility.sendMessage(receiver, "Admin " + admin.getName() + " gives you some kinah.");
        } else {
            PacketSendUtility.sendMessage(admin, "Kinah couldn't be given.");
        }
    }
}
