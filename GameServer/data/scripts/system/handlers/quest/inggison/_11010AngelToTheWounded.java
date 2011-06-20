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
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

/**
 * @author dta3000
 */
 
public class _11010AngelToTheWounded extends QuestHandler
{
	private final static int	questId	= 11010;
	
	public _11010AngelToTheWounded()
	{
		super(questId);
	}

	@Override
	public void register()
	{
	qe.setNpcQuestData(798931).addOnQuestStart(questId);
	qe.setNpcQuestData(798931).addOnTalkEvent(questId);
	qe.setNpcQuestData(799071).addOnTalkEvent(questId);
                  qe.setNpcQuestData(798906).addOnTalkEvent(questId);
                  qe.setNpcQuestData(730323).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 798931)
			{
				if(env.getDialogId() == 25)
				{
					return sendQuestDialog(env, 1011);
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if(qs == null)
			return false;
			
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{ 
                                                                       case 799071:
				{
					switch(env.getDialogId())
					{
						case 25:
						{
							return sendQuestDialog(env, 1352);
						}
						case 10000:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);  
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
                                                                        case 798906:
				{
					switch(env.getDialogId())
					{
						case 25:
						{
							return sendQuestDialog(env, 1693);
						}
						case 10001:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env); 
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
				case 730323:
				{
					switch(env.getDialogId())
					{
						case 25:
						{
							return sendQuestDialog(env, 2034);
						}
						case 10002:
						{
							qs.setQuestVar(3);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);   
                                                                                                                              PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                                                                                                              ItemService.addItems(player, Collections.singletonList(new QuestItems(182206713, 1)));
                                                                                                                              return true;
						}
					}
                                                                                          return false;
				}
                        }
                } 
                if( qs.getStatus() == QuestStatus.REWARD )
		{
			if( targetId == 799071 )
                        {
                                  if (env.getDialogId() == -1)
                                         return sendQuestDialog(env, 2375);
					return defaultQuestEndDialog( env );
                        }     
		}
		return false;
	}
}