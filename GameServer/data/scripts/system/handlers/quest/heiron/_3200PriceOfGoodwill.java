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
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldMapInstance;

/**
 * @author kecimis
 */
public class _3200PriceOfGoodwill extends QuestHandler {

    private final static int questId = 3200;
    private final static int[] npc_ids = {204658, 798332, 700522, 279006, 798322};
    /*
    * 204658 - Roikinerk
    * 798332 - Haorunerk
    * 700522 - Haorunerks Bag
    * 279006 - Garkbinerk
    * 798322 - Kuruminerk
    */


    public _3200PriceOfGoodwill() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204658).addOnQuestStart(questId);    //Roikinerk
        qe.setQuestItemIds(182209082).add(questId);//Teleport Scroll
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }


    @Override
    public boolean onDialogEvent(final QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204658)//Roikinerk
            {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 4762);
                else return defaultQuestStartDialog(env);

            }
            return false;
        }


        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 798322)//Kuruminerk
            {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else if (env.getDialogId() == 1009)
                    return sendQuestDialog(env, 5);
                else return defaultQuestEndDialog(env);
            }
            return false;
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 204658)//Roikinerk
            {
                switch (env.getDialogId()) {
                    case 25:
                        return sendQuestDialog(env, 1003);
                    case 1011:
                        return sendQuestDialog(env, 1011);
                    case 10000:
                        // Create instance
                        WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300100000);
                        InstanceService.registerPlayerWithInstance(newInstance, player);
                        //teleport to cell in steel rake: 300100000 403.55 508.11 885.77 0
                        TeleportService.teleportTo(player, 300100000, newInstance.getInstanceId(), 403.55f, 508.11f, 885.77f, 0);
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        return true;
                }

            } else if (targetId == 798332 && var == 1)//Haorunerk
            {
                switch (env.getDialogId()) {
                    case 25:
                        return sendQuestDialog(env, 1352);
                    case 1353:
                        PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 431));
                        break;
                    case 10001:
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                }
            } else if (targetId == 700522 && var == 2)//Haorunerks Bag, loc: 401.24 503.19 885.76 119
            {
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        updateQuestStatus(env);
                    }
                }, 3000);
                return true;
            } else if (targetId == 279006 && var == 3)//Garkbinerk
            {
                switch (env.getDialogId()) {
                    case 25:
                        return sendQuestDialog(env, 2034);
                    case 10255:
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return true;

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

        if (id != 182209082 || qs == null || qs.getQuestVarById(0) != 2)
            return false;

        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                player.getInventory().removeFromBagByItemId(182209082, 1);
                //teleport location(BlackCloudIsland): 400010000 3419.16 2445.43 2766.54 57
                TeleportService.teleportTo(player, 400010000, 3419.16f, 2445.43f, 2766.54f, 57);
                qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                updateQuestStatus(env);
            }
        }, 3000);
        return true;
    }
}
