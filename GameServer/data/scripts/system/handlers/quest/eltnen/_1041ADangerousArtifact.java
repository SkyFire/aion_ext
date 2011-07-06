/*
 * This file is part of Aion X EMU <aionxemu.com>.
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.eltnen;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.Collections;

/**
 * @author Xitanium
 */
public class _1041ADangerousArtifact extends QuestHandler {

    private final static int questId = 1041;

    public _1041ADangerousArtifact() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203901).addOnTalkEvent(questId); //Telemachus
        qe.setNpcQuestData(204015).addOnTalkEvent(questId); //Civil Engineer
        qe.setNpcQuestData(700267).addOnTalkEvent(questId); //Secret Exit TEMPORARY FIX
        qe.setNpcQuestData(203833).addOnTalkEvent(questId); //Xenophon
        qe.setNpcQuestData(278500).addOnTalkEvent(questId); //Yuditio
        qe.setNpcQuestData(204042).addOnTalkEvent(questId); //Laigas
        qe.setNpcQuestData(700181).addOnTalkEvent(questId); //Stolen Artifact
        qe.addQuestLvlUp(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }

    @Override
    public boolean onDialogEvent(final QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;
        if (targetId == 203901) //Telemachus
        {
            if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else if (env.getDialogId() == 10000) {
                    qs.setQuestVar(1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
                            .getObjectId(), 10));
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 184));		
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            } else if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 3) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1693);
                else if (env.getDialogId() == 10002) {
                    qs.setQuestVar(4);
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            } else if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 6) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 2716);
                else if (env.getDialogId() == 10005) {
                    qs.setQuestVar(7);
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            } else if (qs.getStatus() == QuestStatus.REWARD) {
                return defaultQuestEndDialog(env);
            }
        } else if (targetId == 204015) //Civil Engineer
        {
            if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 10001) {
                    qs.setQuestVar(2);
                    updateQuestStatus(env);
                    QuestService.addNewSpawn(210020000, player.getInstanceId(), 700267, (float) 2265.621, (float) 2357.8164, (float) 277.8047, (byte) 0, true);
                    QuestService.addNewSpawn(210020000, player.getInstanceId(), 700267, (float) 1827.1799, (float) 2537.9143, (float) 267.5, (byte) 0, true);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);

            }

        } else if (targetId == 203833) //Xenophon
        {
            if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 4) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 2034);
                else if (env.getDialogId() == 10003) {
                    qs.setQuestVar(5);
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);

            }
        } else if (targetId == 278500) //Yuditio
        {
            if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 5) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 2375);
                else if (env.getDialogId() == 10004) {
                    qs.setQuestVar(6);
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);

            }
        } else if (targetId == 204042) //Laigas
        {
            if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 7) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 3057);
                else if (env.getDialogId() == 10006) {
                    qs.setQuestVar(8);
                    ItemService.addItems(player, Collections.singletonList(new QuestItems(182201011, 1)));
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 37));
                    return true;
                } else
                    return defaultQuestStartDialog(env);

            } else if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 9) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 3398);
                else if (env.getDialogId() == 10007) {
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 38));
                    return true;
                } else
                    return defaultQuestStartDialog(env);

            }

        } else if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2) {
            switch (targetId) {
                case 700267: //TEMPORARY SECRET PASSAGE
                {
                    if (qs.getQuestVarById(0) == 2 && env.getDialogId() == -1) {

                        final int targetObjectId = env.getVisibleObject().getObjectId();
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
                                1));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                                targetObjectId), true);
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            final QuestState qs = player.getQuestStateList().getQuestState(questId);

                            @Override
                            public void run() {
                                qs.setQuestVar(3);
                                updateQuestStatus(env);
                                if (player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId)
                                    return;
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
                                        targetObjectId, 3000, 0));
                                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                                        targetObjectId), true);
                            }
                        }, 3000);
                    }
                }

            }
        } else if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 8) {
            switch (targetId) {
                case 700181: //Stolen Artifact
                {
                    if (qs.getQuestVarById(0) == 8 && env.getDialogId() == -1) {

                        final int targetObjectId = env.getVisibleObject().getObjectId();
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
                                1));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                                targetObjectId), true);
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            final QuestState qs = player.getQuestStateList().getQuestState(questId);

                            @Override
                            public void run() {
                                qs.setQuestVar(9);
                                updateQuestStatus(env);
                                player.getInventory().removeFromBagByItemId(182201011, 1);
                                if (player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId)
                                    return;
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
                                        targetObjectId, 3000, 0));
                                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                                        targetObjectId), true);
                            }
                        }, 3000);
                    }
                }
            }
        }

        return false;
    }

}
