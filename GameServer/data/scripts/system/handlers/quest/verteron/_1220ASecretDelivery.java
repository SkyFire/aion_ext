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
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

public class _1220ASecretDelivery extends QuestHandler {
    private final static int questId = 1220;

    public _1220ASecretDelivery() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203172).addOnQuestStart(questId);
        qe.setNpcQuestData(203172).addOnTalkEvent(questId);
        qe.setNpcQuestData(798004).addOnTalkEvent(questId);
        qe.setNpcQuestData(798046).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203172) {
                if (env.getDialogId() == 25) {
                    return sendQuestDialog(env, 1011);
                } else
                    return defaultQuestStartDialog(env);
            }
        }

        if (qs == null)
            return false;

        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 798004: {
                    switch (env.getDialogId()) {
                        case 25:
                            return sendQuestDialog(env, 1352);
                        case 10000:
                            return defaultCloseDialog(env, 0, 1);
                    }
                }
                case 798046: {
                    switch (env.getDialogId()) {
                        case 25: {
                            return sendQuestDialog(env, 2375);
                        }
                        case 1009: {
                            qs.setQuestVar(2);
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                            return defaultQuestEndDialog(env);
                        }
                        default:
                            return defaultQuestEndDialog(env);
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 798046) {
                switch (env.getDialogId()) {
                    case 1009: {
                        return sendQuestDialog(env, 5);
                    }
                    default:
                        return defaultQuestEndDialog(env);
                }
            }
        }
        return false;
    }
}