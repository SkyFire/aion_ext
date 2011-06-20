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
 * @author Shepper
 *
 */
public class Admin extends AdminCommand 
{
    private final static CustomMessageId[] commandSyntax = {
        CustomMessageId.COMMAND_ADMIN_PROMOTE_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_REVOKE_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_ADD_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_ADDSKILL_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_DELSKILL_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_GIVEMISSINGSKILLS_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_ADDTITLE_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_ANNOUNCE_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_NOTICE_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_INFO_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_SETLEVEL_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_SETEXP_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_SETTITLE_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_SETCLASS_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_SPEED_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_HEAL_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_KILL_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_KICK_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_UNSTUCK_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_MOVEPLAYERTOPLAYER_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_MOVETOME_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_MOVETOPLAYER_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_MOVETO_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_GOTO_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_SPAWN_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_DELETE_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_SAVESPAWN_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_RELOADSPAWN_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_ADDDROP_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_ZONE_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_RELOADSKILL_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_RELOADQUEST_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_QUEST_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_SYS_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_AI_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_WEATHER_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_LEGION_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_SPRISON_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_RPRISON_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_REMOVECD_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_DISPEL_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_ADDEFFECT_SYNTAX,
        CustomMessageId.COMMAND_ADMIN_DROPLIST_SYNTAX,
    };

    public Admin() 
    {
        super("admin");
    }

    @Override
    public void executeCommand(Player admin, String[] params) 
    {
        if(admin.getAccessLevel() < AdminConfig.COMMAND_ADMIN) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }

        String msg = "|----------------------|-----------------------\n" +
            "|  //commands      |    params                  \n" +
            "|----------------------|-----------------------\n";
        for (int i=0; i < commandSyntax.length; ++i) {
            msg += "| //" + LanguageHandler.translate(commandSyntax[i]) + "\n";
        }
        msg += "|____________________________________________\n";
        PacketSendUtility.sendMessage(admin, msg);
    }
}