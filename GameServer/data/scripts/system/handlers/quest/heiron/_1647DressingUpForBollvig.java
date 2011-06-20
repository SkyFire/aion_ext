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

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.Collections;
import java.util.List;

/**
 * @author Balthazar
 */

public class _1647DressingUpForBollvig extends QuestHandler {
    private final static int questId = 1647;

    public _1647DressingUpForBollvig() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(790019).addOnQuestStart(questId);
        qe.setNpcQuestData(790019).addOnTalkEvent(questId);
        qe.setQuestItemIds(182201783).add(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 790019) {
                switch (env.getDialogId()) {
                    case 25: {
                        return sendQuestDialog(env, 4762);
                    }
                    case 1002: {
                        if (player.getInventory().getItemCountByItemId(182201783) == 0) {
                            if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182201783, 1)))) {
                                return true;
                            }
                        }
                    }
                    default:
                        return defaultQuestStartDialog(env);
                }
            }
        }

        if (qs == null)
            return false;

        if (qs.getStatus() == QuestStatus.REWARD) {
            switch (targetId) {
                case 790019: {
                    switch (env.getDialogId()) {
                        case 25: {
                            return sendQuestDialog(env, 10002);
                        }
                        case 1009: {
                            return sendQuestDialog(env, 5);
                        }
                        default:
                            return defaultQuestEndDialog(env);
                    }
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

        if (id != 182201783) {
            return false;
        }
        if (qs == null) {
            return false;
        }

        int var = qs.getQuestVars().getQuestVars();
        if (var != 0) {
            return false;
        }

        if (qs.getStatus() != QuestStatus.START) {
            return false;
        }

        if (MathUtil.getDistance(1677, 2520, 100, player.getPosition().getX(), player.getPosition().getY(), player
                .getPosition().getZ()) > 5) {
            return false;
        }

        int itemId1 = 110100150;
        int itemId2 = 113100144;
        boolean CheckitemId1 = false;
        boolean CheckitemId2 = false;

        List<Item> items1 = player.getEquipment().getEquippedItemsByItemId(itemId1);
        for (@SuppressWarnings("unused")
        Item ListeCheckitemId1 : items1) {
            CheckitemId1 = true;
        }

        List<Item> items2 = player.getEquipment().getEquippedItemsByItemId(itemId2);
        for (@SuppressWarnings("unused")
        Item ListeCheckitemId2 : items2) {
            CheckitemId2 = true;
        }

        if (!CheckitemId1 && CheckitemId2 || CheckitemId1 && !CheckitemId2 || !CheckitemId1 && !CheckitemId2) {
            return false;
        }

        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id,
                3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId,
                        id, 0, 1, 0), true);
                player.getInventory().removeFromBagByObjectId(itemObjId, 1);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
            }
        }, 3000);
        return true;
    }
}