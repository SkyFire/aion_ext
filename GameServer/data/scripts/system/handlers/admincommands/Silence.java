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
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Kamui
 */
public class Silence extends AdminCommand {
    public Silence() {
        super("silence");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_SILENCE) {
            PacketSendUtility.sendMessage(admin, "You do not have enough rights to execute this command");
            return;
        }

        if (admin.isWhisperable()) {
            admin.setWhisperable(false);
            PacketSendUtility.sendMessage(admin, "Whisper refusal mode enabled.");
            PacketSendUtility.sendMessage(admin, "You are not able to receive whispers.");
        } else {
            admin.setWhisperable(true);
            PacketSendUtility.sendMessage(admin, "Whisper refusal mode disabled.");
            PacketSendUtility.sendMessage(admin, "You are now able to receive whispers.");
        }
    }
}
