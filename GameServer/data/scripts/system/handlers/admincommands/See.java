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
import gameserver.model.gameobjects.state.CreatureSeeState;
import gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
* @author Altrec
*
*/

public class See extends AdminCommand
{
	public See()
	{
		super("see");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_SEE)
		{
			PacketSendUtility.sendMessage(admin, "You don't have enough rights to execute this command.");
			return;
		}
		
		if (admin.getSeeState() < 2)
		{
			admin.setSeeState(CreatureSeeState.SEARCH2);
			PacketSendUtility.broadcastPacket(admin, new SM_PLAYER_STATE(admin), true);
			PacketSendUtility.sendMessage(admin, "You can see through hide.");
		}
		else
		{
			admin.unsetSeeState(CreatureSeeState.SEARCH2);
			PacketSendUtility.broadcastPacket(admin, new SM_PLAYER_STATE(admin), true);
			PacketSendUtility.sendMessage(admin, "You cannot see through hide.");
		}
	}
}
