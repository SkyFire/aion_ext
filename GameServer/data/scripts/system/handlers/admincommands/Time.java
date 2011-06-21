/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_GAME_TIME;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.gametime.GameTimeManager;
import gameserver.world.World;

/**
 * @author Pan, PZIKO333
 */
public class Time extends AdminCommand {

	/**
	 * Changes in-game time
	 */
	public Time() {
		super("time");
	}

	@Override
	public void executeCommand(Player admin, String[] params) {

		if (admin.getAccessLevel() < AdminConfig.COMMAND_TIME) {
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		String syntax = "Syntax: //time < dawn | day | dusk | night | desired hour (number) >";

		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(admin, syntax);
			return;
		}

		// Getting current hour and minutes
		int time = GameTimeManager.getGameTime().getHour();
		int min = GameTimeManager.getGameTime().getMinute();
		int hour;

		// If the given param is one of these four, get the correct hour...
		if (params[0].equals("night")) {
			hour = 22;
		}
		else if (params[0].equals("dusk")) {
			hour = 18;
		}
		else if (params[0].equals("day")) {
			hour = 9;
		}
		else if (params[0].equals("dawn")) {
			hour = 4;
		}
		else {
			// If not, check if the param is a number (hour)...
			try {
				hour = Integer.parseInt(params[0]);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, syntax);
				return;
			}

			// A day have only 24 hours!
			if (hour < 0 || hour > 23) {
				PacketSendUtility.sendMessage(admin, syntax);
				PacketSendUtility.sendMessage(admin, "A day have only 24 hours!\n" + "Min value : 0 - Max value : 23");
				return;
			}
		}

		// Calculating new time in minutes...
		time = hour - time;
		time = GameTimeManager.getGameTime().getTime() + (60 * time) - min;

		// Reloading the time, restarting the clock...
		GameTimeManager.reloadTime(time);

		// Checking the new daytime
		GameTimeManager.getGameTime().analyzeDayTime();
		for (Player player : World.getInstance().getPlayers()) {
			PacketSendUtility.sendPacket(player, new SM_GAME_TIME());
		}

		PacketSendUtility.sendMessage(admin, "You changed the time to " + params[0].toString() + ".");
	}
}
