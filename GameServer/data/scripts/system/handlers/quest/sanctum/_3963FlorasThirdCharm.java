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
package quest.sanctum;

import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;


/**
 * @author XRONOS
 *
 */

public class _3963FlorasThirdCharm extends QuestHandler {
	private final static int	questId	= 3963;
	
	
	public _3963FlorasThirdCharm() {
		super(questId);
	}

    @Override
    public void register() {
        int[] npcs = {798384, 203740};
        qe.setNpcQuestData(798384).addOnQuestStart(questId);
        for (int npc : npcs)
            qe.setNpcQuestData(npc).addOnTalkEvent(questId);
    }

	@Override
	public boolean onDialogEvent(QuestCookie env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 798384, 182206110, 1))
			return true;

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)	{
			switch (env.getTargetId())	{
				case 203740:
					switch(env.getDialogId()) {
						case 25:
							if(var == 0)
								return sendQuestDialog(env, 1352);
						case 10000:
							return defaultCloseDialog(env, 0, 1, 0, 0, 182206110, 1);
					}
					break;
				case 798384:
					switch(env.getDialogId()) {
						case 25:
							if(var == 1)
								return sendQuestDialog(env, 2375);
						case 33:
							return defaultQuestItemCheck(env, 1, 2, true, 5, 2716);
					}
					break;
			}
		}
		return defaultQuestRewardDialog(env, 798384, 0);
	}
}