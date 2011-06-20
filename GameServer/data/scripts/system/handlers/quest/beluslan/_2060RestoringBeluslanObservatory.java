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

package quest.beluslan;

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
import gameserver.services.ZoneService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;

import java.util.Collections;


/**
 *
 *
 */
public class _2060RestoringBeluslanObservatory extends QuestHandler {
    private final static int questId = 2060;
    private final static int[] npc_ids = {204701, 204785, 278003, 278088, 700293};

    public _2060RestoringBeluslanObservatory() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setQuestEnterZone(ZoneName.MALEK_MINE_220040000).add(questId);
        qe.addQuestLvlUp(questId);
        qe.setQuestItemIds(182204318).add(questId);
        qe.setNpcQuestData(700290).addOnKillEvent(questId);
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 2500);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 700290, 5, 8))
            return true;
        else
            return false;
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
            if (targetId == 204701) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else return defaultQuestEndDialog(env);
            }
            return false;
        } else if (qs.getStatus() != QuestStatus.START) {
            return false;
        }
        if (targetId == 204701) {
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
        } else if (targetId == 204785) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 1)
                        return sendQuestDialog(env, 1352);
                    else if (var == 4)
                        return sendQuestDialog(env, 2375);
                case 10001:
                    if (var == 1) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                case 10004:
                    if (var == 4) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                case 33:
                    if (var == 4) {
                        if (QuestService.collectItemCheck(env, false)) {
                            return sendQuestDialog(env, 2376);
                        } else
                            return sendQuestDialog(env, 2461);
                    }
            }
        } else if (targetId == 278003) {
            switch (env.getDialogId()) {
                case -1:
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
        } else if (targetId == 278088) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 3)
                        return sendQuestDialog(env, 2034);
                case 2035:
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 281));
                    break;
                case 10003:
                    if (var == 3) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        ItemService.addItems(player, Collections.singletonList(new QuestItems(182204318, 1)));
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        } else if (targetId == 700293 && var == 8) {
            if (env.getDialogId() == -1) {
                final int targetObjectId = env.getVisibleObject().getObjectId();
                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
                        player.getInventory().removeFromBagByItemId(182204319, 1);
                        PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 254));
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                    }
                }, 3000);
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean onItemUseEvent(QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (qs == null)
            return false;
        if (qs.getQuestVarById(0) != 4)
            return false;
        if (id != 182204318)
            return false;
        if (!ZoneService.getInstance().isInsideZone(player, ZoneName.LEIBO_ISLAND_400010000))
            return false;
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                player.getInventory().removeFromBagByObjectId(itemObjId, 1);
                ItemService.addItems(player, Collections.singletonList(new QuestItems(182204319, 1)));
            }
        }, 3000);
        return true;
    }
}
