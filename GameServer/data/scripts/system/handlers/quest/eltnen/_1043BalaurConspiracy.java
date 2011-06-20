/*
  * This file is part of Aion X Emu <aionxemu.com>
  *
  * This is free software: you can redistribute it and/or modify
  * it under the terms of the GNU Lesser Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This software is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU Lesser Public License for more details.
  *
  * You should have received a copy of the GNU Lesser Public License
  * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
 
package quest.eltnen;
import java.util.Collection;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.InstanceService;
import gameserver.services.QuestService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldMapInstance;


/**
 * @author Balthazar
 * Finished by Bio/Dreamworks
 */

public class _1043BalaurConspiracy extends QuestHandler
{
	private final static int	questId	= 1043;
	private int instanceId = 0;
	private Creature kimeiaNpc;

	public _1043BalaurConspiracy()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(203901).addOnTalkEvent(questId);
		qe.setNpcQuestData(204020).addOnTalkEvent(questId);
		qe.setNpcQuestData(204044).addOnTalkEvent(questId);
		qe.setNpcQuestData(700177).addOnTalkEvent(questId);
		qe.addOnDie(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		int [] quests = {1031, 1032, 1033, 1034, 1035, 1036, 1037, 1038, 1039, 1040, 1041, 1042};
		return defaultQuestOnLvlUpEvent(env, quests);
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		final int var = qs.getQuestVarById( 0 );
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if( qs.getStatus() == QuestStatus.REWARD )
		{
			if( targetId == 203901 ) {
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 2375);
				return defaultQuestEndDialog(env);
			}
		}
		else if( qs.getStatus() != QuestStatus.START )
			return false;

