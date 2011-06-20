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
package quest.eltnen;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;

/**
 * @author Atomics
 * @author Nephis
 */
public class _1487BalaurBones extends QuestHandler {
    private final static int questId = 1487;

    public _1487BalaurBones() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(798126).addOnQuestStart(questId);
        qe.setNpcQuestData(798126).addOnTalkEvent(questId);
        qe.setNpcQuestData(700313).addOnTalkEvent(questId);
        qe.setNpcQuestData(700314).addOnTalkEvent(questId);
        qe.setNpcQuestData(700315).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 798126) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else
                    return defaultQuestStartDialog(env);
            }

            if (qs != null && qs.getStatus() == QuestStatus.START) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 2375);
                else if (env.getDialogId() == 33) {
                    if (QuestService.collectItemCheck(env, true)) {
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 5);
                    } else
                        return sendQuestDialog(env, 2716);
                } else
                    return defaultQuestEndDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD)
                return defaultQuestEndDialog(env);
        } else if (qs != null && qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 700313:
                case 700314:
                case 700315: {
                    if (qs.getQuestVarById(0) == 0 && env.getDialogId() == -1)
                        return true;
                }
            }
        }
        return false;
    }
}
