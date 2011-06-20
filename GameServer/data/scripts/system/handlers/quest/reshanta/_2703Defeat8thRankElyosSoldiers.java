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

package quest.reshanta;

import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;

public class _2703Defeat8thRankElyosSoldiers extends QuestHandler {
    private final static int questId = 2703;

    public _2703Defeat8thRankElyosSoldiers() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(278016).addOnQuestStart(questId);
        qe.setNpcQuestData(278016).addOnTalkEvent(questId);
        qe.addOnKillPlayer(questId);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillPlayerEvent(env, 2, 0, 9, false) || defaultQuestOnKillPlayerEvent(env, 2, 9, 10, true))
            return true;
        else
            return false;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
        if (defaultQuestNoneDialog(env, 278016))
            return true;
        if (qs == null)
            return false;
        return defaultQuestRewardDialog(env, 278016, 1352);
    }
}
