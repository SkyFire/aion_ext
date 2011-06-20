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
package quest.morheim;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.world.zone.ZoneName;

/**
 * @author MrPoke + Dune11
 */
public class _2300MorheimCommandersCall extends QuestHandler {

    private final static int questId = 2300;

    public _2300MorheimCommandersCall() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204301).addOnTalkEvent(questId);
        qe.setQuestEnterZone(ZoneName.MORHEIM_ICE_FORTRESS_220020000).add(questId);
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
        if (targetId != 204301)
            return false;
        if (qs.getStatus() == QuestStatus.START) {
            if (env.getDialogId() == 25) {
                qs.setQuestVar(1);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                return sendQuestDialog(env, 1011);
            } else
                return defaultQuestStartDialog(env);
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (env.getDialogId() == 17) {
                int[] ids = {2031, 2032, 2033, 2034, 2035, 2036, 2037, 2038, 2039, 2040, 2041, 2042};
                for (int id : ids) {
                    QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), id, env.getDialogId()), QuestStatus.LOCKED);
                }
            }
            return defaultQuestEndDialog(env);
        }
        return false;
    }

    @Override
    public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (zoneName != ZoneName.MORHEIM_ICE_FORTRESS_220020000)
            return false;
        if (qs != null)
            return false;
        QuestService.startQuest(env, QuestStatus.START);
        return true;
    }
}
