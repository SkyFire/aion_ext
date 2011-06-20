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
package quest.heiron;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

/**
 * @author Atomics
 */
public class _1548KlawControl extends QuestHandler {

    private final static int questId = 1548;

    public _1548KlawControl() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204583).addOnQuestStart(questId); //Senea
        qe.setNpcQuestData(204583).addOnTalkEvent(questId); //Senea
        qe.setNpcQuestData(700209).addOnKillEvent(questId); //Klawspawn
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;

        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 204583) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else
                    return defaultQuestStartDialog(env);
            } else if (qs.getStatus() == QuestStatus.REWARD) {
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 700209, 0, 4))
            return true;
        if (defaultQuestOnKillEvent(env, 700209, 4, 5)) {
            Player player = env.getPlayer();
            QuestState qs = player.getQuestStateList().getQuestState(questId);
            qs.setStatus(QuestStatus.REWARD);
            updateQuestStatus(env);
            return true;
        } else
            return false;
    }
}
