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
import gameserver.services.PunishmentService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

import java.util.NoSuchElementException;

/**
 * @author lord_rex
 *         Command: //sprison <player name> <time delay>(minutes)
 *         This command sends a player to prison.
 */
public class SendToPrison extends AdminCommand {

    public SendToPrison() {
        super("sprison");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_PRISON) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
            return;
        }

        if (params.length == 0 || params.length != 2) {
            PacketSendUtility.sendMessage(admin, "syntax //sprison <player name> <time delay>");
            return;
        }

        try {
            Player playerToPrison = World.getInstance().findPlayer(Util.convertName(params[0]));
            int delay = Integer.parseInt(params[1]);

            if (playerToPrison != null) {
                PunishmentService.setIsInPrison(playerToPrison, true, delay);
                if (CustomConfig.CHANNEL_ALL_ENABLED)
                    playerToPrison.banFromWorld(admin.getName(), "you have been sent to prison", 0);
                PacketSendUtility.sendMessage(admin, "Player " + playerToPrison.getName() + " has been sent to prison for "
                        + delay + ".");
            }
        }
        catch (NoSuchElementException nsee) {
            PacketSendUtility.sendMessage(admin, "Usage: //sprison <player name> <time delay>");
        }
        catch (Exception e) {
            PacketSendUtility.sendMessage(admin, "Usage: //sprison <player name> <time delay>");
        }
    }
}
