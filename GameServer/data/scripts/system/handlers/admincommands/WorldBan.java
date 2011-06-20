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
import gameserver.utils.HumanTime;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author xavier
 */
public class WorldBan extends AdminCommand {
    public WorldBan() {
        super("wban");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        String syntax = "Syntax: //wban <player name> <time_in_minutes> <reason>";
        Player player = null;
        int duration = 0;
        int durationIndex = 0;

        if (!CustomConfig.CHANNEL_ALL_ENABLED) {
            PacketSendUtility.sendMessage(admin, "<There is no such admin command: " + getCommandName() + ">");
            return;
        }

        if (admin.getAccessLevel() < AdminConfig.COMMAND_WORLDBAN) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
            return;
        }

        if (params == null || params.length == 0) {
            PacketSendUtility.sendMessage(admin, syntax);
            return;
        }

        player = parsePlayerParameter(params[0], admin, syntax);
        if (player == null) {
            return;
        }

        try {
            if (player.equals(admin.getTarget())) {
                if (params.length < 1) {
                    PacketSendUtility.sendMessage(admin, syntax);
                    return;
                }
                duration = Integer.parseInt(params[0]);
            } else {
                if (params.length < 2) {
                    PacketSendUtility.sendMessage(admin, syntax);
                    return;
                }
                duration = Integer.parseInt(params[1]);
                durationIndex = 1;
            }
        }
        catch (NumberFormatException e) {
            PacketSendUtility.sendMessage(admin, "Duration invalid " + params[durationIndex]);
            PacketSendUtility.sendMessage(admin, syntax);
            return;
        }

        String reason = "";
        for (int i = durationIndex + 1; i < params.length; i++) {
            reason += params[i] + " ";
        }

        if (reason.trim().isEmpty()) {
            reason = "no reason specified";
        }

        if (!player.banFromWorld(admin.getName(), reason, duration * 60 * 1000)) {
            PacketSendUtility.sendMessage(admin, "Unable to ban " + player.getName() + " from the chat channels, please try again later");
        } else {
            PacketSendUtility.sendMessage(admin, "Player " + player.getName() + " has been banned from the chat channels for " + ((duration == 0) ? "indefinitely" : (HumanTime.approximately(duration * 60 * 1000))) + " for the following reason : " + reason);
            PacketSendUtility.sendSysMessage(player, "You have been banned from chat channels by " + admin.getName() + " for " + ((duration == 0) ? "indefinitely" : HumanTime.approximately(duration * 60 * 1000)) + " for the following reason : " + reason);
        }
    }
}
