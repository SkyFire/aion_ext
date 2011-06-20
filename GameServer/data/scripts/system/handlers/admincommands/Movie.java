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
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author kecimis
 */
public class Movie extends AdminCommand {
    public Movie() {
        super("movie");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVIE) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
            return;
        }

        int movieId = 0;
        int type = 0;
        VisibleObject target = admin.getTarget();

        if (target == null || !(target instanceof Player)) {
            target = admin;
        }
        if (params.length == 0) {
            PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
            return;
        }
        if (params.length == 1) {
            try {
                movieId = Integer.valueOf(params[0]);
                PacketSendUtility.sendPacket((Player) target, new SM_PLAY_MOVIE(0, movieId));
            }
            catch (ArrayIndexOutOfBoundsException e) {
                PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "Use numbers only!");
            }
        } else if (params.length == 2) {
            try {
                type = Integer.valueOf(params[0]);
                movieId = Integer.valueOf(params[1]);
                PacketSendUtility.sendPacket((Player) target, new SM_PLAY_MOVIE(type, movieId));
            }
            catch (ArrayIndexOutOfBoundsException e) {
                PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "Use numbers only!");
            }
        }

    }
}