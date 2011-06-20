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

package quest.altgard;

import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;

/**
 * @author HellBoy
 */
public class _2230AFriendlyWager extends QuestHandler {

    private final static int questId = 2230;

    public _2230AFriendlyWager() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203621).addOnQuestStart(questId);
        qe.setNpcQuestData(203621).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (env.getTargetId() == 203621) {
                switch (env.getDialogId()) {
                    case 25:
                        return sendQuestDialog(env, 1011);
                    case 1007:
                        return sendQuestDialog(env, 4);
                    case 1002:
                        return sendQuestDialog(env, 1003);
                    case 1003:
                        return sendQuestDialog(env, 1004);
                    case 10000:
                        if (QuestService.startQuest(env, QuestStatus.START)) {
                            QuestService.questTimerStart(env, 1800);
                            return defaultCloseDialog(env, 0, 0);
                        } else
                            return false;
                }
            }
        }
        if (qs == null)
            return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            if (env.getTargetId() == 203621) {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 0)
                            return sendQuestDialog(env, 2375);
                    case 33:
                        if (var == 0) {
                            if (QuestService.collectItemCheck(env, true)) {
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                QuestService.questTimerEnd(env);
                                return sendQuestDialog(env, 5);
                            } else
                                return sendQuestDialog(env, 2716);
                        }
                }
            }
        }
        return defaultQuestRewardDialog(env, 203621, 0);
    }
}
