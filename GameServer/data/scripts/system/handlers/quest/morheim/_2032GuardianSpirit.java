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
package quest.morheim;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
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
 * @author Erin
 */
public class _2032GuardianSpirit extends QuestHandler {
    private final static int questId = 2032;
    private int itemId = 182204005;

    public _2032GuardianSpirit() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setQuestItemIds(itemId).add(questId);
        qe.setNpcQuestData(204302).addOnTalkEvent(questId); //Bragi
        qe.setNpcQuestData(204329).addOnTalkEvent(questId); //Tofa
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);

        if (qs == null)
            return false;

        final int var = qs.getQuestVarById(0);
        int targetId = 0;

        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 204302:
                    if (var == 0) {
                        switch (env.getDialogId()) {
                            case 25:
                                return sendQuestDialog(env, 1011);
                            case 10000:
                            case 10001:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            default:
                                return defaultQuestStartDialog(env);
                        }
                    }
                    break;
                case 204329:
                    switch (env.getDialogId()) {
                        case 25:
                            switch (var) {
                                case 1:
                                    return sendQuestDialog(env, 1352);
                                case 2:
                                    return sendQuestDialog(env, 1693);
                            }
                        case 1353:
                            if (var == 1)
                                PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 73));
                            break;
                        case 10000:
                        case 10001:
                        case 10003:
                            switch (var) {
                                case 1:
                                    qs.setQuestVarById(0, var + 1);
                                    updateQuestStatus(env);
                                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                    return true;
                                case 2:
                                    qs.setQuestVarById(0, var + 1);
                                    updateQuestStatus(env);
                                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                    return true;
                                case 3:
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(itemId, 1))))
                                        return true;

                                    qs.setQuestVarById(0, var + 1);
                                    updateQuestStatus(env);
                                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                    return true;
                            }
                        case 33:
                            if (var == 2) {
                                if (QuestService.collectItemCheck(env, true)) {
                                    qs.setQuestVarById(0, var + 1);
                                    updateQuestStatus(env);
                                    return sendQuestDialog(env, 2034);
                                } else
                                    return sendQuestDialog(env, 10001);
                            }
                    }
                    break;
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204329)
                return defaultQuestEndDialog(env);
        }
        return false;
    }

    @Override
    public boolean onItemUseEvent(final QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (id != itemId)
            return false;

        if (!ZoneService.getInstance().isInsideZone(player, ZoneName.EXECUTION_GROUND_OF_DELTRAS_220020000))
            return false;

        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 88));
                player.getInventory().removeFromBagByItemId(itemId, 1);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
            }
        }, 3000);

        return true;
    }
}
