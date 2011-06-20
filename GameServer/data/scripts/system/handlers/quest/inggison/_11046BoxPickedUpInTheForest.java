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

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

/**
 * @author Dta3000
 * 
 */
public class _11046BoxPickedUpInTheForest extends QuestHandler
{
	private final static int	questId	= 11046;

	public _11046BoxPickedUpInTheForest()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798954).addOnTalkEvent(questId);
		qe.setQuestItemIds(182206745).add(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if(targetId == 0)
		{
			switch(env.getDialogId())
			{
				case 1002:
					QuestService.startQuest(env, QuestStatus.START);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
					return true;
				case 1003:
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
					return true;
			}
		}
		else if(targetId == 798954)
		{
			if(qs != null)
			{
				if(env.getDialogId() == 25 && qs.getStatus() == QuestStatus.START)
				{
					return sendQuestDialog(env, 2375);
				}
				else if(env.getDialogId() == 1009)
				{
                                                                                           player.getInventory().removeFromBagByItemId(182206745, 1);
					qs.setQuestVar(1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env); 
					return defaultQuestEndDialog(env);
				}
				else
					return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onItemUseEvent(final QuestCookie env, Item item)
	{
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(id != 182206745)
			return false;
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 20, 1, 0), true);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
			return sendQuestDialog(env, 4);
			return true;
	}	
}