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

package quest.reshanta;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

import java.util.Collections;

public class _1071SpeakingBalaur extends QuestHandler {
    private final static int questId = 1071;

    public _1071SpeakingBalaur() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setQuestItemIds(182202001).add(questId);
        qe.setNpcQuestData(278532).addOnTalkEvent(questId);
        qe.setNpcQuestData(798026).addOnTalkEvent(questId);
        qe.setNpcQuestData(798025).addOnTalkEvent(questId);
        qe.setNpcQuestData(279019).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 1701);
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

        if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 278532) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else if (env.getDialogId() == 1009)
                    return sendQuestDialog(env, 5);
                else return defaultQuestEndDialog(env);
            }
            return false;
        } else if (qs.getStatus() != QuestStatus.START) {
            return false;
        }
        if (targetId == 278532) {
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
                    return false;
            }
        } else if (targetId == 798026) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 1)
                        return sendQuestDialog(env, 1352);
                    else if (var == 4)
                        return sendQuestDialog(env, 2375);
                    else if (var == 6 || var == 8)
                        return sendQuestDialog(env, 3057);
                case 10004:
                    if (var == 4) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        player.getInventory().removeFromBagByItemId(182202002, 1);
                        ItemService.addItems(player, Collections.singletonList(new QuestItems(182202001, 1)));
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
                case 10006:
                    if (var == 6 || var == 8) {
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;

                case 10010:
                    Storage inventory = player.getInventory();
                    Item KinahsItemPlayer = inventory.getKinahItem();
                    long KinahsPlayer = KinahsItemPlayer.getItemCount();
                    if (var == 1 && KinahsPlayer >= 20000) {
                        KinahsItemPlayer.decreaseItemCount(20000);
                        PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(KinahsItemPlayer));
                        ItemService.addItems(player, Collections.singletonList(new QuestItems(182202001, 1)));
                        qs.setQuestVar(7);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    } else
                        return sendQuestDialog(env, 1355);
                case 10011:
                    if (var == 1) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
            }
        } else if (targetId == 798025) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 2)
                        return sendQuestDialog(env, 1693);
                case 10002:
                    if (var == 2) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
            }
        } else if (targetId == 279019) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 3)
                        return sendQuestDialog(env, 2034);
                    return false;
                case 10003:
                    if (var == 3) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        ItemService.addItems(player, Collections.singletonList(new QuestItems(182202002, 1)));
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
            }
        }

        return false;
    }

    @Override
    public boolean onItemUseEvent(QuestCookie env, Item item) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (id != 182202001)
            return false;
        if (qs == null)
            return false;

        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 1, 1, 0), true);
        player.getInventory().removeFromBagByItemId(182202001, 1);
        qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
        updateQuestStatus(env);
        return true;
    }
}
