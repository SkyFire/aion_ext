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
package quest.cloister_of_kaisinel;

import java.util.Collections;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

/**
 * @author dta3000
 * 
 */
public class _10000LavirintosCall extends QuestHandler
{
	private final static int	questId	= 10000;

	public _10000LavirintosCall()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(203701).addOnTalkEvent(questId);
		qe.setNpcQuestData(798600).addOnTalkEvent(questId);	// eremitia
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs != null)
			return false;
		if(player.getCommonData().getLevel() < 50)
			return false;
		env.setQuestId(questId);
		QuestService.startQuest(env, QuestStatus.START);
		return true;
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if (qs == null) return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 798600)
			{
				return defaultQuestEndDialog(env);
			}
		}		
		else if (qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 203701)
			{
				switch(env.getDialogId())
				{
					case 25:
						if(var == 0)
							return sendQuestDialog(env, 1011);
					case 10255:
						if(var == 0)
						{
							ItemService.addItems(player, Collections.singletonList(new QuestItems(182206300, 1)));
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);  
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					return false;
				}
			}
		}
		else if (targetId == 798600)
			{
				switch(env.getDialogId())
				{
					case 25:
						if(var == 0)
							return sendQuestDialog(env, 10002);
					case 1009:
						if(var == 0)
							return sendQuestDialog(env, 5);
					case 17:
						{
							int rewardExp = player.getRates().getQuestXpRate() * 1093800;
							int rewardKinah = player.getRates().getQuestXpRate() * 527480;
							ItemService.addItems(player, Collections.singletonList(new QuestItems(182400001, rewardKinah)));
							player.getCommonData().addExp(rewardExp);
							qs.setStatus(QuestStatus.COMPLETE);
							qs.setCompliteCount(1);
        						updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(questId, QuestStatus.COMPLETE, 2));
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							return true;
						}
				}
			}
		return false;
	}	
}