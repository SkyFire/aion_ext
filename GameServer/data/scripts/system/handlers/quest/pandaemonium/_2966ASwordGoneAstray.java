package quest.pandaemonium;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

/**
 * @author Altaress
 */
public class _2966ASwordGoneAstray extends QuestHandler {
    private final static int questId = 2966;

    public _2966ASwordGoneAstray() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(278055).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        if (targetId == 0) {
            if (env.getDialogId() == 1002) {
                QuestService.startQuest(env, QuestStatus.START);
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
                return true;
            }
        } else if (targetId == 278055) {
            if (qs != null) {
                if (env.getDialogId() == 25 && qs.getStatus() == QuestStatus.START) {
                    return sendQuestDialog(env, 2375);
                } else if (env.getDialogId() == 1009) {
                    player.getInventory().removeFromBagByItemId(182207044, 1);
                    qs.setQuestVar(1);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    return defaultQuestEndDialog(env);
                } else
                    return defaultQuestEndDialog(env);
            }
        }
        return false;
    }
}
