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
package quest.heiron;

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
 * @author Balthazar @modifie Leunam
 */
 
public class _1644AVeryOldLetter extends QuestHandler {
	private final static int questId = 1644;

	public _1644AVeryOldLetter() {
		super(questId);
	}

	@Override
	public void register() {
        	qe.setNpcQuestData(204545).addOnTalkEvent(questId);
		qe.setNpcQuestData(204537).addOnTalkEvent(questId);
		qe.setNpcQuestData(204546).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
	final Player player = env.getPlayer();
	int targetId = 0;
	final QuestState qs = player.getQuestStateList().getQuestState(questId);
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();

	if (qs == null || qs.getStatus() == QuestStatus.NONE) {
		if (env.getDialogId() == 1002) {	
			QuestService.startQuest(env, QuestStatus.START);
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
			return true;
            } else
                	PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
	} 
	if(qs == null)
	return false;
		
	int var = qs.getQuestVarById(0);
	if(qs.getStatus() == QuestStatus.REWARD)
	{
		if(targetId == 204546)
		{
			return defaultQuestEndDialog(env);
		}
	}
	else if(qs.getStatus() != QuestStatus.START)
	{
		return false;
	}
		else if(targetId == 204545)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 0)
						return sendQuestDialog(env, 1352);
					else 	if(var == 2)
						return sendQuestDialog(env, 2034);
				case 10000:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);	
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						return true;
					}
				case 10002:
					if(var == 2)
					{
						qs.setQuestVarById(0, var + 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);	
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						return true;
					}
				return false;
			}
		}
		else if(targetId == 204537)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 1)
						return sendQuestDialog(env, 1693);
				case 10001:
					if(var == 1)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);	
						player.getInventory().removeFromBagByItemId(182201765, 1);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						return true;
					}
				return false;
			}
		}
		else if(targetId == 204546)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 3)
						return sendQuestDialog(env, 2375);
				case 1009:
					if(var == 3)
						return sendQuestDialog(env, 5);
				case 17:
					{
						int rewardExp = player.getRates().getQuestXpRate() * 758600;
						int rewardKinah = player.getRates().getQuestXpRate() * 18200;
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
