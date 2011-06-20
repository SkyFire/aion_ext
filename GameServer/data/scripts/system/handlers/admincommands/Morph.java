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
import gameserver.network.aion.serverpackets.SM_TRANSFORM;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author ATracer
 * @modified By aionchs- Wylovech
 */
public class Morph extends AdminCommand {

    public Morph() {
        super("morph");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_MORPH) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
            return;
        }

        if (params == null || params.length != 1) {
            PacketSendUtility.sendMessage(admin, "syntax //morph <npc id | cancel> ");
            return;
        }

        Player target = admin;
        int param = 0;

        if (admin.getTarget() instanceof Player)
            target = (Player) admin.getTarget();

        if (!("cancel").startsWith(params[0].toLowerCase())) {
            try {
                param = Integer.parseInt(params[0]);
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "Parameter must be an integer, or cancel.");
                return;
            }
        }

        if ((param != 0 && param < 200000) || param > 298021) {
            PacketSendUtility.sendMessage(admin, "Something wrong with the NPC Id!");
            return;
        }

        target.setTransformedModelId(param);
        PacketSendUtility.broadcastPacketAndReceive(target, new SM_TRANSFORM(target));

        if (param == 0) {
            if (target.equals(admin)) {
                PacketSendUtility.sendMessage(target, "Morph cancelled successfully.");
            } else {
                PacketSendUtility.sendMessage(target, "Your morph has been cancelled by Admin " + admin.getName() + ".");
                PacketSendUtility.sendMessage(admin, "You have cancelled " + target.getName() + "'s morph.");
            }
        } else {
            if (target.equals(admin)) {
                PacketSendUtility.sendMessage(target, "Successfully morphed to npc id " + param + ".");
            } else {
                PacketSendUtility.sendMessage(target, admin.getName() + " morphs you into an NPC form.");
                PacketSendUtility.sendMessage(admin, "You morph " + target.getName() + " to npc id " + param + ".");
            }

        }
    }
}
