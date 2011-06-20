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
import gameserver.world.zone.ZoneName;

public class _1194ReducingTursinStrength extends QuestHandler {
    private final static int questId = 1194;
    private final static int[] mob_ids = {210185, 210186};

    public _1194ReducingTursinStrength() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203098).addOnQuestStart(questId);
        qe.setNpcQuestData(203098).addOnTalkEvent(questId);
        for (int mob_id : mob_ids)
            qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
        qe.setQuestEnterZone(ZoneName.TURSIN_GARRISON).add(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203098) {
                if (env.getDialogId() == 25) {
                    return sendQuestDialog(env, 1011);
                } else
                    return defaultQuestStartDialog(env);
            }
        }

        if (qs == null)
            return false;

        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203098) {
                switch (env.getDialogId()) {
                    case -1: {
                        return sendQuestDialog(env, 1352);
                    }
                    case 1009: {
                        return sendQuestDialog(env, 5);
                    }
                    default:
                        return defaultQuestEndDialog(env);
                }
            }
        }
        return false;
    }

    @Override
    public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (zoneName != ZoneName.TURSIN_GARRISON)
            return false;
        if (qs == null)
            return false;

        if (qs.getQuestVarById(0) == 0) {
            qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
            updateQuestStatus(env);
            return true;
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, mob_ids, 1, 10) || defaultQuestOnKillEvent(env, mob_ids, 10, true))
            return true;
        else
            return false;
    }
}