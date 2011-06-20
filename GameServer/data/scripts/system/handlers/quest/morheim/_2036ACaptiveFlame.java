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
package quest.morheim;

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
 * @author Hellboy aion4Free
 */
public class _2036ACaptiveFlame extends QuestHandler {
    private final static int questId = 2036;
    private final static int[] npc_ids = {204407, 204408, 700236, 204317};

    public _2036ACaptiveFlame() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(212878).addOnKillEvent(questId);
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 2035);
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
                case 204407: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 0)
                                return sendQuestDialog(env, 1011);
                            else if (var == 4)
                                return sendQuestDialog(env, 2375);
                        case 10000:
                            if (var == 0) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                        case 33:
                            if (var == 4) {
                                if (QuestService.collectItemCheck(env, true)) {
                                    qs.setStatus(QuestStatus.REWARD);
                                    updateQuestStatus(env);
                                    return sendQuestDialog(env, 10000);
                                } else
                                    return sendQuestDialog(env, 10001);
                            }
                    }
                }
                break;
                case 204408: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 1)
                                return sendQuestDialog(env, 1352);
                        case 1353:
                            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 79));
                            break;
                        case 10001:
                            if (var == 1) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                    }
                }
                break;
                case 700236:
                    switch (env.getDialogId()) {
                        case -1:
                            if (player.getInventory().getItemCountByItemId(182204014) == 0) {
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
                                        ItemService.addItems(player, Collections.singletonList(new QuestItems(182204014, 1)));
                                    }

                                }, 3000);
                                return false;
                            }
                    }
                    break;
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204317) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else
                    return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 212878, 2, 3)) {
            Player player = env.getPlayer();
            QuestState qs = player.getQuestStateList().getQuestState(questId);
            qs.setQuestVar(4);
            updateQuestStatus(env);
            return true;
        } else
            return false;
    }
}
