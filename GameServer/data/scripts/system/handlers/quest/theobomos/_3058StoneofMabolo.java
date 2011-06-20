package quest.theobomos;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.zone.ZoneName;

/**
 * @author Leunam
 * 
 */
public class _3058StoneofMabolo extends QuestHandler {
	private final static int questId = 3058;

	public _3058StoneofMabolo() {
		super(questId);
	}

	@Override
	public void register() {
		qe.setNpcQuestData(798189).addOnTalkEvent(questId);
		qe.setNpcQuestData(203701).addOnTalkEvent(questId);
		qe.setNpcQuestData(798213).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
	final Player player = env.getPlayer();
	int targetId = 0;
	final QuestState qs = player.getQuestStateList().getQuestState(questId);
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (env.getDialogId() == 1002) {	
				QuestService.startQuest(env, QuestStatus.START);
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
				return true;
            	} else
                		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
		} 
		if(qs == null)
		return false;
		
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 798213)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 2375);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return defaultQuestEndDialog(env);
			}
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 798189)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 0)
						return sendQuestDialog(env, 1352);
				case 10000:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);								
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				return false;
			}
		}
		else if(targetId == 203701)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 1)
						return sendQuestDialog(env, 1693);
				case 10001:
					if(var == 1)
					{
						qs.setQuestVarById(0, var + 1);
					qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);								
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				return false;
			}
		}
     	return false;
	}
}
