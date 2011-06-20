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
import gameserver.model.templates.quest.QuestItems;
import gameserver.model.gameobjects.Item;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

/**
 * @author Fennek
 * 
 */

public class _30204PilomenesAdvice	extends QuestHandler
{
	private final static int questId = 30204;
	
	public _30204PilomenesAdvice()
	{
		super (questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(798926).addOnQuestStart(questId); //Outremus
		qe.setNpcQuestData(798941).addOnTalkEvent(questId); //Pilomenes
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		//Instanceof
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		// ------------------------------------------------------------
        // NPC Quest :
		// Outremus start
		if (qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if (targetId == 798926) // Outremus Start
			{
				if (env.getDialogId() == 25)
					return sendQuestDialog(env, 1011);
				else
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				return defaultQuestStartDialog(env);
			}
		}
		if (qs == null)
			return false;		
		if (targetId == 798941 && qs.getStatus() == QuestStatus.REWARD) //Pilomenes
		{
			return sendQuestDialog(env, 2375);
		}
		return false;
	}
}
