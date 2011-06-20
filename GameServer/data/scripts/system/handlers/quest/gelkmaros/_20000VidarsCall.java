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

import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;

/**
 * @author Nephis
 */
public class _20000VidarsCall extends QuestHandler {
    private final static int questId = 20000;

    public _20000VidarsCall() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        int[] npcs = {204052, 798800};
        for (int npc : npcs)
            qe.setNpcQuestData(npc).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
        if (qs != null || !lvlCheck)
            return false;
        QuestService.startQuest(env, QuestStatus.START);
        return true;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        if (!super.defaultQuestOnDialogInitStart(env))
            return false;

        QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            if (env.getTargetId() == 204052) {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 0)
                            return sendQuestDialog(env, 1011);
                    case 10255:
                        return defaultCloseDialog(env, 0, 0, true, false);
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (env.getTargetId() == 798800) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else if (env.getDialogId() == 17)
                    QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), 20001, env.getDialogId()), QuestStatus.LOCKED);
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }
}
