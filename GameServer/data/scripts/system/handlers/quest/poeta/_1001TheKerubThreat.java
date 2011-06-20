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
package quest.poeta;

import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

/**
 * @author HellBoy
 */
public class _1001TheKerubThreat extends QuestHandler {
    private final static int questId = 1001;

    public _1001TheKerubThreat() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(210670).addOnKillEvent(questId);
        int[] npcs = {203071, 203067};
        for (int npc : npcs)
            qe.setNpcQuestData(npc).addOnTalkEvent(questId);
        qe.addQuestLvlUp(questId);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        return defaultQuestOnKillEvent(env, 210670, 1, 6);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        if (!super.defaultQuestOnDialogInitStart(env))
            return false;

        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START) {
            if (env.getTargetId() == 203071) {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 0)
                            return sendQuestDialog(env, 1011);
                        else if (var == 6)
                            return sendQuestDialog(env, 1352);
                        else if (var == 7)
                            return sendQuestDialog(env, 1693);
                    case 1012:
                        return defaultQuestMovie(env, 15);
                    case 10000:
                        return defaultCloseDialog(env, 0, 1);
                    case 10001:
                        return defaultCloseDialog(env, 6, 7);
                    case 33:
                        return defaultQuestItemCheck(env, 7, 8, true, 1694, 2034);
                }
            }
        } else {
            if (env.getDialogId() == 10002)
                return defaultCloseDialog(env, 8, 0);
        }
        return defaultQuestRewardDialog(env, 203067, 0);
    }
}