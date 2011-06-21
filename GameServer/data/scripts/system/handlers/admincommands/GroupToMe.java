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

import java.util.Collection;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.PlayerGroup;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

/*
 * @author Source, PZIKO333
 */

public class GroupToMe extends AdminCommand
{
/*
 * Constructor.
 */
	public GroupToMe()
	{
		super("grouptome");
	}

	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_GROUPTOME)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params == null || params.length < 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //grouptome <player>");
			return;
		}

		Player groupToMove = World.getInstance().findPlayer(Util.convertName(params[0]));
		if (groupToMove == null)
		{
			PacketSendUtility.sendMessage(admin, "The player is not online.");
			return;
		}

		if(!groupToMove.isInGroup())
		{
			PacketSendUtility.sendMessage(admin, groupToMove.getName() + " is not in group.");
			return;
		}

		Collection<Player> players = World.getInstance().getPlayers();
		for(Player player : World.getInstance().getPlayers())
		{
			if (player.getPlayerGroup() == groupToMove.getPlayerGroup())
				if (player != admin)
				{
					TeleportService.teleportTo(player, admin.getWorldId(), admin.getInstanceId(), admin.getX(), admin.getY(), admin.getZ(), admin.getHeading(), 0);
					PacketSendUtility.sendMessage(player, "You have been summoned by " + admin.getName() + ".");
					PacketSendUtility.sendMessage(admin, "You summon " + player.getName() + ".");
				}
		}
	}
}
