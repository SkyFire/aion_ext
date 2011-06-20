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

import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

/**
 * @author Nephis
 */
public class _20026WaytoInggison extends QuestHandler {
    private final static int questId = 20026;

    public _20026WaytoInggison() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        int[] npcs = {799364, 799365};
        for (int npc : npcs)
            qe.setNpcQuestData(npc).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 20001);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        if (!super.defaultQuestOnDialogInitStart(env))
            return false;

        QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
                case 799364:
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 0)
                                return sendQuestDialog(env, 1011);
                        case 10000:
                            return defaultCloseDialog(env, 0, 1);
                    }
                case 799365:
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 1)
                                return sendQuestDialog(env, 1352);
                        case 10255:
                            if (defaultCloseDialog(env, 1, 0, true, false))
                                return sendQuestDialog(env, 5);
                    }
            }
        }
        return defaultQuestRewardDialog(env, 799365, 10002);
    }
}
