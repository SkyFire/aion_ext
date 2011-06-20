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
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.WorldMapType;

/**
 * Admin moveto command
 *
 * @author KID
 */

public class MoveTo extends AdminCommand {

    /**
     * Constructor.
     */

    public MoveTo() {
        super("moveto");
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVETO) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        if (params == null || params.length < 4) {
            PacketSendUtility.sendMessage(admin, "syntax //moveto <world Id> <X> <Y> <Z>");
            return;
        }

        int worldId;
        float x, y, z;

        try {
            worldId = Integer.parseInt(params[0]);
            x = Float.parseFloat(params[1]);
            y = Float.parseFloat(params[2]);
            z = Float.parseFloat(params[3]);
        }
        catch (NumberFormatException e) {
            PacketSendUtility.sendMessage(admin, "All the parameters should be numbers");
            return;
        }

        if (WorldMapType.getWorld(worldId) == null) {
            PacketSendUtility.sendMessage(admin, "Illegal WorldId %d " + worldId);
        } else {
            TeleportService.teleportTo(admin, worldId, x, y, z, 0);
            PacketSendUtility.sendMessage(admin, "Teleported to " + x + " " + y + " " + z + " [" + worldId + "]");
        }
    }
}
