package quest.pandaemonium;

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

import java.util.Collections;

/*
 * author : Altaress
 */
public class _2914ATokenofLostLove extends QuestHandler {
    private final static int questId = 2914;

    public _2914ATokenofLostLove() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204147).addOnQuestStart(questId);
        qe.setNpcQuestData(204147).addOnTalkEvent(questId);
        qe.setNpcQuestData(204236).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 204147) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else
                    return defaultQuestStartDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 2375);
                else
                    return defaultQuestEndDialog(env);
            }
        } else if (targetId == 204236) {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 10000) {
                    qs.setQuestVarById(0, 1);
                    updateQuestStatus(env);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    ItemService.addItems(player, Collections.singletonList(new QuestItems(182207014, 1)));
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
                    return true;
                }
            }
        }
        return false;
    }
} 