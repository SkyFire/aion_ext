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

import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.quest.handlers.QuestHandler;
import com.aionemu.gameserver.quest.model.QuestCookie;
import com.aionemu.gameserver.quest.model.QuestState;
import com.aionemu.gameserver.quest.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;


public class _38001RadiantOpsRecruitment extends QuestHandler
{
	private final static int	questId	= 38001;

	public _38001RadiantOpsRecruitment()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;		
		return defaultQuestRewardDialog(env, 799828, 10002);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
			if(!lvlCheck)
				return false;
			QuestService.startQuest(env, QuestStatus.REWARD);
			return true;
		}
		return false;
	}

	@Override
	public void register()
	{
		if(GSConfig.SERVER_VERSION.startsWith("1.9"))
		{
			qe.setNpcQuestData(799828).addOnTalkEvent(questId);
			qe.addQuestLvlUp(questId);
		}
	}
}
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

import gameserver.configs.main.GSConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;


public class _38001RadiantOpsRecruitment extends QuestHandler
{
	private final static int	questId	= 38001;

	public _38001RadiantOpsRecruitment()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;		
		return defaultQuestRewardDialog(env, 799828, 10002);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
			if(!lvlCheck)
				return false;
			QuestService.startQuest(env, QuestStatus.REWARD);
			return true;
		}
		return false;
	}

	@Override
	public void register()
	{
		if(GSConfig.SERVER_VERSION.startsWith("1.9"))
		{
			qe.setNpcQuestData(799828).addOnTalkEvent(questId);
			qe.addQuestLvlUp(questId);
		}
	}
}
