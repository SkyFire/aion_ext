/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.eltnen;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.Storage;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Sylar
 * 
 */
public class _1033SatalocasHeart extends QuestHandler
{

	private final static int	questId	= 1033;

	public _1033SatalocasHeart()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203900).addOnTalkEvent(questId); //Diomedes
		qe.setNpcQuestData(203996).addOnTalkEvent(questId); //Kimeia
		qe.setQuestMovieEndIds(42).add(questId);
		qe.addOnQuestTimerEnd(questId);
		qe.addQuestLvlUp(questId);
	}

	@Override
	public boolean onMovieEndEvent(QuestCookie env, int movieId)
	{
		if(movieId != 42)
			return false;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START || 
		   qs.getQuestVarById(0) != 1 || player.getQuestTimerOn())
			return false;

		// remove all items, so we count from the beginning
		Storage inventory = player.getInventory();
		long fangCount = inventory.getItemCountByItemId(182201019);
		inventory.removeFromBagByItemId(182201019, fangCount);
		QuestService.questTimerStart(env, 180);
		return true;
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		
		if(env.getTargetId() == 203900) //Diomedes
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 10000)
					return defaultCloseDialog(env, 0, 1);
			}
			else if(qs.getStatus() == QuestStatus.REWARD)
			{
				if(env.getDialogId() == -1)
				{
					if (qs.getCompleteCount() == 1)
						return sendQuestDialog(env, 2375);
					else
						return sendQuestDialog(env, 2716);
				}
				else
				{
					int reward = qs.getCompleteCount() == 1 ? 0 : 1;
					return defaultQuestEndDialog(env, reward);
				}
			}
		}
		else if(env.getTargetId() == 203996) //Kimeia
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1 && qs.getCompleteCount() == 0)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1693);
				else if(env.getDialogId() == 10002)
				{
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
					if (!player.getQuestTimerOn())
						defaultQuestMovie(env, 42);
					return true;
				}
			}
			else if(qs.getStatus() == QuestStatus.START && qs.getCompleteCount() == 1)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2034);
				else if (env.getDialogId() == 2035)
				{
					Storage inventory = player.getInventory();
					long fangCount = inventory.getItemCountByItemId(182201019);
					if (fangCount < 4)
					{
						qs.setCompliteCount(2);  // allow to repeat
						updateQuestStatus(env);
						return sendQuestDialog(env, 2035);
					}
					else
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						if (fangCount < 10)
						{
							qs.setCompliteCount(2); // average results
							return sendQuestDialog(env, 2120);
						}
						else
							return sendQuestDialog(env, 2205);
					}
				}
			}
			else if(qs.getStatus() == QuestStatus.START && qs.getCompleteCount() == 2)
			{
				if(env.getDialogId() == 26)
				{
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
					if (!player.getQuestTimerOn())
						defaultQuestMovie(env, 42);
					return true;
				}
			}
			else if(qs.getStatus() == QuestStatus.START && qs.getCompleteCount() > 2)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2034);
				else if (env.getDialogId() == 2035)
				{
					Storage inventory = player.getInventory();
					long fangCount = inventory.getItemCountByItemId(182201019);
					if (fangCount < 4)
					{
						qs.setCompliteCount(2);  // allow to repeat
						updateQuestStatus(env);
						return sendQuestDialog(env, 2035);
					}
					else
					{
						qs.setStatus(QuestStatus.REWARD);
						qs.setCompliteCount(2); // average results
						updateQuestStatus(env);
						return sendQuestDialog(env, 2205);
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onQuestTimerEndEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		
		int completeCount = qs.getCompleteCount() + 1;
		qs.setCompliteCount(completeCount);
		updateQuestStatus(env);
		return true;
	}

}
