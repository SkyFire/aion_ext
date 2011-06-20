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

/**
 * @author Atomics
 */
public class _1346KillingforCastor extends QuestHandler {

    private final static int questId = 1346;

    public _1346KillingforCastor() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203966).addOnQuestStart(questId);
        qe.setNpcQuestData(210844).addOnKillEvent(questId);
        qe.setNpcQuestData(210878).addOnKillEvent(questId);
        qe.setNpcQuestData(203966).addOnTalkEvent(questId);
        qe.setNpcQuestData(203965).addOnTalkEvent(questId);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 210844, 0, 15, 0) || defaultQuestOnKillEvent(env, 210878, 0, 20, 1))
            return true;
        else
            return false;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (player.getCommonData().getLevel() < 27)
            return false;
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203966) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 203965) {
                if (env.getDialogId() == 25 && qs.getQuestVarById(1) == 20 && qs.getQuestVarById(0) == 15) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    return sendQuestDialog(env, 1352);
                } else
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203965) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 5);
                else if (env.getDialogId() == 1009)
                    return sendQuestDialog(env, 5);
                else
                    return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

}
