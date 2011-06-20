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

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;

public class _1149MissingPoppy extends QuestHandler {
    private final static int questId = 1149;

    public _1149MissingPoppy() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203191).addOnQuestStart(questId);
        qe.setNpcQuestData(203145).addOnQuestStart(questId);
        qe.setNpcQuestData(203145).addOnTalkEvent(questId);
        qe.setNpcQuestData(203191).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203145) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else
                    return defaultQuestStartDialog(env);
            }
        }

        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203145) {
                if (env.getDialogId() == 1009)
                    return sendQuestDialog(env, 5);
                else
                    return defaultQuestEndDialog(env);
            }
            return false;
        } else if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 203191:
                    switch (env.getDialogId()) {
                        case -1:
                            if (var == 1) {
                                Npc npc = (Npc) env.getVisibleObject();
                                if (MathUtil.getDistance(1255, 2223, 144, npc.getX(), npc.getY(), npc.getZ()) > 5) {
                                    if (!npc.getMoveController().isScheduled())
                                        npc.getMoveController().schedule();
                                    npc.getMoveController().setFollowTarget(true);
                                    return true;
                                } else
                                    qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 12));
                                npc.getController().onDie(null);
                                npc.getController().onDespawn(false);
                                return true;
                            }
                        case 25:
                            if (var == 0)
                                return sendQuestDialog(env, 1352);
                        case 10000:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            Npc npc = (Npc) env.getVisibleObject();
                            npc.getMoveController().setDistance(4);
                            npc.getMoveController().setFollowTarget(true);
                            npc.getMoveController().schedule();
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
                                    .getObjectId(), 0));
                            return true;
                    }
            }
        }
        return false;
    }
}