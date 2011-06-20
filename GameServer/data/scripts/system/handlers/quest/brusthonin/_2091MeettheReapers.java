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

package quest.brusthonin;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;

/**
 *
 *
 */
public class _2091MeettheReapers extends QuestHandler {

    private final static int questId = 2091;

    public _2091MeettheReapers() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(205150).addOnTalkEvent(questId);
        qe.addOnEnterWorld(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        if (targetId != 205150)
            return false;
        if (qs.getStatus() == QuestStatus.START) {
            if (env.getDialogId() == 25)
                return sendQuestDialog(env, 10002);
            else if (env.getDialogId() == 1009) {
                qs.setStatus(QuestStatus.REWARD);
                qs.setQuestVarById(0, 1);
                updateQuestStatus(env);
                return sendQuestDialog(env, 5);
            }
            return false;
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (env.getDialogId() == 17) {
                int[] ids = {2092, 2093, 2094};
                for (int id : ids) {
                    QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), id, env.getDialogId()), QuestStatus.LOCKED);
                }
            }
            return defaultQuestEndDialog(env);
        }
        return false;
    }

    @Override
    public boolean onEnterWorldEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (player.getWorldId() != 220050000)
            return false;
        if (qs != null)
            return false;
        QuestService.startQuest(env, QuestStatus.START);
        return true;
    }
}
