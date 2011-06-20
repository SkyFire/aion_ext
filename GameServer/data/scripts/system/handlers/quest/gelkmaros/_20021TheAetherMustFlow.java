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
package quest.gelkmaros;

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
 * @author Nephis
 */
public class _20021TheAetherMustFlow extends QuestHandler //TODO: Effect item
{

    private final static int questId = 20021;
    private final static int[] npc_ids = {799226, 799247, 799250, 799325, 215488, 799503, 799258, 799239};

    public _20021TheAetherMustFlow() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(215992).addOnKillEvent(questId);
        qe.setNpcQuestData(215995).addOnKillEvent(questId);
        qe.setNpcQuestData(215488).addOnKillEvent(questId);
        qe.setQuestItemIds(182207604).add(questId);
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) // FIXME: FIX VAR!!
    {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START)
            return false;

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        switch (targetId) {
            case 215995:
            case 215992:
                if (qs.getQuestVarById(1) < 10) {
                    qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
                    updateQuestStatus(env);
                    return true;
                } else if (qs.getQuestVarById(1) == 10) {
                    qs.setQuestVar(5);
                    updateQuestStatus(env);
                    return true;
                }
                break;
            case 215488:
                if (qs.getQuestVarById(0) == 8) {
                    qs.setQuestVar(9);
                    updateQuestStatus(env);
                    return true;
                }
        }
        return false;
    }

    @Override
    public boolean onItemUseEvent(final QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int var = qs.getQuestVarById(0);
        if (var == 7) {
            if (id != 182207603)
                return false;

            PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    int var = qs.getQuestVarById(0);
                    PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                    qs.setQuestVarById(0, var + 1);
                    updateQuestStatus(env);
                }
            }, 3000);
            return true;

        } else if (var == 6) {
            if (id != 182207604)
                return false;

            PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    int var = qs.getQuestVarById(0);
                    PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                    player.getInventory().removeFromBagByObjectId(itemObjId, 1);
                    qs.setQuestVarById(0, var + 1);
                    updateQuestStatus(env);
                }
            }, 3000);
            return true;
        }
        return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 20020);
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
            if (targetId == 799226) {
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
        if (targetId == 799226) {
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
        } else if (targetId == 799247) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 1)
                        return sendQuestDialog(env, 1352);
                case 10001:
                    if (var == 1) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        } else if (targetId == 799250) {
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
            }
        } else if (targetId == 799325) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 3)
                        return sendQuestDialog(env, 2034);
                    else if (var == 5)
                        return sendQuestDialog(env, 2716);
                case 10003:
                    if (var == 3) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                case 10005:
                    if (var == 5) {
                        if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182207604, 1))))
                            return true;
                        if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182207603, 1))))
                            return true;
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        } else if (targetId == 799503) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 9)
                        return sendQuestDialog(env, 4080);
                case 10009:
                    if (var == 9) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        } else if (targetId == 799258) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 10)
                        return sendQuestDialog(env, 1267);
                case 10010:
                    if (var == 10) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        } else if (targetId == 799239) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 11)
                        return sendQuestDialog(env, 1608);
                case 10255:
                    if (var == 11) {
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        }

        return false;
    }
}
