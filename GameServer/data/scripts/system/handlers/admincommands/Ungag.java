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
import gameserver.model.TaskId;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

import java.util.concurrent.Future;

/**
 * @author Watson
 */
public class Ungag extends AdminCommand {
    public Ungag() {
        super("ungag");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_GAG) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
            return;
        }

        if (params == null || params.length < 1) {
            PacketSendUtility.sendMessage(admin, "Syntax: //ungag <player name>");
            return;
        }

        String name = Util.convertName(params[0]);
        Player player = World.getInstance().findPlayer(name);
        if (player == null) {
            PacketSendUtility.sendMessage(admin, "Player " + name + " was not found!");
            PacketSendUtility.sendMessage(admin, "Syntax: //ungag <player name>");
            return;
        }

        player.setGagged(false);
        Future<?> task = player.getController().getTask(TaskId.GAG);
        if (task != null)
            player.getController().cancelTask(TaskId.GAG);
        PacketSendUtility.sendMessage(player, "Your chat ban punishment is over");
        PacketSendUtility.sendMessage(admin, "Player " + name + " chat ban has been lifted");
    }
}
