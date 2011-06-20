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
import gameserver.model.ChatType;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * Admin notice command
 *
 * @author Jenose, ZeroSignal
 *         Updated By Darkwolf
 */

public class Notice extends AdminCommand {
    public Notice() {
        super("notice");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_NOTICE) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        if (params == null || params.length < 1) {
            PacketSendUtility.sendMessage(admin, "syntax //notice <message>");
            return;
        }

        String msg;
        if (params.length > 1) {
            msg = "";
            try {
                for (int i = 0; i < params.length; i++) {
                    msg += " " + params[i];
                }                    
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "parameters should be text and numbers");
                return;
            }
        }
        else
            msg = params[0];

        PacketSendUtility.broadcastPacket(admin, new SM_MESSAGE(0, null, "Information : " + msg, ChatType.SYSTEM_NOTICE), true);
    }
}