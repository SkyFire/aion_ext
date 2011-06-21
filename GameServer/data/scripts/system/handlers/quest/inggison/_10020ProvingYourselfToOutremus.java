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

import gameserver.controllers.movement.StartMovingListener;
import gameserver.model.EmotionType;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

/**
 * @author PZIKO333
 * 
 */

public class _10020ProvingYourselfToOutremus extends QuestHandler
{
	private final static int	questId	= 10020;
	private final static int[]	npcIds	= { 798926, 798927, 798928, 798955, 730223, 730224, 730225, 700628, 700629,
		700630 };
	private final static int[]	killIds	= { 215508, 215509, 216463, 216783, 216647, 215518, 216691, 215516, 216464,
		215507, 215506, 216692, 215517, 216648, 215519, 216647, 216691, 215516, 215505, 215504, 216782 };

	public _10020ProvingYourselfToOutremus()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(798926).addOnQuestStart(questId);
		for(int npcId : npcIds)
			qe.setNpcQuestData(npcId).addOnTalkEvent(questId);
		for(int killId : killIds)
			qe.setNpcQuestData(killId).addOnKillEvent(questId);
	}

	@Override
	public boolean onLvlUpEvent(final QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env, 10026);
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 798926)
			{
				if(var == 10)
				{
					return defaultQuestEndDialog(env);
				}
				return false;
			}
		}
		if(qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 798926)
			{
				switch(env.getDialogId())
				{
					case 25:
						if(var == 0)
							return sendQuestDialog(env, 1011);
						return false;

					case 10000:
						if(var == 0)
						{
							qs.setQuestVar(1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 10));
							return true;
						}
						return false;
				}
			}
			if(targetId == 798928)
			{
				switch(env.getDialogId())
				{
					case 25:
						if(var == 1)
							return sendQuestDialog(env, 1352);
						if(var == 3 || var == 4)
							return sendQuestDialog(env, 2716);
						return false;

					case 10001:
						if(var == 1)
						{
							qs.setQuestVar(2);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 10));
							return true;
						}
						return false;

					case 10005:
						if(var == 3 || var == 4)
						{
							qs.setQuestVar(5);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 10));
							return true;
						}
						return false;
				}
			}
			if(targetId == 798927)
			{
				switch(env.getDialogId())
				{
					case 25:
						if(var == 5)
							return sendQuestDialog(env, 3057);
						if(var == 10)
							return sendQuestDialog(env, 3398);
						return false;

					case 10006:
						if(var == 5)
						{
							qs.setQuestVar(6);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 10));
							return true;
						}
						return false;

					case 10255:
						if(var == 10)
						{
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 10));
							return true;
						}
						return false;
				}
			}
			if(targetId == 798955)
			{
				switch(env.getDialogId())
				{
					case 25:
						if(var == 6)
							return sendQuestDialog(env, 3058);
						return false;

					case 10006:
						if(var == 6)
						{
							qs.setQuestVar(7);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 10));
							return true;
						}
						return false;
				}
			}
			if(targetId == 730223)
			{
				if(qs.getQuestVarById(1) == 0)
					sendQuestDialog(env, 1693);
				switch(env.getDialogId())
				{
					case 10002:
						if(qs.getQuestVarById(1) == 0)
						{
							qs.setQuestVarById(1, var);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 0));
							if(qs.getQuestVarById(1) != 0 && qs.getQuestVarById(2) != 0 && qs.getQuestVarById(3) != 0)
							{
								qs.setQuestVar(3);
								updateQuestStatus(env);
							}
							return true;
						}
						return false;
				}
			}
			if(targetId == 730224)
			{
				if(qs.getQuestVarById(2) == 0)
					sendQuestDialog(env, 1693);
				switch(env.getDialogId())
				{
					case 10002:
						if(qs.getQuestVarById(2) == 0)
						{
							qs.setQuestVarById(2, var);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 0));
							if(qs.getQuestVarById(1) != 0 && qs.getQuestVarById(2) != 0 && qs.getQuestVarById(3) != 0)
							{
								qs.setQuestVar(3);
								updateQuestStatus(env);
							}
							return true;
						}
						return false;
				}
			}
			if(targetId == 730225)
			{
				if(qs.getQuestVarById(3) == 0)
					sendQuestDialog(env, 1693);
				switch(env.getDialogId())
				{
					case 10002:
						if(qs.getQuestVarById(3) == 0)
						{
							qs.setQuestVarById(3, var);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 0));
							if(qs.getQuestVarById(1) != 0 && qs.getQuestVarById(2) != 0 && qs.getQuestVarById(3) != 0)
							{
								qs.setQuestVar(3);
								updateQuestStatus(env);
							}
							return true;
						}
						return false;
				}
			}
			if(targetId == 700628)
			{
				if(var == 7)
				{
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), env.getVisibleObject()
						.getObjectId(), 3000, 1));
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0,
						env.getVisibleObject().getObjectId()), true);

					player.getController().cancelTask(TaskId.ITEM_USE);
					player.getObserveController().attach(new StartMovingListener(){

						@Override
						public void moved()
						{
							player.getController().cancelTask(TaskId.ITEM_USE);
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), env
								.getVisibleObject().getObjectId(), 3000, 0));
							PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT,
								0, env.getVisibleObject().getObjectId()), true);
						}
					});
					player.getController().addNewTask(TaskId.ITEM_USE,
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							@Override
							public void run()
							{
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), env
									.getVisibleObject().getObjectId(), 3000, 0));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player,
									EmotionType.END_QUESTLOOT, 0, env.getVisibleObject().getObjectId()), true);
								qs.setQuestVar(8);
								updateQuestStatus(env);
							}
						}, 3000));
				}
			}
			if(targetId == 700629)
			{
				if(var == 8)
				{
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), env.getVisibleObject()
						.getObjectId(), 3000, 1));
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0,
						env.getVisibleObject().getObjectId()), true);

					player.getController().cancelTask(TaskId.ITEM_USE);
					player.getObserveController().attach(new StartMovingListener(){

						@Override
						public void moved()
						{
							player.getController().cancelTask(TaskId.ITEM_USE);
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), env
								.getVisibleObject().getObjectId(), 3000, 0));
							PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT,
								0, env.getVisibleObject().getObjectId()), true);
						}
					});
					player.getController().addNewTask(TaskId.ITEM_USE,
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							@Override
							public void run()
							{
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), env
									.getVisibleObject().getObjectId(), 3000, 0));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player,
									EmotionType.END_QUESTLOOT, 0, env.getVisibleObject().getObjectId()), true);
								qs.setQuestVar(9);
								updateQuestStatus(env);
							}
						}, 3000));
				}
			}
			if(targetId == 700630)
			{
				if(var == 9)
				{
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), env.getVisibleObject()
						.getObjectId(), 3000, 1));
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0,
						env.getVisibleObject().getObjectId()), true);

					player.getController().cancelTask(TaskId.ITEM_USE);
					player.getObserveController().attach(new StartMovingListener(){

						@Override
						public void moved()
						{
							player.getController().cancelTask(TaskId.ITEM_USE);
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), env
								.getVisibleObject().getObjectId(), 3000, 0));
							PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT,
								0, env.getVisibleObject().getObjectId()), true);
						}
					});
					player.getController().addNewTask(TaskId.ITEM_USE,
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							@Override
							public void run()
							{
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), env
									.getVisibleObject().getObjectId(), 3000, 0));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player,
									EmotionType.END_QUESTLOOT, 0, env.getVisibleObject().getObjectId()), true);
								qs.setQuestVar(10);
								updateQuestStatus(env);
							}
						}, 3000));
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		int[] var1 = { 216463, 216783, 216647, 215518, 216691, 215516, 216464, 215507, 215506, 216692, 215517, 216648,
			215519, 216647, 216691, 215516, 215505, 215504, 216782 };
		int[] var2 = { 215508, 215509 };
		if(defaultQuestOnKillEvent(env, var1, 0, 23, 1) || defaultQuestOnKillEvent(env, var2, 0, 4, 2))
			return true;
		else
			return false;
	}
}
