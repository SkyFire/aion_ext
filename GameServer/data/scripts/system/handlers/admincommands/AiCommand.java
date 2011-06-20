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
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author ATracer
 */
public class AiCommand extends AdminCommand {
    public AiCommand() {
        super("ai");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_AI) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        if (params == null || params.length < 1) {
            PacketSendUtility.sendMessage(admin, "syntax //ai info");
            return;
        }

        VisibleObject target = admin.getTarget();

        if (target == null || !(target instanceof Npc)) {
            PacketSendUtility.sendMessage(admin, "Select a target first (Npc only)");
            return;
        }

        Npc npc = (Npc) target;

        if (params[0].equals("info")) {
            PacketSendUtility.sendMessage(admin, "Ai state: " + npc.getAi().getAiState());
            PacketSendUtility.sendMessage(admin, "Ai desires size: " + npc.getAi().desireQueueSize());
            PacketSendUtility.sendMessage(admin, "Ai task scheduled: " + npc.getAi().isScheduled());
        }
    }
}
