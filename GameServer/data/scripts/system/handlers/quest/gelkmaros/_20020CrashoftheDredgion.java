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
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ZoneService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;

/**
 * @author Nephis
 */
public class _20020CrashoftheDredgion extends QuestHandler {
    private final static int questId = 20020;
    private final static int[] npc_ids = {799225, 799226, 799239, 798703};

    public _20020CrashoftheDredgion() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
        qe.setQuestItemIds(182207600).add(questId);
        qe.setQuestEnterZone(ZoneName.COWARDS_COVE_220070000).add(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 20026);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        if (!super.defaultQuestOnDialogInitStart(env))
            return false;

        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
                case 799225:
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 0)
                                return sendQuestDialog(env, 1011);
                        case 10000:
                            return defaultCloseDialog(env, 0, 1);
                    }
                    break;
                case 799226:
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 1)
                                return sendQuestDialog(env, 1352);
                            else if (var == 2)
                                return sendQuestDialog(env, 1693);
                            else if (var == 5)
                                return sendQuestDialog(env, 2716);
                            else if (var == 7)
                                return sendQuestDialog(env, 3398);
                        case 33:
                            return defaultQuestItemCheck(env, 2, 3, false, 10000, 10001, 182207602, 1);
                        case 10001:
                            return defaultCloseDialog(env, 1, 2);
                        case 10005:
                            return defaultCloseDialog(env, 5, 6);
                        case 10007:
                            return defaultCloseDialog(env, 7, 8);
                    }
                    break;
                case 799239:
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 3)
                                return sendQuestDialog(env, 2034);
                        case 10003:
                            return defaultCloseDialog(env, 3, 4, 0, 0, 182207602, 1);
                    }
                    break;
                case 798703:
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 4)
                                return sendQuestDialog(env, 2375);
                        case 10004:
                            return defaultCloseDialog(env, 4, 5, 182207600, 1, 0, 0);
                    }
                    break;
            }
        }
        return defaultQuestRewardDialog(env, 799226, 10002);
    }

    @Override
    public boolean onItemUseEvent(final QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (id != 182207600)
            return false;
        if (qs == null || qs.getQuestVarById(0) != 6)
            return false;
        if (!ZoneService.getInstance().isInsideZone(player, ZoneName.DREDGION_CRASH_SITE_220070000))
            return false;
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 1000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                defaultQuestRemoveItem(env, 182207600, 1);
                qs.setQuestVar(7);
                updateQuestStatus(env);
            }
        }, 1000);
        return true;
    }

    public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs.getStatus() == QuestStatus.COMPLETE)
            return false;
        if (zoneName != ZoneName.COWARDS_COVE_220070000)
            return false;
        if (qs == null)
            return false;
        if (qs.getQuestVarById(0) == 8) {
            qs.setStatus(QuestStatus.REWARD);
            updateQuestStatus(env);
            return true;
        }
        return false;
    }
}
