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
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ZoneService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;

/**
 * @author Mcrizza
 */
public class _2031PetrifyingElim extends QuestHandler {
    private final static int questId = 2031;

    public _2031PetrifyingElim() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setQuestItemIds(182204001).add(questId);
        qe.setNpcQuestData(204304).addOnTalkEvent(questId); //Vili
        qe.setNpcQuestData(730038).addOnTalkEvent(questId); //Nabaru
        qe.addQuestLvlUp(questId);
    }

    @Override
    public boolean onItemUseEvent(final QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (id != 182204001)
            return false;

        if (!ZoneService.getInstance().isInsideZone(player, ZoneName.HILL_OF_BELEMU_220020000))
            return false;

        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 72));
                player.getInventory().removeFromBagByItemId(182204001, 1);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
            }
        }, 3000);

        return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;

        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        QuestState qs = player.getQuestStateList().getQuestState(questId);

        if (qs == null)
            return false;

        if (targetId == 204304) //Vili
        {
            if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else if (env.getDialogId() == 10000) {
                    qs.setQuestVar(1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        } else if (targetId == 730038) //Nabaru
        {
            if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 1353)
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 71));
                else if (env.getDialogId() == 10001) {
                    qs.setQuestVar(2);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }

            if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1693);
                else if (env.getDialogId() == 33) {
                    if (player.getInventory().getItemCountByItemId(182204001) > 0) {
                        qs.setQuestVar(3);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    } else
                        return sendQuestDialog(env, 10001);
                } else
                    return defaultQuestStartDialog(env);
            }

            if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 3) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 2034);
                else if (env.getDialogId() == 10003) {
                    qs.setQuestVar(4);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            } else if (qs.getStatus() == QuestStatus.REWARD)
                return defaultQuestEndDialog(env);
        }

        return false;

    }
}
