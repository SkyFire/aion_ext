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
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

/**
 * @author Phantom, ZeroSignal
 */
public class DelSkill extends AdminCommand {

    public DelSkill() {
        super("delskill");
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.utils.chathandlers.AdminCommand#executeCommand(com.aionemu.gameserver.model.gameobjects.player.Player, java.lang.String[])
      */

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_DELSKILL) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }

        if (params.length != 1) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_DELSKILL_SYNTAX));
            return;
        }

        VisibleObject target = admin.getTarget();
        int skillId = 0;

        try {
            skillId = Integer.parseInt(params[0]);
        }
        catch (NumberFormatException e) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.INTEGER_PARAMETER_REQUIRED));
            return;
        }

        if (target instanceof Player) {
            Player player = (Player) target;
            player.getSkillList().removeSkill(player, skillId, true);
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_DELSKILL_ADMIN_SUCCESS, skillId, player.getName()));
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_DELSKILL_PLAYER_SUCCESS, admin.getName(), skillId));
        }
    }
}
