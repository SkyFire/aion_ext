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

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.configs.administration.AdminConfig;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Friend;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.network.aion.serverpackets.SM_LEGION_UPDATE_MEMBER;
import gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.PlayerService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

import java.util.Iterator;

/**
 * @author xTz
 *
 */

public class Rename extends AdminCommand
{
    public Rename()
    {
        super("rename");
    }

    @Override
    public void executeCommand(Player admin, String[] params)
    {
        if(admin.getAccessLevel() < AdminConfig.COMMAND_RENAME)
        {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        if(params.length < 1 || params.length > 2)
        {
            PacketSendUtility.sendMessage(admin, "No parameters detected.\n"
                    + "Please use //rename <Player name> <rename>\n"
                    + "or use //rename [target] <rename>");
            return;
        }

        String recipient = null;
        String rename = null;

        if(params.length == 2)
        {
            recipient = Util.convertName(params[0]);
            rename = Util.convertName(params[1]);

            if(!DAOManager.getDAO(PlayerDAO.class).isNameUsed(recipient))
            {
                PacketSendUtility.sendMessage(admin, "Could not find a Player by that name.");
                return;
            }
            final Player player = World.getInstance().findPlayer(Util.convertName(params[0]));

            if(!check(admin, rename))
                return;


            player.getCommonData().setName(rename);
            DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
            if(player.getCommonData().isOnline())
            {
                PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
                sendPacket(admin, player, rename, recipient);
            }
            else
                PacketSendUtility.sendMessage(admin, "Player " + recipient + " has been renamed to " + rename);
        }
        if(params.length == 1)
        {
            rename = Util.convertName(params[0]);

            VisibleObject target = admin.getTarget();
            if(target == null)
            {
                PacketSendUtility.sendMessage(admin, "You should select a target first!");
                return;
            }

            if(target instanceof Player)
            {
                final Player player = (Player) target;
                if(!check(admin, rename))
                    return;

                player.getCommonData().setName(rename);
                PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
                DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
                recipient = (String) target.getName();
                sendPacket(admin, player, rename, recipient);
            } else {
                PacketSendUtility.sendMessage(admin, "The command can be applied only on the player.");
                return;
            }

        }
    }

    private static boolean check(Player admin, String rename)
    {
        if(!PlayerService.isValidName(rename))
        {
            PacketSendUtility.sendPacket(admin, new SM_SYSTEM_MESSAGE(1400151));
            return false;
        }
        if(!PlayerService.isFreeName(rename))
        {
            PacketSendUtility.sendPacket(admin, new SM_SYSTEM_MESSAGE(1400155));
            return false;
        }
        return true;
    }

    public void sendPacket(Player admin, Player player, String rename, String recipient)
    {
        Iterator<Friend> knownFriends = player.getFriendList().iterator();

        while(knownFriends.hasNext())
        {
            Friend nextObject = knownFriends.next();
            if(nextObject.getPlayer() != null && nextObject.getPlayer().isOnline())
            {
                PacketSendUtility.sendPacket(nextObject.getPlayer(), new SM_PLAYER_INFO(player, false));
            }
        }

        if(player.isLegionMember())
        {
            PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_UPDATE_MEMBER(player, 0, ""));
        }
        PacketSendUtility.sendMessage(player, "You have been renamed to " + rename);
        PacketSendUtility.sendMessage(admin, "Player " + recipient + " has been renamed to " + rename);
    }
}
