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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.ZoneService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;



public class _10025ItsBetterItsAnObelisk extends QuestHandler
{

	private final static int	questId	= 10025;
	private final static int[]	npc_ids	= { 798927, 798932, 798933, 799023, 799024, 278500, 798926, 700607, 700637 };

	public _10025ItsBetterItsAnObelisk()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setQuestItemIds(182206623).add(questId);
		qe.setQuestItemIds(182206624).add(questId);
		qe.setQuestItemIds(182206626).add(questId);
		qe.setQuestEnterZone(ZoneName.BESHMUNDIR_WALK_300170000).add(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	 
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName)
	{
		if(zoneName != ZoneName.BESHMUNDIR_WALK_300170000)
			return false;
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getQuestVars().getQuestVars() != 9)
			return false;
		env.setQuestId(questId);
		qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
		updateQuestStatus(env);
		return true;
	}
	
	@Override
	public boolean onItemUseEvent(final QuestCookie env, Item item)
	{
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int var = qs.getQuestVarById(0);
		if (var == 4)
		{
			if (qs == null || id != 182206623)
				return true;
			if(!ZoneService.getInstance().isInsideZone(player, ZoneName.TEMPLE_OF_SCALES_210050000))
				return false;
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					player.getInventory().removeFromBagByItemId(182206623, 1);
					int var = qs.getQuestVarById(0);
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
				}
			}, 3000);
			return true;
		
		}
		else if (var == 6)
		{
			if (qs == null || id != 182206624)
				return true;
			if(!ZoneService.getInstance().isInsideZone(player, ZoneName.ALTAR_OF_AVARICE_210050000))
				return false;
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					player.getInventory().removeFromBagByItemId(182206624, 1);
					int var = qs.getQuestVarById(0);
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
				}
			}, 3000);
			return true;
		
		}
		else if (var == 12)
		{
			if (qs == null || id != 182206626)
				return true;
			if(!ZoneService.getInstance().isInsideZone(player, ZoneName.ANGRIEF_BULWARK_210050000))
				return false;
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					player.getInventory().removeFromBagByItemId(182206626, 1);
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
			}, 3000);
			return true;
		
		}
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
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 798926)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 10002);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
			return false;
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 798927)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 0)
						return sendQuestDialog(env, 1011);
				case 10000:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		else if(targetId == 798932)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 1)
						return sendQuestDialog(env, 1352);
				case 10001:
					if(var == 1)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}					
			}
		}
		else if(targetId == 798933)
		{
			switch(env.getDialogId())
			{		
				case 25:
					if(var == 2)
						return sendQuestDialog(env, 1693);
				case 10002:
					if(var == 2)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		else if(targetId == 799023)
		{
			switch(env.getDialogId())
			{		
				case 25:
					if(var == 3)
						return sendQuestDialog(env, 2034);
				case 10003:
					if(var == 3)
					{
						if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182206623, 1))))
							return true;
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		else if(targetId == 799024)
		{
			switch(env.getDialogId())
			{		
				case 25:
					if(var == 5)
						return sendQuestDialog(env, 2716);
					else if(var == 7)
						return sendQuestDialog(env, 3398);
				case 10005:
					if(var == 5)
					{
						if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182206624, 1))))
							return true;
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 10007:
					if(var == 7)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		else if(targetId == 278500)
		{
			switch(env.getDialogId())
			{		
				case 25:
					if(var == 8)
						return sendQuestDialog(env, 3739);
				case 10008:
					if(var == 8)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		else if(targetId == 700607)
		{
			switch(env.getDialogId())
			{		
				case -1:
					if (var == 10)
					{
						final int targetObjectId = env.getVisibleObject().getObjectId();
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
						qs.setQuestVar(11);
						updateQuestStatus(env);
					}
			}
		}
		else if(targetId == 700637)
		{
			switch(env.getDialogId())
			{		
				case -1:
					if (var == 11)
					{
						final int targetObjectId = env.getVisibleObject().getObjectId();
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
						qs.setQuestVar(12);
						updateQuestStatus(env);
					}
			}
		}

		return false;
	}	
}
