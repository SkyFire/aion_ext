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

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.configs.administration.AdminConfig;
import gameserver.dao.PlayerDAO;
import gameserver.dao.PlayerPasskeyDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.loginserver.LoginServer;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author acu77
 */
public class PasskeyReset extends AdminCommand {
    public PasskeyReset() {
        super("passkeyreset");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_PASSKEY_RESET) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        if (params == null || params.length < 1) {
            PacketSendUtility.sendMessage(admin, "syntax: //passkeyreset <player> <passkey>");
            return;
        }

        String name = Util.convertName(params[0]);
        int accountId = DAOManager.getDAO(PlayerDAO.class).getAccountIdByName(name);
        if (accountId == 0) {
            PacketSendUtility.sendMessage(admin, "player " + name + " can't find!");
            PacketSendUtility.sendMessage(admin, "syntax: //passkeyreset <player> <passkey>");
            return;
        }

        try {
            Integer.parseInt(params[1]);
        }
        catch (NumberFormatException e) {
            PacketSendUtility.sendMessage(admin, "parameters should be number!");
            return;
        }

        String newPasskey = params[1];
        if (!(newPasskey.length() > 5 && newPasskey.length() < 9)) {
            PacketSendUtility.sendMessage(admin, "passkey is 6~8 digits!");
            return;
        }

        DAOManager.getDAO(PlayerPasskeyDAO.class).updateForcePlayerPasskey(accountId, newPasskey);
        LoginServer.getInstance().sendBanPacket((byte) 2, accountId, "", -1, admin.getObjectId());
    }
}
