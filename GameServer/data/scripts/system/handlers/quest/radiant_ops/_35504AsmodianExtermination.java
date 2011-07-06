/*
 * This file is part of aion-unique
 *
 *  aion-engine is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.radiant_ops;

import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;


public class _35504AsmodianExtermination extends QuestHandler
{
	private final static int	questId	= 35504;

	public _35504AsmodianExtermination()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
	    if(!super.defaultQuestOnDialogInitStart(env))
			return false;
		return defaultQuestRewardDialog(env, 799825, 10002);
		

	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		if(player.getWorldId() == 210050000)
		{
			if(defaultQuestOnKillPlayerEvent(env, 1, 0, 4, false) || defaultQuestOnKillPlayerEvent(env, 1, 4, 5, true))
				return true;
		}
		return false;
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(799825).addOnTalkEvent(questId);
		qe.setNpcQuestData(799826).addOnTalkEvent(questId);
		qe.addOnKillPlayer(questId);
	}
}
