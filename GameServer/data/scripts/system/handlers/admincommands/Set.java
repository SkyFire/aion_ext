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
import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_TITLE_INFO;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

import java.util.Arrays;

/**
 * @author Nemiroff, ATracer, IceReaper
 *         Date: 11.12.2009
 * @author Sarynth - Added AP
 */
public class Set extends AdminCommand {

    public Set() {
        super("set");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {

        if (params == null || params.length < 2) {
            PacketSendUtility.sendMessage(admin, "syntax //set <class | exp | ap | level | title>");
            return;
        }

        Player target = null;
        VisibleObject creature = admin.getTarget();

        if (admin.getTarget() instanceof Player) {
            target = (Player) creature;
        }

        if (target == null) {
            PacketSendUtility.sendMessage(admin, "You should select a target first!");
            return;
        }

        if (params[1] == null) {
            PacketSendUtility.sendMessage(admin, "You should enter second parameter!");
            return;
        }
        String paramValue = params[1];

        if (params[0].equals("class")) {
            if (admin.getAccessLevel() < AdminConfig.COMMAND_SETCLASS) {
                PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
                return;
            }

            byte newClass;
            try {
                newClass = Byte.parseByte(paramValue);
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "You should enter valid second parameter!");
                return;
            }

            PlayerClass oldClass = null;
            Player player = null;
            if (target instanceof Player) {
                player = target;
                oldClass = target.getPlayerClass();
            } else {
                player = admin;
                oldClass = admin.getPlayerClass();
            }
            setClass(player, oldClass, newClass);
        } else if (params[0].equals("exp")) {
            if (admin.getAccessLevel() < AdminConfig.COMMAND_SETEXP) {
                PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
                return;
            }

            long exp;
            try {
                exp = Long.parseLong(paramValue);
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "You should enter valid second parameter!");
                return;
            }

            if (target instanceof Player) {
                Player player = target;
                player.getCommonData().setExp(exp);
                PacketSendUtility.sendMessage(admin, "Set your exp to " + paramValue);
            }

        } else if (params[0].equals("ap")) {
            if (admin.getAccessLevel() < AdminConfig.COMMAND_SETAP) {
                PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
                return;
            }

            int ap;
            try {
                ap = Integer.parseInt(paramValue);
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "You should enter valid second parameter!");
                return;
            }

            if (target instanceof Player) {
                Player player = (Player) target;
                player.getCommonData().setAp(ap);
                if (player == admin) {
                    PacketSendUtility.sendMessage(admin, "Set your Abyss Points to " + ap + ".");
                } else {
                    PacketSendUtility.sendMessage(admin, "Set " + player.getName() + " Abyss Points to " + ap + ".");
                    PacketSendUtility.sendMessage(player, "Admin set your Abyss Points to " + ap + ".");
                }
            } else {
                PacketSendUtility.sendMessage(admin, "You must select a Player to set AP.");
            }
        } else if (params[0].equals("level")) {
            if (admin.getAccessLevel() < AdminConfig.COMMAND_SETLEVEL) {
                PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
                return;
            }

            int level;
            try {
                level = Integer.parseInt(paramValue);
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "You should enter valid second parameter!");
                return;
            }

            if (target instanceof Player) {
                Player player = target;
                if (level <= 56)
                    player.getCommonData().setLevel(level);
                PacketSendUtility.sendMessage(admin, "Set " + player.getCommonData().getName() + " level to " + level);
            }
        } else if (params[0].equals("title")) {
            if (admin.getAccessLevel() < AdminConfig.COMMAND_SETTITLE) {
                PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
                return;
            }

            int titleId;
            try {
                titleId = Integer.parseInt(paramValue);
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "You should enter valid second parameter!");
                return;
            }

            if (target instanceof Player) {

                Player player = target;
                if (titleId <= 106)
                    setTitle(player, titleId);
                PacketSendUtility.sendMessage(admin, "Set " + player.getCommonData().getName() + " title to " + titleId);
            }
        }
    }

    private void setTitle(Player player, int value) {
        PacketSendUtility.sendPacket(player, new SM_TITLE_INFO(value));
        PacketSendUtility.broadcastPacket(player, (new SM_TITLE_INFO(player, value)));
        player.getCommonData().setTitleId(value);
    }

    private void setClass(Player player, PlayerClass oldClass, byte value) {
        PlayerClass playerClass = PlayerClass.getPlayerClassById(value);
        int level = player.getLevel();
        if (level < 9) {
            PacketSendUtility.sendMessage(player, "You can only switch class after reach level 9");
            return;
        }
        if (Arrays.asList(1, 2, 4, 5, 7, 8, 10, 11).contains(oldClass.ordinal())) {
            PacketSendUtility.sendMessage(player, "You already switched class");
            return;
        }
        int newClassId = playerClass.ordinal();
        switch (oldClass.ordinal()) {
            case 0:
                if (newClassId == 1 || newClassId == 2)
                    break;
            case 3:
                if (newClassId == 4 || newClassId == 5)
                    break;
            case 6:
                if (newClassId == 7 || newClassId == 8)
                    break;
            case 9:
                if (newClassId == 10 || newClassId == 11)
                    break;
            default:
                PacketSendUtility.sendMessage(player, "Invalid class switch chosen");
                return;
        }
        player.getCommonData().setPlayerClass(playerClass);
        player.getCommonData().upgradePlayer();
        PacketSendUtility.sendMessage(player, "You have successfuly switched class");
    }
}
