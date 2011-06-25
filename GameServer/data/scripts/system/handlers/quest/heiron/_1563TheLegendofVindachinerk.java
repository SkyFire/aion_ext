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
package quest.heiron;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;


/**
 * @author HellBoy
 * 
 */
public class _1563TheLegendofVindachinerk extends QuestHandler
{
	private final static int	questId	= 1563;

	public _1563TheLegendofVindachinerk()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798096).addOnQuestStart(questId);
		int[] npcs = {798096, 279005};
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
		qe.setQuestItemIds(182201729).add(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 798096, 4762))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 798096)
			{
				switch(env.getDialogId())
				{
					case 25:
						if(var == 1)
							return sendQuestDialog(env, 1352);
					case 10001:
						return defaultQuestItemCheck(env, 1, 0, true, 5, 1353);
				}
			}
			else if(env.getTargetId() == 279005)
			{
				switch(env.getDialogId())
				{
					case 25:
						if(var == 1)
							return sendQuestDialog(env, 1438);
					case 10001:
						return defaultQuestItemCheck(env, 1, 2, true, 6, 1439);
				}
			}
		}
		if(defaultQuestRewardDialog(env, 798096, 0, 0) || defaultQuestRewardDialog(env, 279005, 0, 1))
			return true;
		else
			return false;
	}
	
	@Override
	public boolean onItemUseEvent(final QuestCookie env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if(id != 182201729 || qs == null)
			return true;

		if (qs.getStatus() != QuestStatus.START)
			return false;

		if(qs.getQuestVars().getQuestVars() != 0)
			return false;

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id,
			3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId,
					id, 0, 1, 0), true);
				qs.setQuestVar(1);
				updateQuestStatus(env);
			}
		}, 3000);
		return true;
	}
}
