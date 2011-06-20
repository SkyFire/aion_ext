package quest.beluslan;

import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

/**
 * @author Leunam
 *
 */
public class _2630TheDutifulSpy extends QuestHandler {
	private final static int questId = 2630;

	public _2630TheDutifulSpy() {
		super(questId);
	}

	@Override
	public void register() {
	int[] npcs = {204799, 204777};
	for (int npc : npcs)
		qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	qe.setNpcQuestData(204799).addOnQuestStart(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
	if (defaultQuestNoneDialog(env, 204799))
	return true;
	QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
	if (qs == null)
	return false;
	int var = qs.getQuestVarById(0);
	if (qs.getStatus() == QuestStatus.START) 
	{
            	if (env.getTargetId() == 204777) 
            	{
			switch (env.getDialogId()) 
			{
			case 25:
				if (var == 0)
					return sendQuestDialog(env, 1352);
			case 10000:
				return defaultCloseDialog(env, 0, 1, true, false);
			}
		}
	}
	return defaultQuestRewardDialog(env, 204799, 2375);
    }
}
