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
import gameserver.model.gameobjects.Gatherable;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Nemiroff
 *         Date: 28.12.2009
 */
public class Info extends AdminCommand {

    public Info() {
        super("info");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_INFO) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        VisibleObject target = admin.getTarget();

        if (target == null || target.equals(admin)) {
            PacketSendUtility.sendMessage(admin, "Your object id is : " + admin.getObjectId());
        } else {
            if (target instanceof Npc) {
                Npc npc = (Npc) admin.getTarget();
                PacketSendUtility.sendMessage(admin, "[Info about npc]\n" + "Name: " + npc.getName() + "\nId: " + npc.getNpcId() + " / ObjectId: " + admin.getTarget().getObjectId() + "\nX: " + admin.getTarget().getX() + " / Y: " + admin.getTarget().getY() + " / Z: " + admin.getTarget().getZ() + " / Heading: " + admin.getTarget().getHeading() + " / Title: " + npc.getObjectTemplate().getTitleId());
            } else if (target instanceof Gatherable) {
                Gatherable gather = (Gatherable) target;
                PacketSendUtility.sendMessage(admin, "[Info about gather]\n" + "Name: " + gather.getName() + "\nId: " + gather.getObjectTemplate().getTemplateId() + " / ObjectId: " + admin.getTarget().getObjectId() + "\nX: " + admin.getTarget().getX() + " / Y: " + admin.getTarget().getY() + " / Z: " + admin.getTarget().getZ() + " / Heading: " + admin.getTarget().getHeading());
            }
        }
    }
}
