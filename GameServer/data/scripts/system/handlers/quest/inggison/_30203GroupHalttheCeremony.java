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
package quest.inggison;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

/**
 * @author Nephis
 */
public class _30203GroupHalttheCeremony extends QuestHandler {

    private final static int questId = 30203;
    private final static int[] mob_ids = {216255, 216257, 216259, 216261, 216263};

    public _30203GroupHalttheCeremony() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(798926).addOnQuestStart(questId);
        qe.setNpcQuestData(798926).addOnTalkEvent(questId);
        for (int mob_id : mob_ids)
            qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 216255, 0, 1, 0) || defaultQuestOnKillEvent(env, 216257, 0, 1, 1) || defaultQuestOnKillEvent(env, 216259, 0, 1, 2) || defaultQuestOnKillEvent(env, 216261, 0, 1, 3) || defaultQuestOnKillEvent(env, 216263, 0, true, 4))
            return true;
        else
            return false;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 798926) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 4762);
                else
                    return defaultQuestStartDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD)
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else if (env.getDialogId() == 1009)
                    return sendQuestDialog(env, 5);
                else
                    return defaultQuestEndDialog(env);
        }

        return false;
    }
}
