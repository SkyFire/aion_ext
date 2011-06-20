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

package quest.altgard;

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

/**
 * @author Mr. Poke
 */
public class _2208MauInTenMinutesADay extends QuestHandler {

    private final static int questId = 2208;

    public _2208MauInTenMinutesADay() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203591).addOnQuestStart(questId);
        qe.setNpcQuestData(203591).addOnTalkEvent(questId);
        qe.setNpcQuestData(203589).addOnTalkEvent(questId);
        qe.setQuestItemIds(182203205).add(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203591) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else if (env.getDialogId() == 1002) {
                    if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182203205, 1))))
                        return defaultQuestStartDialog(env);
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 203589) {
                int var = qs.getQuestVarById(0);
                if (env.getDialogId() == 25) {
                    if (var == 0)
                        return sendQuestDialog(env, 1693);
                    else if (var == 1)
                        return sendQuestDialog(env, 1352);
                } else if (env.getDialogId() == 10000) {
                    qs.setQuestVar(2);
                    updateQuestStatus(env);
                    qs.setQuestVar(1);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
                    return true;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203591) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 2375);
                else
                    return defaultQuestEndDialog(env);
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

        if (id != 182203205)
            return false;
        if (qs == null)
            return false;
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                player.getInventory().removeFromBagByObjectId(itemObjId, 1);
                qs.setQuestVarById(0, 1);
                updateQuestStatus(env);
            }
        }, 3000);
        return true;
    }
}