		switch( targetId )
		{
			case 203901:
				switch( env.getDialogId() )
				{
					case 25:
						if(var == 0)
							return sendQuestDialog(env, 1011);
					case 10000:
						if(var == 0)
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							return sendQuestDialog(env, 0);
						}
					default:
						return false;
				}
			case 204020:
				switch( env.getDialogId() )
				{
					case 25:
						if(var == 1)
							return sendQuestDialog(env, 1352);
					case 10001:
						if(var == 1)
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							return sendQuestDialog(env, 0);
						}
					default:
						return false;
				}
			case 204044:
				switch( env.getDialogId() )
				{
					case 25:
						if(var == 2)
							return sendQuestDialog(env, 1693);
						else if(var == 4)
							return sendQuestDialog(env, 2034);
					case 10002:
						if(var == 2 && player.getPlayerGroup() != null)
						{
							updateParty(player, 2, 3, env);
							kimeiaNpc = (Creature) env.getVisibleObject();
							
							ThreadPoolManager.getInstance().schedule(new Runnable()
							{
								@Override
								public void run()
								{
									if(!kimeiaNpc.getLifeStats().isAlreadyDead())
									{
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213575, (float) 242.6, (float) 267, (float) 229.5, (byte) 93, true))), (float) 275, (float) 181.75, (float) 204.78);
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213575, (float) 259.6, (float) 268.24, (float) 229.37, (byte) 93, true))), (float) 268.17, (float) 182.61, (float) 205.14);
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213576, (float) 255.15, (float) 264.7, (float) 228.66, (byte) 93, true))), (float) 274.54, (float) 187.76, (float) 205.71); 
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213576, (float) 249.38, (float) 262.78, (float) 228.6, (byte) 93, true))), (float) 269, (float) 188.9, (float) 206.1);
									}
									else
										updateParty(player, -1, 0, env);
										
								}
							}, 1000);
							
							ThreadPoolManager.getInstance().schedule(new Runnable()
							{
								@Override
								public void run()
								{
									if(!kimeiaNpc.getLifeStats().isAlreadyDead())
									{
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213575, (float) 242.6, (float) 267, (float) 229.5, (byte) 93, true))), (float) 275, (float) 181.75, (float) 204.78);
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213578, (float) 259.6, (float) 268.24, (float) 229.37, (byte) 93, true))), (float) 268.17, (float) 182.61, (float) 205.14);
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213576, (float) 255.15, (float) 264.7, (float) 228.66, (byte) 93, true))), (float) 274.54, (float) 187.76, (float) 205.71);
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213576, (float) 249.38, (float) 262.78, (float) 228.6, (byte) 93, true))), (float) 269, (float) 188.9, (float) 206.1);
									}
									else
										updateParty(player, -1, 0, env);
								}
							}, 61000);
							
							ThreadPoolManager.getInstance().schedule(new Runnable()
							{
								@Override
								public void run()
								{
									if(!kimeiaNpc.getLifeStats().isAlreadyDead())
									{
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213575, (float) 242.6, (float) 267, (float) 229.5, (byte) 93, true))), (float) 275, (float) 181.75, (float) 204.78);
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213578, (float) 259.6, (float) 268.24, (float) 229.37, (byte) 93, true))), (float) 268.17, (float) 182.61, (float) 205.14);
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213579, (float) 255.15, (float) 264.7, (float) 228.66, (byte) 93, true))), (float) 274.54, (float) 187.76, (float) 205.71);
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213576, (float) 249.38, (float) 262.78, (float) 228.6, (byte) 93, true))), (float) 269, (float) 188.9, (float) 206.1);
									}
									else
										updateParty(player, -1, 0, env);
								}
							}, 121000);
							
							ThreadPoolManager.getInstance().schedule(new Runnable()
							{
								@Override
								public void run()
								{
									if(!kimeiaNpc.getLifeStats().isAlreadyDead())
									{
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213577, (float) 242.6, (float) 267, (float) 229.5, (byte) 93, true))), (float) 275, (float) 181.75, (float) 204.78);
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213578, (float) 259.6, (float) 268.24, (float) 229.37, (byte) 93, true))), (float) 268.17, (float) 182.61, (float) 205.14);
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213579, (float) 255.15, (float) 264.7, (float) 228.66, (byte) 93, true))), (float) 274.54, (float) 187.76, (float) 205.71);
										moveTo(((Creature)(QuestService.addNewSpawn(310040000, player.getInstanceId(), 213576, (float) 249.38, (float) 262.78, (float) 228.6, (byte) 93, true))), (float) 269, (float) 188.9, (float) 206.1);
									}
									else
										updateParty(player, -1, 0, env);
								}
							}, 181000);

							ThreadPoolManager.getInstance().schedule(new Runnable()
							{
								@Override
								public void run()
								{
									if(!kimeiaNpc.getLifeStats().isAlreadyDead())
										updateParty(player, 3, 4, env);
									else
										updateParty(player, -1, 0, env);
								}
							}, 240000);
							return sendQuestDialog(env, 0);
						}
					case 10003:
						if(var == 4)
						{
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
			  			    TeleportService.teleportTo(player, 210020000, 1, 263.41736f, 2794.24070f, 272.61414f, (byte) 77, 0);
							return sendQuestDialog(env, 0);
						}
					default:
						return false;
				}
			case 700177:
				if(var == 2 && player.getPlayerGroup() != null)
				{
					final int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
					
					WorldMapInstance newInstance = InstanceService.getRegisteredInstance(310040000, player.getPlayerGroup().getGroupId());
					if (newInstance == null)
					{
						newInstance = InstanceService.getNextAvailableInstance(310040000);
						InstanceService.registerGroupWithInstance(newInstance, player.getPlayerGroup());
						instanceId = newInstance.getInstanceId();
					}
					
					ThreadPoolManager.getInstance().schedule(new Runnable()
					{
						@Override
						public void run()
						{
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
							PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
							updateQuestStatus(env);
							TeleportService.teleportTo(player, 310040000, instanceId,(float) 274.51,(float) 168.6,(float) 204.3, 0);
						}
					}, 3000);
					return false;
				}
				else if (var == 2 && player.getPlayerGroup() == null)//TODO: find proper message, just temp fix
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENTER_ONLY_PARTY_DON);
				default:
					return false;
		}
	}
	
	@Override
	public boolean onDieEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		int var = qs.getQuestVars().getQuestVars();
		if(var == 3 || var == 4)
		{
			qs.setQuestVar(2);
			updateQuestStatus(env);
		}

		return false;
	}

	private void updateParty(Player player, int reqVar, int var, QuestCookie env)
	{							
		Collection<Player> members = player.getPlayerGroup().getMembers();
		for (Player member : members)
		{
			if (member.getWorldId() == 310040000)
			{
				QuestState qs1 = member.getQuestStateList().getQuestState(questId);
				if (qs1 != null && qs1.getStatus() == QuestStatus.START && (qs1.getQuestVarById(0) == reqVar || qs1.getQuestVarById(0) == -1))
				{
					if(var == 3)
					{
						if(player == member)
							QuestService.questTimerStart(env, 242);// Real timer
						else
							PacketSendUtility.sendPacket(member, new SM_QUEST_ACCEPTED(questId, 242));// Dummy timer
					}
					else if(var == 4 || reqVar == -1)
					{						
						if(player == member)
							QuestService.questTimerEnd(env);
						else
							PacketSendUtility.sendPacket(member, new SM_QUEST_ACCEPTED(questId, 0));
						
						if(var == 4)
							PacketSendUtility.sendPacket(member, new SM_PLAY_MOVIE(0, 157));
					}

					qs1.setStatus(QuestStatus.START);
					qs1.setQuestVar(var);
					
				}
			}
		}
	}

	private void moveTo(final Creature Attacker, float x, float y, float z)
	{
		PacketSendUtility.broadcastPacket((Npc) Attacker, new SM_EMOTION((Npc) Attacker, EmotionType.ATTACKMODE));
		Attacker.getMoveController().setNewDirection(x, y, z);
		Attacker.getMoveController().schedule();
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				if(Attacker != null && !Attacker.getLifeStats().isAlreadyDead())
					Attacker.getMoveController().stop();
			}
		}, 25000);
	}
}