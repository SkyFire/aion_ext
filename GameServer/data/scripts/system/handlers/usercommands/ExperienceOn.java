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

import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

/**
 *
 *
 */
public class ExperienceOn extends UserCommand {
    public ExperienceOn() {
        super("xpon");
    }

    @Override
    public void executeCommand(Player player, String params) {
        if (!CustomConfig.PLAYER_EXPERIENCE_CONTROL) {
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_DISABLED));
            return;
        }

        if (player.isNoExperienceGain()) {
            player.setNoExperienceGain(false);
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_XP_ENABLED));
        } else {
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_XP_ALREADY_ENABLED));
        }
    }


}
