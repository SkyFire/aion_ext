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

public class _1131UndeliveredArmor extends QuestHandler {
    private final static int questId = 1131;

    public _1131UndeliveredArmor() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203097).addOnQuestStart(questId);
        qe.setNpcQuestData(203097).addOnTalkEvent(questId);
        qe.setNpcQuestData(798001).addOnTalkEvent(questId);
        qe.setNpcQuestData(203101).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 203097) {
            if (qs == null) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else if (env.getDialogId() == 1007)
                    return sendQuestDialog(env, 4);
                else
                    return defaultQuestStartItem(env, 182200506, 1, 0, 0);
            }
        } else if (targetId == 798001) {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 10000)
                    return defaultCloseDialog(env, 0, 1, 182200507, 1, 182200506, 1);
                else
                    return defaultQuestStartDialog(env);
            }
        } else if (targetId == 203101) {
            if (qs != null) {
                if (env.getDialogId() == 25 && qs.getStatus() == QuestStatus.START)
                    return sendQuestDialog(env, 2375);
                else if (env.getDialogId() == 1009)
                    return defaultCloseDialog(env, 1, 2, true, true, 0, 0, 0, 182200507, 1);
                else
                    return defaultQuestEndDialog(env);
            }
        }
        return false;
    }
}
