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
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;

import java.util.Collections;


/**
 *
 *
 */
public class _2061SuppressingtheBakarmaLegion extends QuestHandler {
    private final static int questId = 2061;
    private final static int[] npc_ids = {204702, 278001, 204807, 700295, 204052};

    public _2061SuppressingtheBakarmaLegion() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setQuestEnterZone(ZoneName.MALEK_MINE_220040000).add(questId);
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(700290).addOnKillEvent(questId);
        qe.setNpcQuestData(214026).addOnKillEvent(questId);
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        int[] quests = {2051, 2052, 2053, 2054, 2055, 2056, 2057, 2058, 2059, 2060};
        return defaultQuestOnLvlUpEvent(env, quests);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 700290, 5, 8) || defaultQuestOnKillEvent(env, 214026, 12, 13))
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
            if (targetId == 204052) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else return defaultQuestEndDialog(env);
            }
            return false;
        } else if (qs.getStatus() != QuestStatus.START) {
            return false;
        }
        if (targetId == 204702) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 0)
                        return sendQuestDialog(env, 1011);
                    else if (var == 2)
                        return sendQuestDialog(env, 1693);
                    else if (var == 13)
                        return sendQuestDialog(env, 2375);
                case 1694:
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 255));
                    break;
                case 10000:
                    if (var == 0) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                case 10002:
                    if (var == 2) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                case 10255:
                    if (var == 13) {
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        } else if (targetId == 278001) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 1)
                        return sendQuestDialog(env, 1352);
                case 10001:
                    if (var == 1) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        ItemService.addItems(player, Collections.singletonList(new QuestItems(182204320, 1)));
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        } else if (targetId == 204807) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 3)
                        return sendQuestDialog(env, 2034);
                case 10003:
                    if (var == 3) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        } else if (targetId == 700295 && var >= 4 && var < 12) {
            if (env.getDialogId() == -1) {
                if (var == 11) {
                    player.getInventory().removeFromBagByItemId(182204320, 1);
                }
                final int targetObjectId = env.getVisibleObject().getObjectId();
                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
                        qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                        updateQuestStatus(env);
                    }
                }, 3000);
            }
            return true;
        }
        return false;
    }
}
