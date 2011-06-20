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
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.SkillListEntry;
import gameserver.model.group.PlayerGroup;
import gameserver.model.legion.Legion;
import gameserver.model.legion.LegionMemberEx;
import gameserver.services.LegionService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author lyahim
 */
public class PlayerInfo extends AdminCommand {

    private final static int showLineNumber = 20;

    public PlayerInfo() {
        super("playerinfo");
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.utils.chathandlers.AdminCommand#executeCommand(com.aionemu.gameserver.model.gameobjects.player.Player, java.lang.String[])
      */

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_PLAYERINFO) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        if (params == null || params.length < 1) {
            PacketSendUtility.sendMessage(admin, "syntax //playerinfo <player name> <loc | item | group | skill | legion> ");
            return;
        }

        Player target = World.getInstance().findPlayer(Util.convertName(params[0]));

        if (target == null) {
            PacketSendUtility.sendMessage(admin, "Selected player is not online!");
            return;
        }

        //PacketSendUtility.sendMessage(admin, "\n[Info about " + target.getName() + "]\n-common: lv" + target.getLevel() + "(" + target.getCommonData().getExpShown()+ " xp), " + target.getCommonData().getRace() + ", " + target.getPlayerClass() +"\n-ip: " + target.getClientConnection().getSource() + "\n" + "-account name: " + target.getClientConnection().getAccount().getName() + "\n" + "-online: ~" + (System.currentTimeMillis() - target.getCommonData().getLastOnline().getTime())/60000 + " minute(s)\n");
        PacketSendUtility.sendMessage(admin, "\n[Info about " + target.getName() + "]\n-common: lv" + target.getLevel() + "(" + target.getCommonData().getExpShown() + " xp), " + target.getCommonData().getRace() + ", " + target.getPlayerClass() + "\n-ip: " + target.getClientConnection().getIP() + "\n" + "-account name: " + target.getClientConnection().getAccount().getName() + "\n" + "-online: ~" + (System.currentTimeMillis() - target.getCommonData().getLastOnline().getTime()) / 60000 + " minute(s)\n");

        if (params.length < 2)
            return;

        if (params[1].equals("item")) {
            StringBuilder strbld = new StringBuilder("-items in inventory:\n");

            List<Item> items = target.getInventory().getAllItems();
            Iterator<Item> it = items.iterator();

            if (items.isEmpty())
                strbld.append("none\n");
            else {
                while (it.hasNext()) {

                    Item act = (Item) it.next();
                    strbld.append("    " + act.getItemCount() + "(s) of " + "[item:" + act.getItemTemplate().getTemplateId() + "]" + "\n");
                }
            }
            items.clear();
            items = target.getEquipment().getEquippedItems();
            it = items.iterator();
            strbld.append("-equipped items:\n");
            if (items.isEmpty())
                strbld.append("none\n");
            else {
                while (it.hasNext()) {
                    Item act = (Item) it.next();
                    strbld.append("    " + act.getItemCount() + "(s) of " + "[item:" + act.getItemTemplate().getTemplateId() + "]" + "\n");
                }
            }

            items.clear();
            items = target.getWarehouse().getAllItems();
            it = items.iterator();
            strbld.append("-items in warehouse:\n");
            if (items.isEmpty())
                strbld.append("none\n");
            else {
                while (it.hasNext()) {
                    Item act = (Item) it.next();
                    strbld.append("    " + act.getItemCount() + "(s) of " + "[item:" + act.getItemTemplate().getTemplateId() + "]" + "\n");
                }
            }
            showAllLines(admin, strbld.toString());
        } else if (params[1].equals("group")) {
            StringBuilder strbld = new StringBuilder("-group info:\n  Leader: ");

            PlayerGroup group = target.getPlayerGroup();
            if (group == null)
                PacketSendUtility.sendMessage(admin, "-group info: no group");
            else {
                Iterator<Player> it = group.getMembers().iterator();

                strbld.append(group.getGroupLeader().getName() + "\n  Members:\n");
                while (it.hasNext()) {
                    Player act = (Player) it.next();
                    strbld.append("    " + act.getName() + "\n");
                }
                PacketSendUtility.sendMessage(admin, strbld.toString());
            }
        } else if (params[1].equals("skill")) {
            StringBuilder strbld = new StringBuilder("-list of skills:\n");

            SkillListEntry sle[] = target.getSkillList().getAllSkills();

            for (int i = 0; i < sle.length; i++) {
                strbld.append("    level " + sle[i].getSkillLevel() + " of " + sle[i].getSkillName() + "\n");
            }
            showAllLines(admin, strbld.toString());
        } else if (params[1].equals("loc")) {
            PacketSendUtility.sendMessage(admin, "-location:\n  mapid: " + target.getWorldId() + "\n  X: " + target.getX() + " Y: " + target.getY() + "Z: " + target.getZ() + "heading: " + target.getHeading());
        } else if (params[1].equals("legion")) {
            StringBuilder strbld = new StringBuilder();

            Legion legion = target.getLegion();
            if (legion == null)
                PacketSendUtility.sendMessage(admin, "-legion info: no legion");
            else {
                ArrayList<LegionMemberEx> legionmemblist = LegionService.getInstance().loadLegionMemberExList(legion);
                Iterator<LegionMemberEx> it = legionmemblist.iterator();

                strbld.append("-legion info:\n  name: " + legion.getLegionName() + ", level: " + legion.getLegionLevel() + "\n  members(online):\n");
                while (it.hasNext()) {
                    LegionMemberEx act = (LegionMemberEx) it.next();
                    strbld.append("    " + act.getName() + "(" + ((act.isOnline() == true) ? "online" : "offline") + ")" + act.getRank().toString() + "\n");
                }
            }
            showAllLines(admin, strbld.toString());
        } else {
            PacketSendUtility.sendMessage(admin, "bad switch!");
            PacketSendUtility.sendMessage(admin, "syntax //playerinfo <player name> <loc | item | group | skill | legion> ");
        }
    }

    private void showAllLines(Player admin, String str) {
        int index = 0;
        String[] strarray = str.split("\n");

        while (index < strarray.length - showLineNumber) {
            StringBuilder strbld = new StringBuilder();
            for (int i = 0; i < showLineNumber; i++, index++) {
                strbld.append(strarray[index]);
                if (i < showLineNumber - 1) strbld.append("\n");
            }
            PacketSendUtility.sendMessage(admin, strbld.toString());
        }
        int odd = strarray.length - index;
        StringBuilder strbld = new StringBuilder();
        for (int i = 0; i < odd; i++, index++) {
            strbld.append(strarray[index] + "\n");
        }
        PacketSendUtility.sendMessage(admin, strbld.toString());
    }
}
