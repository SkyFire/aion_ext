package quest.heiron;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

/**
 * @author Leunam
 * 
 */
public class _1540BaittheHooks extends QuestHandler {

	private final static int questId = 1540;

	public _1540BaittheHooks() {
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204588).addOnQuestStart(questId);
		qe.setNpcQuestData(204588).addOnTalkEvent(questId);
		qe.setNpcQuestData(730189).addOnTalkEvent(questId);
		qe.setNpcQuestData(730190).addOnTalkEvent(questId);
		qe.setNpcQuestData(730191).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env) {
	final Player player = env.getPlayer();
	int targetId = 0;
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	final QuestState qs = player.getQuestStateList().getQuestState(questId);
	if (qs == null || qs.getStatus() == QuestStatus.NONE)
	{
		if(targetId == 204588) {
			if (env.getDialogId() == 25)
				return sendQuestDialog(env, 1011);
			else
				return defaultQuestStartDialog(env);
		}

	}
	else if(qs.getStatus() == QuestStatus.START)
	{
		switch(targetId)
		{
			case 730189:
			{
				if(qs.getQuestVarById(0) == 0 && env.getDialogId() == -1)
				{
					final int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId,
						3000, 1));
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
					ThreadPoolManager.getInstance().schedule(new Runnable(){
						@Override
						public void run()
						{
							if(!player.isTargeting(targetObjectId))
								return;
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
								targetObjectId, 3000, 0));
							PacketSendUtility.broadcastPacket(player,
								new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
							{
								qs.setQuestVarById(0, 1);
								updateQuestStatus(env);
							}
						}
					}, 3000);
				}
			}

			case 730190:
			{
				if(qs.getQuestVarById(0) == 1 && env.getDialogId() == -1)
				{
					final int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId,
						3000, 1));
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
					ThreadPoolManager.getInstance().schedule(new Runnable(){
						@Override
						public void run()
						{
							if(!player.isTargeting(targetObjectId))
								return;
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
								targetObjectId, 3000, 0));
							PacketSendUtility.broadcastPacket(player,
								new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
							{
								qs.setQuestVarById(0, 2);
								updateQuestStatus(env);
							}
						}
					}, 3000);
				}
			}

			case 730191:
			{
				if(qs.getQuestVarById(0) == 2 && env.getDialogId() == -1)
				{
					final int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId,
						3000, 1));
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
					ThreadPoolManager.getInstance().schedule(new Runnable(){
						@Override
						public void run()
						{
							if(!player.isTargeting(targetObjectId))
								return;
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
								targetObjectId, 3000, 0));
							PacketSendUtility.broadcastPacket(player,
								new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
							qs.setQuestVarById(0, 3);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
						}
					}, 3000);
				}
			}
		}
	}

	else if(qs.getStatus() == QuestStatus.REWARD)
	{
		if(targetId == 204588)
			return defaultQuestEndDialog(env);
	}
	return false;
      }
}
