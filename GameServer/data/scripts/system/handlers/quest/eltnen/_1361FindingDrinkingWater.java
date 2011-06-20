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
package quest.eltnen;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.ZoneService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;

import java.util.Collections;

/**
 * @author Xitanium
 */
public class _1361FindingDrinkingWater extends QuestHandler {

    private final static int questId = 1361;

    public _1361FindingDrinkingWater() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setQuestItemIds(182201326).add(questId); //Empty Bucket
        qe.setNpcQuestData(203943).addOnQuestStart(questId); //Turiel start
        qe.setNpcQuestData(203943).addOnTalkEvent(questId); //Turiel talk
        qe.setNpcQuestData(700173).addOnTalkEvent(questId); //Water tank
    }

    @Override
    public boolean onItemUseEvent(final QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (id != 182201326) //Empty Bucket
            return false;
        if (!ZoneService.getInstance().isInsideZone(player, ZoneName.MYSTIC_SPRING_OF_ANATHE))
            return false;
        if (qs == null)
            return false;
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                player.getInventory().removeFromBagByObjectId(itemObjId, 1);
                ItemService.addItems(player, Collections.singletonList(new QuestItems(182201327, 1)));
                qs.setQuestVar(1);
                updateQuestStatus(env);
            }
        }, 3000);
        return false;
    }

    @Override
    public boolean onDialogEvent(final QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203943) //Turiel
            {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else if (env.getDialogId() == 1002) {
                    if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182201326, 1))))
                        return defaultQuestStartDialog(env);
                    else
                        return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) //Reward
        {
            if (env.getDialogId() == 25)
                return sendQuestDialog(env, 2375);
            else if (env.getDialogId() == 1009) {
                qs.setQuestVar(2);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                return defaultQuestEndDialog(env);
            } else
                return defaultQuestEndDialog(env);
        } else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
            switch (targetId) {
                case 700173: //Water Tank
                {
                    if (qs.getQuestVarById(0) == 1 && env.getDialogId() == -1) {

                        final int targetObjectId = env.getVisibleObject().getObjectId();
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
                                1));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                                targetObjectId), true);
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                final QuestState qs = player.getQuestStateList().getQuestState(questId);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                player.getInventory().removeFromBagByItemId(182201327, 1);
                                if (player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId)
                                    return;
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
                                        targetObjectId, 3000, 0));
                                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                                        targetObjectId), true);
                            }
                        }, 3000);
                    }
                }
            }
        }
        return false;
    }
}
