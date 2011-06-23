package quest.haramel;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;



public class _18501ShugoIndemnity extends QuestHandler
{
	private final static int	questId	= 18501;

	public _18501ShugoIndemnity()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(799522).addOnQuestStart(questId);
		qe.setNpcQuestData(799522).addOnTalkEvent(questId);
		qe.setNpcQuestData(799523).addOnTalkEvent(questId);
		qe.setNpcQuestData(700833).addOnTalkEvent(questId);
		qe.setNpcQuestData(700951).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(targetId == 799522)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 25)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if (qs == null)
			return false;
			
		else if(targetId == 799523)
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				if(env.getDialogId() == 25)
					return sendQuestDialog(env, 2375);
				else if(env.getDialogId() == 33)
				{
					if(QuestService.collectItemCheck(env, true))
					{
						qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
							return sendQuestDialog(env, 5);
					}
					else
						return sendQuestDialog(env, 2716);	
				}
				else
					return defaultQuestStartDialog(env);
			}
			
			else if(qs.getStatus() == QuestStatus.REWARD)
				return defaultQuestEndDialog(env);
		}
		else if(targetId == 700833)
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
				return true;
		}
		else if(targetId == 700951)
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
				return true;
		}
		return false;
	}
}
