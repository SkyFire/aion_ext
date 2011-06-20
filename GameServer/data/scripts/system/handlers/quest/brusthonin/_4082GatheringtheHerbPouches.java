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
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.Collections;

/**
 *
 *
 */
public class _4082GatheringtheHerbPouches extends QuestHandler {
    private final static int questId = 4082;

    public _4082GatheringtheHerbPouches() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(205190).addOnQuestStart(questId);
        qe.setNpcQuestData(205190).addOnTalkEvent(questId);
        qe.setNpcQuestData(700430).addOnTalkEvent(questId);
        qe.setNpcQuestData(700431).addOnTalkEvent(questId);
        qe.setNpcQuestData(700432).addOnTalkEvent(questId);
        qe.setQuestItemIds(182209058).add(questId);
    }

    @Override
    public boolean onItemUseEvent(QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (id != 182209058)
            return false;
        if (qs == null || qs.getQuestVarById(0) != 0)
            return false;
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
            }
        }, 3000);
        return true;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 205190) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else if (env.getDialogId() == 1002) {
                    if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182209058, 1))))
                        return defaultQuestStartDialog(env);
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }

            if (qs != null && qs.getStatus() == QuestStatus.START) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 2375);
                else if (env.getDialogId() == 33) {
                    if (QuestService.collectItemCheck(env, true)) {
                        player.getInventory().removeFromBagByItemId(182209058, 1);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 5);
                    } else
                        return sendQuestDialog(env, 2716);
                } else
                    return defaultQuestEndDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD)
                return defaultQuestEndDialog(env);
        } else if (qs != null && qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 700430:
                case 700431:
                case 700432: {
                    if (qs.getQuestVarById(0) == 0 && env.getDialogId() == -1)
                        return true;
                }
            }
        }
        return false;
    }
}
