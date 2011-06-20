/*
 * This file is part of Aion X EMU <aionxemu.com>.
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.pandaemonium;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

/**
 * @author Hellboy aion4Free
 */
public class _2947FollowingThrough extends QuestHandler {
    private final static int questId = 2947;

    public _2947FollowingThrough() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(204053).addOnTalkEvent(questId);
        qe.setNpcQuestData(204301).addOnTalkEvent(questId);
        qe.setNpcQuestData(212396).addOnKillEvent(questId);
        qe.setNpcQuestData(212611).addOnKillEvent(questId);
        qe.setNpcQuestData(212408).addOnKillEvent(questId);
        qe.setNpcQuestData(204089).addOnTalkEvent(questId);
        qe.setNpcQuestData(700368).addOnTalkEvent(questId);
        qe.setNpcQuestData(700369).addOnTalkEvent(questId);
        qe.setNpcQuestData(210343).addOnKillEvent(questId);
        qe.setNpcQuestData(700268).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 204053: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 0)
                                return sendQuestDialog(env, 1011);
                        case 10010:
                            if (var == 0) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                        case 10011:
                            if (var == 0) {
                                qs.setQuestVarById(0, var + 4);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                        case 10012:
                            if (var == 0) {
                                qs.setQuestVarById(0, var + 9);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                        case 33:
                            if (var == 1 || var == 4 || var == 9) {
                                qs.setQuestVarById(0, 0);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 2375);
                            }
                    }
                }
                break;
                case 204301: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 1)
                                return sendQuestDialog(env, 1352);
                            else if (var == 2)
                                return sendQuestDialog(env, 3398);
                            else if (var == 7)
                                return sendQuestDialog(env, 3739);
                            else if (var == 9) {
                                if (QuestService.collectItemCheck(env, true)) {
                                    qs.setStatus(QuestStatus.REWARD);
                                    updateQuestStatus(env);
                                    return sendQuestDialog(env, 7);
                                } else
                                    return sendQuestDialog(env, 4080);
                            }
                        case 10001:
                            if (var == 1) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                        case 1009:
                            if (var == 2) {
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            }
                            if (var == 7) {
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 6);
                            }
                    }
                }
                break;
                case 204089: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 4)
                                return sendQuestDialog(env, 1693);
                            else if (var == 5)
                                return sendQuestDialog(env, 2034);
                        case 10002:
                            if (var == 4) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                TeleportService.teleportTo(player, 320090000, 276, 293, 163, 90);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                        case 10003:
                            if (var == 5) {
                                qs.setQuestVarById(0, 7);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                    }
                }
                break;
                case 700368: {
                    switch (env.getDialogId()) {
                        case -1:
                            if (var == 5) {
                                final int targetObjectId = env.getVisibleObject().getObjectId();
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
                                        1));
                                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                                        targetObjectId), true);
                                ThreadPoolManager.getInstance().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        Npc npc = (Npc) player.getTarget();
                                        if (npc == null || npc.getObjectId() != targetObjectId)
                                            return;
                                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
                                                targetObjectId, 3000, 0));
                                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                                                targetObjectId), true);

                                        TeleportService.teleportTo(player, 320090000, 276, 293, 163, 90);
                                    }

                                }, 3000);
                            }
                            return false;
                    }
                }
                break;
                case 700369: {
                    switch (env.getDialogId()) {
                        case -1:
                            if (var == 5) {
                                final int targetObjectId = env.getVisibleObject().getObjectId();
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
                                        1));
                                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                                        targetObjectId), true);
                                ThreadPoolManager.getInstance().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        Npc npc = (Npc) player.getTarget();
                                        if (npc == null || npc.getObjectId() != targetObjectId)
                                            return;
                                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
                                                targetObjectId, 3000, 0));
                                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                                                targetObjectId), true);

                                        TeleportService.teleportTo(player, 120010000, 982, 1556, 210, 90);
                                    }

                                }, 3000);
                            }
                            return false;
                    }
                }
                break;
                case 700268: {
                    switch (env.getDialogId()) {
                        case -1:
                            if (var == 9) {
                                final int targetObjectId = env.getVisibleObject().getObjectId();
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
                                        1));
                                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                                        targetObjectId), true);
                                ThreadPoolManager.getInstance().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        Npc npc = (Npc) player.getTarget();
                                        if (npc == null || npc.getObjectId() != targetObjectId)
                                            return;
                                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
                                                targetObjectId, 3000, 0));
                                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                                                targetObjectId), true);

                                    }

                                }, 3000);
                                return true;
                            }
                    }
                }
                break;
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204301) {
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 212396, 0, 3, 1) || defaultQuestOnKillEvent(env, 212611, 0, 3, 2) || defaultQuestOnKillEvent(env, 212408, 0, 3, 3) || defaultQuestOnKillEvent(env, 210343, 0, 10, 4))
            return true;
        else
            return false;
    }

}
