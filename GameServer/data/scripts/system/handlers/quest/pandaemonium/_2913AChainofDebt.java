package quest.pandaemonium;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.Collections;

/*
 * author : Altaress
 */
public class _2913AChainofDebt extends QuestHandler {
    private final static int questId = 2913;

    public _2913AChainofDebt() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204193).addOnQuestStart(questId);
        qe.setNpcQuestData(204193).addOnTalkEvent(questId);
        qe.setNpcQuestData(204170).addOnTalkEvent(questId);
        qe.setNpcQuestData(798065).addOnTalkEvent(questId);
        qe.setNpcQuestData(204173).addOnTalkEvent(questId);
        qe.setQuestItemIds(182207006).add(questId);
    }

    @Override
    public boolean onItemUseEvent(QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        switch (id) {
            case 182207006:
                if (qs == null || qs.getStatus() != QuestStatus.START)
                    return false;
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 1000, 0, 0), true);
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                    }
                }, 1000);
                return true;
        }
        return false;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 204193) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else if (env.getDialogId() == 1002) {
                    if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182207006, 1))))
                        return defaultQuestStartDialog(env);
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        } else if (targetId == 204170) {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 10000) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
                    return true;
                }
            }
        } else if (targetId == 798065) {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1693);
                else if (env.getDialogId() == 10001) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
                    return true;
                }
            }
        } else if (targetId == 204173) {
            if (qs != null) {
                if (env.getDialogId() == 25 && qs.getStatus() == QuestStatus.START)
                    return sendQuestDialog(env, 2375);
                else if (env.getDialogId() == 1009) {
                    qs.setQuestVar(3);
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