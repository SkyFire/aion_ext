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
package quest.heiron;

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
 * @author Rhys2002
 */
public class _1055EternalRest extends QuestHandler {

    private final static int questId = 1055;
    private final static int[] npc_ids = {204629, 204625, 204628, 204627, 204626, 204622, 700270};

    public _1055EternalRest() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 1500);
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
            if (targetId == 204629)
                return defaultQuestEndDialog(env);
        } else if (qs.getStatus() != QuestStatus.START) {
            return false;
        }
        if (targetId == 204629) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 0)
                        return sendQuestDialog(env, 1011);
                    else if (var == 2)
                        return sendQuestDialog(env, 1693);
                case 10000:
                    if (var == 0) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                case 10001:
                    if (var == 1) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
            }
        } else if (targetId == 204625) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 1)
                        return sendQuestDialog(env, 1352);
                    else if (var == 2)
                        return sendQuestDialog(env, 1693);
                    else if (var == 4)
                        return sendQuestDialog(env, 2375);
                case 33:
                    if (QuestService.collectItemCheck(env, true)) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        ItemService.addItems(player, Collections.singletonList(new QuestItems(182201613, 1)));
                        return sendQuestDialog(env, 10000);
                    } else
                        return sendQuestDialog(env, 10001);
                case 10001:
                    if (var == 1) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                case 10255:
                    if (var == 4) {
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
            }
        } else if (targetId == 204628) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 2)
                        return sendQuestDialog(env, 1694);
                case 10002:
                    if (var == 2) {
                        if (player.getInventory().getItemCountByItemId(182201609) == 0)
                            ItemService.addItems(player, Collections.singletonList(new QuestItems(182201609, 1)));
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
            }
        } else if (targetId == 204627) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 2)
                        return sendQuestDialog(env, 1781);
                case 10002:
                    if (var == 2) {
                        if (player.getInventory().getItemCountByItemId(182201610) == 0)
                            ItemService.addItems(player, Collections.singletonList(new QuestItems(182201610, 1)));
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
            }
        } else if (targetId == 204626) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 2)
                        return sendQuestDialog(env, 1864);
                case 10002:
                    if (var == 2) {
                        if (player.getInventory().getItemCountByItemId(182201611) == 0)
                            ItemService.addItems(player, Collections.singletonList(new QuestItems(182201611, 1)));
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
            }
        } else if (targetId == 204622) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 2)
                        return sendQuestDialog(env, 1949);
                case 10002:
                    if (var == 2) {
                        if (player.getInventory().getItemCountByItemId(182201612) == 0)
                            ItemService.addItems(player, Collections.singletonList(new QuestItems(182201612, 1)));
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
            }
        } else if (targetId == 700270 && qs.getQuestVarById(0) == 3) {
            if (env.getDialogId() == -1) {
                final int targetObjectId = env.getVisibleObject().getObjectId();
                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
                        player.getInventory().removeFromBagByItemId(182201613, 1);
                        qs.setQuestVarById(0, 4);
                        updateQuestStatus(env);
                    }
                }, 3000);
            }
        }
        return false;
    }
}
