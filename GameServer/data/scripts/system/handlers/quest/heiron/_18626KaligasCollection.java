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

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.PlayerClass;
import gameserver.model.templates.QuestTemplate;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

/**
 * @author Leunam
 *
 */
public class _18626KaligasCollection extends QuestHandler {
	private final static int questId = 18626;

	public _18626KaligasCollection() {
		super(questId);
	}

	@Override
	public void register() {
		qe.setNpcQuestData(730334).addOnQuestStart(questId); //Lacchus
		qe.setNpcQuestData(730334).addOnActionItemEvent(questId);
		qe.setNpcQuestData(730334).addOnTalkEvent(questId);
	}

	@Override
	public boolean onActionItemEvent(QuestCookie env) {
		if(env.getPlayer().getTribe().equals("PC"))
		{
			int targetId = 0;
			if (env.getVisibleObject() instanceof Npc)
				targetId = ((Npc) env.getVisibleObject()).getNpcId();
			return (targetId == 730334);
		} else {
			return false;
		}
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
		if(env.getPlayer().getTribe().equals("PC"))
		{
			final Player player = env.getPlayer();
			int targetId = 0;
			if (env.getVisibleObject() instanceof Npc)
				targetId = ((Npc) env.getVisibleObject()).getNpcId();
			QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (targetId == 730334) 
			{
				PlayerClass playerClass = player.getCommonData().getPlayerClass();
				if (playerClass == PlayerClass.SORCERER || playerClass == PlayerClass.SPIRIT_MASTER || playerClass == PlayerClass.MAGE || playerClass == PlayerClass.PRIEST || playerClass == PlayerClass.WARRIOR || playerClass == PlayerClass.SCOUT) 
				{
					if (qs == null || qs.getStatus() == QuestStatus.NONE || (qs.getStatus() == QuestStatus.COMPLETE && (qs.getCompliteCount() <= template.getMaxRepeatCount()))) 
					{
						if (env.getDialogId() == -1)
							return sendQuestDialog(env, 1011);
						else
							return defaultQuestStartDialog(env);
					}  
					else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) 
					{
						if (env.getDialogId() == -1)
							return sendQuestDialog(env, 2375);
						else if (env.getDialogId() == 33) 
						{
							if (player.getInventory().getItemCountByItemId(185000102) >= 1) 
							{
								player.getInventory().removeFromBagByItemId(185000102, 1);
								qs.setStatus(QuestStatus.REWARD);
								qs.setQuestVar(1);
								qs.setCompliteCount(0);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
							} 
							else
								return sendQuestDialog(env, 2716);
						}
					}  
					else if (qs != null && qs.getStatus() == QuestStatus.REWARD) 
					{
						int var = qs.getQuestVarById(0);
						switch (env.getDialogId()) 
						{
							case -1:
								if (var == 1)
									return sendQuestDialog(env, 5);
							case 17:
								QuestService.questFinish(env, qs.getQuestVars().getQuestVars() - 1);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
						}
					}
				}
				return false;
			}
			return false;
		} else {
			return false;
		}
	}
}
