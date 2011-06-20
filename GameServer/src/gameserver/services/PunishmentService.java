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
package gameserver.services;

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.dao.PlayerPunishmentsDAO;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldMapType;

import java.util.concurrent.Future;

/**
 * @author lord_rex
 */
public class PunishmentService
{

	/**
	 * This method will handle moving or removing a player from prison
	 * 
	 * @param player
	 * @param state
	 * @param delayInMinutes
	 */
	public static void setIsInPrison(Player player, boolean state, long delayInMinutes)
	{
		stopPrisonTask(player, false);
		if(state)
		{
			long prisonTimer = player.getPrisonTimer();
			if(delayInMinutes > 0)
			{
				prisonTimer = delayInMinutes * 60000L;
				schedulePrisonTask(player, prisonTimer);
				PacketSendUtility.sendMessage(player, "You are in prison for " + delayInMinutes
					+ " minutes.\nIf you disconnect, the countdown will be stopped.");
			}

			player.setStartPrison(System.currentTimeMillis());
			TeleportService.teleportToPrison(player);
			DAOManager.getDAO(PlayerPunishmentsDAO.class).punishPlayer(player, 1);
		}
		else
		{
			PacketSendUtility.sendMessage(player, "You have been removed from prison!");
			player.setPrisonTimer(0);

			TeleportService.moveToBindLocation(player, true);

			DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(player);
		}
	}

	/**
	 * This method will stop the prison task
	 * 
	 * @param playerObjId
	 */
	public static void stopPrisonTask(Player player, boolean save)
	{
		Future<?> prisonTask = player.getController().getTask(TaskId.PRISON);
		if(prisonTask != null)
		{
			if(save)
			{
				long delay = player.getPrisonTimer();
				if(delay < 0)
					delay = 0;
				player.setPrisonTimer(delay);
			}
			player.getController().cancelTask(TaskId.PRISON);
		}
	}

	/**
	 * This method will update the prison status
	 * 
	 * @param player
	 */
	public static void updatePrisonStatus(Player player)
	{
		if(player.isInPrison())
		{
			long prisonTimer = player.getPrisonTimer();
			if(prisonTimer > 0)
			{
				schedulePrisonTask(player, prisonTimer);
				int timeInPrison = Math.round(prisonTimer / 60000);

				if(timeInPrison <= 0)
					timeInPrison = 1;

				PacketSendUtility.sendMessage(player, "You will be in prison for " + timeInPrison + " minute"
					+ (timeInPrison > 1 ? "s" : "") + ".");

				player.setStartPrison(System.currentTimeMillis());
			}

			switch(player.getCommonData().getRace())
			{
				case ELYOS:
				{
					if(player.getWorldId() != WorldMapType.LF_PRISON.getId())
						TeleportService.teleportToPrison(player);
					break;
				}
				case ASMODIANS:
				{
					if(player.getWorldId() != WorldMapType.DF_PRISON.getId())
						TeleportService.teleportToPrison(player);
					break;
				}
			}
		}
	}

	/**
	 * This method will schedule a prison task
	 * 
	 * @param player
	 * @param prisonTimer
	 */
	private static void schedulePrisonTask(final Player player, long prisonTimer)
	{
		player.setPrisonTimer(prisonTimer);
		player.getController().addTask(TaskId.PRISON, ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				setIsInPrison(player, false, 0);
				player.unbanFromWorld();
			}
		}, prisonTimer));
	}
}
