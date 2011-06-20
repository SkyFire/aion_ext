/*
 * This file is part of Aion X EMU <aionxemu.com>.
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.gelkmaros;

import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;


/**
 * @author Kamui, Hellboy
 * 
 */
public class _21135VellunsRequest extends QuestHandler
{
	private final static int	questId	= 21135;

	public _21135VellunsRequest()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(799239).addOnQuestStart(questId);	//Vellun
		qe.setNpcQuestData(799270).addOnTalkEvent(questId);		//Skira
		qe.setNpcQuestData(799271).addOnTalkEvent(questId);		//Gehlen
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{

		if(defaultQuestNoneDialog(env, 799239))
			return true;

		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);

		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 799270)
			{
				switch(env.getDialogId())
				{
					case 25:
						if(var == 0)
							return sendQuestDialog(env, 1352);
					case 10000:
						return defaultCloseDialog(env, 0, 1, true, false);
				}
			}
		}
		return defaultQuestRewardDialog(env, 799271, 2375);
	}
}
