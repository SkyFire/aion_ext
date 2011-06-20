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

package quest.theobomos;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.*;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.Collections;

public class _1094ProjectDrakanhammer extends QuestHandler {


    private final static int questId = 1094;
    private final static int[] npc_ids = {203834, 798155, 700411, 730153};

    public _1094ProjectDrakanhammer() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setQuestItemIds(182208017).add(questId);
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 1093);
    }

    @Override
    public boolean onDialogEvent(final QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203834) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else if (env.getDialogId() == 1009)
                    return sendQuestDialog(env, 5);
                else {
                    QuestService.collectItemCheck(env, true);
                    return defaultQuestEndDialog(env);
                }
            }
            return false;
        } else if (qs.getStatus() != QuestStatus.START) {
            return false;
        }
        if (targetId == 203834) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 0)
                        return sendQuestDialog(env, 1011);
                case 10000:
                    if (var == 0) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        } else if (targetId == 798155) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 1)
                        return sendQuestDialog(env, 1352);
                case 1353:
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 367));
                    break;
                case 10001:
                    if (var == 1) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        } else if (targetId == 700411) {
            switch (env.getDialogId()) {
                case -1:
                    if (var == 2 && player.getInventory().getItemCountByItemId(182208017) == 0) {
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

                                ItemService.addItems(player, Collections.singletonList(new QuestItems(182208017, 1)));
                            }

                        }, 3000);
                        return false;
                    }
            }
        } else if (targetId == 730153) {
            switch (env.getDialogId()) {
                case -1:
                    if (var == 3) {
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
                                player.getInventory().removeFromBagByItemId(182208018, 1);
                                player.getInventory().removeFromBagByItemId(182208019, 1);
                                player.getInventory().removeFromBagByItemId(182208020, 1);
                                player.getInventory().removeFromBagByItemId(182208021, 1);
                                qs.setStatus(QuestStatus.REWARD);
                                qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                                updateQuestStatus(env);
                            }

                        }, 3000);
                        return false;
                    }
            }
        }
        return false;
    }

    @Override
    public boolean onItemUseEvent(final QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (id != 182208017)
            return false;
        if (qs == null || qs.getQuestVarById(0) != 2)
            return false;
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 1000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                player.getInventory().removeFromBagByItemId(182208017, 1);
                qs.setQuestVarById(0, 3);
                updateQuestStatus(env);
            }
        }, 1000);
        return true;
    }
}
