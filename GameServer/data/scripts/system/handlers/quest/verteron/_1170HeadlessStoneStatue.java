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

package quest.verteron;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.Collections;

public class _1170HeadlessStoneStatue extends QuestHandler {
    private final static int questId = 1170;

    public _1170HeadlessStoneStatue() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(730000).addOnQuestStart(questId); // Headless Statue Body
        qe.setNpcQuestData(730000).addOnActionItemEvent(questId);
        qe.setNpcQuestData(730000).addOnTalkEvent(questId);
        qe.setNpcQuestData(700033).addOnTalkEvent(questId); // Head of Stone Statue
        qe.setQuestMovieEndIds(16).add(questId);
    }

    @Override
    public boolean onActionItemEvent(QuestCookie env) {
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        return (targetId == 730000);
    }

    @Override
    public boolean onDialogEvent(final QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 730000) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 1011);
                else
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 700033 && env.getDialogId() == -1) {
                final int targetObjectId = env.getVisibleObject().getObjectId();
                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, targetObjectId), true);
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (!player.isTargeting(targetObjectId))
                            return;
                        if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182200504, 1)))) {
                            PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
                            PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0, targetObjectId), true);
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                        }
                    }
                }, 3000);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 730000) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 10000) {
                    if (player.getInventory().getItemCountByItemId(182200504) >= 1) {
                        player.getInventory().removeFromBagByItemId(182200504, 1);
                        PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 16));
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
                                .getObjectId(), 0));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onMovieEndEvent(QuestCookie env, int movieId) {
        if (movieId != 16)
            return false;
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.REWARD)
            return false;
        int rewardExp = player.getRates().getQuestXpRate() * 8410;
        player.getCommonData().addExp(rewardExp);
        qs.setStatus(QuestStatus.COMPLETE);
        qs.setCompliteCount(1);
        updateQuestStatus(env);
        PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(questId, QuestStatus.COMPLETE, 2));
        return true;
    }
}
