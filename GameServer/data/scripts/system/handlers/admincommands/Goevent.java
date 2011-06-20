/*
 *  This file is part of Aion X EMU <aionxemu.com>.
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

/**
 * @author Alfa
 *
 */
public class Goevent extends AdminCommand
{
    public Goevent() {
          super("goevent");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if(admin.getAccessLevel() < AdminConfig.COMMAND_GOEVENT) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }

        if(admin.isLookingForEvent()) {
            admin.setLookingForEvent(false);
            PacketSendUtility.sendMessage(admin, "You are no longer waiting for event.");
        }
        else {
            admin.setLookingForEvent(true);
            PacketSendUtility.sendMessage(admin, "You are waiting for the event.");
        }
    }
}
