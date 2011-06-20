package quest.inggison;

import java.util.Collections;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

/**
 * @author Leunam
 *
 */
public class _11068AMysteriousWind extends QuestHandler {
	private final static int questId = 11068;
	private final static int[] npc_ids ={ 799025, 799026 };
	
	public _11068AMysteriousWind() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.setNpcQuestData(799025).addOnQuestStart(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
	final Player player = env.getPlayer();
	int targetId = 0;
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	QuestState qs = player.getQuestStateList().getQuestState(questId);
	if(targetId == 799025)
	{
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if (env.getDialogId() == 25)
				return sendQuestDialog(env, 1011);
			else
				return defaultQuestStartDialog(env);
		}
	}
	if(qs == null)
	return false;
		
	int var = qs.getQuestVarById(0);
	if(qs.getStatus() == QuestStatus.REWARD)
	{
		if(targetId == 799025)
		{
			return defaultQuestEndDialog(env);
		}
	}
	else if(qs.getStatus() != QuestStatus.START)
	{
		return false;
	}
		if(targetId == 799026)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 0)
						return sendQuestDialog(env, 1352);
				case 10000:
					if(var == 0)
					{
						if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182206858, 1))))
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);								
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				return false;
			}
		}
		else if(targetId == 799025)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 1)
						return sendQuestDialog(env, 2375);
				case 1009:
					if(var == 1)
					{
						player.getInventory().removeFromBagByItemId(182206858, 1);
						qs.setQuestVarById(0, var + 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);								
						return sendQuestDialog(env, 5);
					}
				return false;
			}
		}		
	return false;
     }
}
