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
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

/**
 * Admin movetoplayer command.
 *
 * @author Tanelorn
 */

public class MoveToPlayer extends AdminCommand {

    /**
     * Constructor.
     */

    public MoveToPlayer() {
        super("movetoplayer");
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVETOPLAYER) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        if (params == null || params.length < 1) {
            PacketSendUtility.sendMessage(admin, "syntax //movetoplayer <player name>");
            return;
        }

        Player player = World.getInstance().findPlayer(Util.convertName(params[0]));

        if (player == null) {
            PacketSendUtility.sendMessage(admin, "The specified player is not online.");
            return;
        }

        if (player == admin) {
            PacketSendUtility.sendMessage(admin, "Cannot use this command on yourself.");
            return;
        }

        TeleportService.teleportTo(admin, player.getWorldId(), player.getInstanceId(), player.getX(), player.getY(), player.getZ(), player.getHeading(), 0);
        PacketSendUtility.sendMessage(admin, "Teleported to player " + player.getName() + ".");
    }
}
