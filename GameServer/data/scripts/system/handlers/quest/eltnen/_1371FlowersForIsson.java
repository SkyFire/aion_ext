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
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

/**
 * @author Nephis and quest helper team
 */
public class _1371FlowersForIsson extends QuestHandler {

    private final static int questId = 1371;

    public _1371FlowersForIsson() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203949).addOnQuestStart(questId);
        qe.setNpcQuestData(203949).addOnTalkEvent(questId);
        qe.setNpcQuestData(730039).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        long itemCount = 0;
        if (targetId == 203949) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else
                    return defaultQuestStartDialog(env);
            } else if (qs.getStatus() == QuestStatus.START) {
                int var = qs.getQuestVarById(0);
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 0) {
                            return sendQuestDialog(env, 1352);
                        }

                    case 33:
                        if (var == 0)
                            itemCount = player.getInventory().getItemCountByItemId(152000601);
                        if (itemCount > 4) {
                            return sendQuestDialog(env, 1353);
                        } else {
                            return sendQuestDialog(env, 1438);
                        }
                    case 10000:
                        player.getInventory().removeFromBagByItemId(152000601, 5);
                        qs.setQuestVar(2);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 0);

                }
                return false;
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                return defaultQuestEndDialog(env);
            }
        } else if (targetId == 730039 && qs != null) {
            int var = qs.getQuestVarById(0);
            if (qs.getStatus() == QuestStatus.START && var == 2) {

                final int targetObjectId = env.getVisibleObject().getObjectId();
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);

                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                        targetObjectId), true);
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId)
                            return;
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId,
                                3000, 0));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                                targetObjectId), true);

                    }
                }, 3000);
            }
        }
        return false;
    }
}
