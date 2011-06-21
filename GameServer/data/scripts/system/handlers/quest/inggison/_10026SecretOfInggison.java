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

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
/**
 * @author dta3000
 * 
 */
public class _10026SecretOfInggison extends QuestHandler
{
	private final static int	questId	= 10026;

	public _10026SecretOfInggison()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(799052).addOnQuestStart(questId);            // Steropes
		qe.setNpcQuestData(799052).addOnTalkEvent(questId);
		qe.setNpcQuestData(799053).addOnTalkEvent(questId);             // Nydrea
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env, 10001);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVars().getQuestVars();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 799052)
			{
				switch(env.getDialogId())
				{
					case 25:
						if(var == 0)
							return sendQuestDialog(env, 1011);
					case 1353:
						if(var == 0)
						{
							PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(1, 14));
							return false;
						}
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
			else if (targetId == 799053)
			{
				switch(env.getDialogId())
				{
					case 25:
						if(var == 1)
							return sendQuestDialog(env, 1352);
					case 1012:
						if(var == 1)
						{
							PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(1, 15));
							return false;
						}
					case 10255:
						if(var == 1)
						{
							qs.setQuestVar(2);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 799053 && var == 2)
			{
				switch (env.getDialogId())
				{
					case 1009:
					{
						return sendQuestDialog(env, 5);				
					}
					default : return defaultQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
