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



public class _1918AnAxForNamus extends QuestHandler{

	private final static int	questId	= 1918;  

	
	public _1918AnAxForNamus() {
		super(questId);
	}


    @Override
    public void register() {
        int[] npcs = {203835, 203788};
        qe.setNpcQuestData(203835).addOnQuestStart(questId);
        for (int npc : npcs)
            qe.setNpcQuestData(npc).addOnTalkEvent(questId);
    }

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 203835))
			return true;

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203788)
			{
				switch(env.getDialogId())
					{
						case 25:
							if(var == 0)
								return sendQuestDialog(env, 1352);
						case 10000:
							return defaultCloseDialog(env, 0, 1, true, false, 182206002, 1, 0, 0);
					}
			}
		}
		return defaultQuestRewardDialog(env, 203835, 0);
	}
}
