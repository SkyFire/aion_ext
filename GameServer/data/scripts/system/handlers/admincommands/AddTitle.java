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
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.world.World;

/**
 * @author xavier
 */
public class AddTitle extends AdminCommand {

    public AddTitle() {
        super("addtitle");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_ADDTITLE) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }

        if ((params.length < 1) || (params.length > 3)) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDTITLE_SYNTAX));
            return;
        }

        int titleId = Integer.parseInt(params[0]);

        if ((titleId > 50) || (titleId < 1)) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDTITLE_TITLE_INVALID, titleId));
            return;
        }

        Player target = null;
        boolean special = false;

        if (params.length >= 2) {
            target = World.getInstance().findPlayer(Util.convertName(params[1]));

            if (target == null) {
                if (params[1].equalsIgnoreCase("special")) {
                    special = true;
                } else {
                    PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.PLAYER_NOT_ONLINE, Util.convertName(params[1])));
                    return;
                }
            } else {
                if (params.length == 3 && params[2].equalsIgnoreCase("special")) {
                    special = true;
                }
            }
        }

        if (target == null) {
            VisibleObject o = admin.getTarget();

            if ((o == null) || !(o instanceof Player)) {
                target = admin;
            }
        }

        if (!special) {
            titleId = target.getCommonData().getRace().getRaceId() * 50 + titleId;
        } else {
            titleId = 100 + titleId;
        }

        if (!target.getTitleList().addTitle(titleId)) {
            if (target.equals(admin)) {
                PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_ME, titleId));
            } else {
                PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_PLAYER, titleId, target.getName()));
            }
        } else {
            if (target.equals(admin)) {
                PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS_ME, titleId));
            } else {
                PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS, target.getName(), titleId));
                PacketSendUtility.sendMessage(target, LanguageHandler.translate(CustomMessageId.COMMAND_ADDTITLE_PLAYER_SUCCESS, admin.getName(), titleId));
            }
        }
    }
}
