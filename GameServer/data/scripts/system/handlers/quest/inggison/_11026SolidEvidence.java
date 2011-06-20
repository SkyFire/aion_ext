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
package quest.inggison;

import java.util.Collections;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.services.ItemService;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

/**
 * @author dta3000
 *
 */
public class _11026SolidEvidence extends QuestHandler
{

	private final static int	questId	= 11026;

	public _11026SolidEvidence()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798950).addOnQuestStart(questId); 
		qe.setNpcQuestData(798950).addOnTalkEvent(questId); 
		qe.setNpcQuestData(798941).addOnTalkEvent(questId); 
		qe.setNpcQuestData(203384).addOnTalkEvent(questId); 
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 798950) 
			{
				if(env.getDialogId() == 25)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 1002)
				{
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182206719, 1))))
						return defaultQuestStartDialog(env);
					else
						return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		else if(targetId == 798941) 
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				if(env.getDialogId() == 25)
					return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10000)
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					 updateQuestStatus(env);
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		else if(targetId == 203384)
		{		
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1)
			{
				if(env.getDialogId() == 25)
					return sendQuestDialog(env, 2375);
				else if(env.getDialogId() == 1009)
				{
                                                                                           player.getInventory().removeFromBagByItemId(182206719, 1);
					 qs.setStatus(QuestStatus.REWARD);
					 updateQuestStatus(env); 
					return sendQuestDialog(env, 2375);
				}
				else
					return defaultQuestStartDialog(env);
			}
		
			else if(qs != null && qs.getStatus() == QuestStatus.REWARD)
				return defaultQuestEndDialog(env);
		}
		
		return false;
	}
}
