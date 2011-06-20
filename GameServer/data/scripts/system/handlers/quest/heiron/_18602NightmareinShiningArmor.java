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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldMapInstance;

/**
 * @author Leunam
 *
 */
public class _18602NightmareinShiningArmor extends QuestHandler {
	private final static int questId = 18602;
	
	public _18602NightmareinShiningArmor() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.setNpcQuestData(205229).addOnQuestStart(questId);
        	qe.setNpcQuestData(730308).addOnActionItemEvent(questId);
        	qe.setNpcQuestData(700939).addOnActionItemEvent(questId);
        	qe.setNpcQuestData(205229).addOnTalkEvent(questId);
        	qe.setNpcQuestData(730308).addOnTalkEvent(questId); 
        	qe.setNpcQuestData(700939).addOnTalkEvent(questId);
        	qe.setNpcQuestData(700924).addOnTalkEvent(questId); 
        	qe.setNpcQuestData(217005).addOnKillEvent(questId);
	}

	@Override
	public boolean onActionItemEvent(QuestCookie env) {
        	int targetId = 0;
        	if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        	return (targetId == 730308 || targetId == 700939);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
	final Player player = env.getPlayer();
	final int instanceId = player.getInstanceId();
	int targetId = 0;
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 205229)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 25)
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);
			}
		}
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 205229)
			{
                		ThreadPoolManager.getInstance().schedule(new Runnable() 
				{
                    		@Override
                    		public void run() 
					{
						if (player.getLevel() <= 44)
						{
							WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300230000);
							InstanceService.registerPlayerWithInstance(newInstance, player);
							TeleportService.teleportTo(player, 300230000, newInstance.getInstanceId(), 228, 251, 191, 0);
						} 
                   		}
                		}, 3000);
			}
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
			if(targetId == 205229)
			{
				switch(env.getDialogId())
				{
					case 25:
						if(var == 0)
							return sendQuestDialog(env, 1011);
						else if(var == 4)
							return sendQuestDialog(env, 10002);
					case 10000:
						if(var == 0)
						{
							if (player.getPlayerGroup() != null) 
							{
                       					return sendQuestDialog(env, 1012);	
							}
							else if (player.getLevel() <= 44)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);									
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
								WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300230000);
								InstanceService.registerPlayerWithInstance(newInstance, player);
								TeleportService.teleportTo(player, 300230000, newInstance.getInstanceId(), 228, 251, 191, 0);
								return true;
							}
						}
					case 1009:
						if(var == 4)
						{
							qs.setQuestVarById(0, var + 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);	
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					return false;
				}
			}
			else if(targetId == 730308)
			{
            		switch (env.getDialogId()) 
				{
                			case -1:
                    			if (var == 1)
                        			return sendQuestDialog(env, 1352);
                			case 1353:
                    			break;
                			case 10001:
                    			if (var == 1) 
						{
							if (player.getInventory().getItemCountByItemId(185000109) >= 1) 
							{
								qs.setQuestVarById(0, var + 1);
                        				player.getInventory().removeFromBagByItemId(185000109, 1);
								if (ItemService.addItems(player, Collections.singletonList(new QuestItems(164000143, 1))));
								updateQuestStatus(env);									
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300230000);
								InstanceService.registerPlayerWithInstance(newInstance, player);
								TeleportService.teleportTo(player, 300230000, newInstance.getInstanceId(), 689, 508, 197, 0);
							}
							else
                        				return sendQuestDialog(env, 10001);	
						}
					return false;
				}
			}
			else if(targetId == 700939 && var == 2)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 1693);
				else if(env.getDialogId() == 10002)
				{
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);									
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				return true;
			}
			else if(targetId == 700924 && env.getDialogId() == -1)
			{
				if (player.getInventory().getItemCountByItemId(185000101) == 1)
 				{
					final int targetObjectId = env.getVisibleObject().getObjectId();
					player.getInventory().removeFromBagByItemId(185000101, 1);
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
					targetObjectId), true);
					ThreadPoolManager.getInstance().schedule(new Runnable()
					{
						@Override
						public void run()
						{
							WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300230000);
							InstanceService.registerPlayerWithInstance(newInstance, player);
							TeleportService.teleportTo(player, 300230000, newInstance.getInstanceId(), 593, 774, 215, 0);
						}
					}, 3000);
				}
			}	
		return false;
	}

	@Override
	public boolean onKillEvent(QuestCookie env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() != QuestStatus.START)
			return false;
		if(targetId == 217005)
		{
			if(var == 3)
			{
				qs.setQuestVarById(0, var + 1);
				updateQuestStatus(env);	
				return true;
			}
		}
		return false;
	}
}