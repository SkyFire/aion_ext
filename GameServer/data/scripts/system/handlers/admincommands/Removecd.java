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
import gameserver.model.gameobjects.player.SkillListEntry;
import gameserver.network.aion.serverpackets.SM_SKILL_CANCEL;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Belux
 */
public class Removecd extends AdminCommand {
    public Removecd() {
        super("removecd");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_ADD) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        Player target = null;
        VisibleObject creature = admin.getTarget();

        if (admin.getTarget() instanceof Player) {
            target = (Player) creature;
        }

        if (creature == null) {
            PacketSendUtility.sendMessage(admin, "You should select a target first!");
            return;
        }

        for (SkillListEntry skillEntry : target.getSkillList().getAllSkills()) {
            target.removeSkillCoolDown(skillEntry.getSkillId());
            PacketSendUtility.broadcastPacketAndReceive(creature, new SM_SKILL_CANCEL(target, skillEntry.getSkillId()));
        }
    }
}
