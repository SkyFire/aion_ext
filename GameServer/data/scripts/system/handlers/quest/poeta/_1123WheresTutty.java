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
import gameserver.world.zone.ZoneName;

/**
 * @author MrPoke
 */
public class _1123WheresTutty extends QuestHandler {
    private final static int questId = 1123;

    public _1123WheresTutty() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(790001).addOnTalkEvent(questId);
        qe.setNpcQuestData(790001).addOnQuestStart(questId);
        qe.setQuestEnterZone(ZoneName.Q1123).add(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        if (defaultQuestNoneDialog(env, 790001))
            return true;
        if (qs == null)
            return false;
        return defaultQuestRewardDialog(env, 790001, 1352);
    }

    @Override
    public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (zoneName != ZoneName.Q1123)
            return false;
        if (qs == null || qs.getStatus() != QuestStatus.START)
            return false;
        defaultQuestMovie(env, 11);
        qs.setStatus(QuestStatus.REWARD);
        updateQuestStatus(env);
        return true;
    }
}
