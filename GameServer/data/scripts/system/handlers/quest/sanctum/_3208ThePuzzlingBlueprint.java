package quest.sanctum;

import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;



public class _3208ThePuzzlingBlueprint extends QuestHandler
{
	private final static int	questId	= 3208;

	public _3208ThePuzzlingBlueprint()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		int[] npcs = {203852, 798026, 203830, 798320};
		qe.setNpcQuestData(730195).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 730195))
			return true;
		if(qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 798026:
					switch(env.getDialogId())
					{
						case 25:
							if(var == 0)
								return sendQuestDialog(env, 1352);
						case 10000:
							return defaultCloseDialog(env, 0, 1, true, false);
					}
				case 203830:
					switch(env.getDialogId())
					{
						case 25:
							if(var == 1)
								return sendQuestDialog(env, 1693);
						case 10001:
							return defaultCloseDialog(env, 1, 2, true, false);
					}
			}
		}
		return defaultQuestRewardDialog(env, 798320, 2375);
	}
	
}
