/**
 * This file is part of Aion X Emu <aionxemu.com>
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package quest.brusthonin;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
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
 *
 *
 */
public class _4038AlasPoorGroznak extends QuestHandler {

    private final static int questId = 4038;
    private final static int[] mob_ids = {214555};

    public _4038AlasPoorGroznak() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(205150).addOnQuestStart(questId); //Surt
        qe.setNpcQuestData(205150).addOnTalkEvent(questId);
        qe.setNpcQuestData(730155).addOnTalkEvent(questId); //Groznak's Skull
        qe.setNpcQuestData(700380).addOnTalkEvent(questId); //Weathered Skeleton
        qe.setNpcQuestData(700381).addOnTalkEvent(questId); //Intact Skeleton
        qe.setNpcQuestData(700382).addOnTalkEvent(questId); //Muddy Skeleton
        for (int mob_id : mob_ids)
            qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 214555, 2, true))
            return true;
        else
            return false;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        Npc npc = null;
        if (env.getVisibleObject() instanceof Npc)
            npc = (Npc) env.getVisibleObject();
        targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 205150) //Surt
        {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 4762);
                else
                    return defaultQuestStartDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 2034);
                else if (env.getDialogId() == 1009) {
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    return defaultQuestEndDialog(env);
                } else
                    return defaultQuestEndDialog(env);
            }
        } else if (targetId == 730155) //Groznak's Skull
        {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else if (env.getDialogId() == 10000) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 33) {
                    if (QuestService.collectItemCheck(env, true)) {
                        qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                        updateQuestStatus(env);
                        PacketSendUtility
                                .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    } else
                        return sendQuestDialog(env, 10001);
                } else
                    return defaultQuestStartDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1693);
                else if (env.getDialogId() == 10002) {
                    QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 214555, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true); //Spawn Groznak's Servant
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    ((Npc) player.getTarget()).getController().onDie(null);
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        } else if (qs != null && qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 700380: //Weathered Skeleton
                {
                    if (qs.getQuestVarById(0) == 1 && env.getDialogId() == -1) {

                        final int targetObjectId = env.getVisibleObject().getObjectId();
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
                                1));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                                targetObjectId), true);
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @SuppressWarnings("unused")
                            final QuestState qs = player.getQuestStateList().getQuestState(questId);

                            @Override
                            public void run() {
                                ItemService.addItems(player, Collections.singletonList(new QuestItems(182209019, 1)));
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

            switch (targetId) {
                case 700381: //Intact Skeleton
                {
                    if (qs.getQuestVarById(0) == 1 && env.getDialogId() == -1) {

                        final int targetObjectId = env.getVisibleObject().getObjectId();
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
                                1));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                                targetObjectId), true);
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @SuppressWarnings("unused")
                            final QuestState qs = player.getQuestStateList().getQuestState(questId);

                            @Override
                            public void run() {
                                ItemService.addItems(player, Collections.singletonList(new QuestItems(182209020, 1)));
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

            switch (targetId) {
                case 700382: //Muddy Skeleton
                {
                    if (qs.getQuestVarById(0) == 1 && env.getDialogId() == -1) {

                        final int targetObjectId = env.getVisibleObject().getObjectId();
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
                                1));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                                targetObjectId), true);
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @SuppressWarnings("unused")
                            final QuestState qs = player.getQuestStateList().getQuestState(questId);

                            @Override
                            public void run() {
                                ItemService.addItems(player, Collections.singletonList(new QuestItems(182209021, 1)));
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
