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

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.world.zone.ZoneName;

/**
 * @author Akiro
 */
public class _2200AltgardDuties extends QuestHandler {

    private final static int questId = 2200;

    public _2200AltgardDuties() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203557).addOnTalkEvent(questId);
        qe.setQuestEnterZone(ZoneName.ALTGARD_FORTRESS_220030000).add(questId);
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
        if (targetId != 203557)
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
                int[] ids = {2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022};
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
        if (zoneName != ZoneName.ALTGARD_FORTRESS_220030000)
            return false;
        if (qs != null)
            return false;
        QuestService.startQuest(env, QuestStatus.START);
        return true;
    }
}
