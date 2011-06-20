package quest.theobomos;

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
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

/**
 * @author Leunam
 *
 */
public class _3055FugitiveScopind extends QuestHandler {
	private final static int questId = 3055;
	
	public _3055FugitiveScopind() {
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(730146).addOnQuestStart(questId);
		qe.setNpcQuestData(730146).addOnTalkEvent(questId);
		qe.setNpcQuestData(798195).addOnTalkEvent(questId);	
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
	final Player player = env.getPlayer();
	int targetId = 0;
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 730146)
		{
            	if (env.getDialogId() == -1)
                		return sendQuestDialog(env, 4762);
            	else if (env.getDialogId() == 10000) {
                		QuestService.startQuest(env, QuestStatus.START);
                		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
                		return true;
            	}
		}
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 798195)
			{
				return defaultQuestEndDialog(env);
			}
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 798195)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 0)
						return sendQuestDialog(env, 1011);
				case 33:
					if(var == 0)
					{
						if(player.getInventory().getItemCountByItemId(182208040) >= 1 )
						{
							player.getInventory().removeFromBagByItemId(182208040, 1);
							qs.setQuestVarById(0, var + 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);								
							return sendQuestDialog(env, 5);
						}
						else
							return sendQuestDialog(env, 10001);
					}
					return false;
			}
		}			
		return false;
	}
}
