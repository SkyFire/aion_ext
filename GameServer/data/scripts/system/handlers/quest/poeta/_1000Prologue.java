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

import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

/**
 * @author MrPoke
 */
public class _1000Prologue extends QuestHandler {
    private final static int questId = 1000;

    public _1000Prologue() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addOnEnterWorld(questId);
        qe.setQuestMovieEndIds(1).add(questId);
    }

    @Override
    public boolean onEnterWorldEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (player.getCommonData().getRace() != Race.ELYOS)
            return false;
        if (qs == null)
            QuestService.startQuest(env, QuestStatus.START);
        qs = player.getQuestStateList().getQuestState(questId);
        if (qs.getStatus() == QuestStatus.START) {
            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(1, 1));
            return true;
        }
        return false;
    }

    @Override
    public boolean onMovieEndEvent(QuestCookie env, int movieId) {
        if (movieId != 1)
            return false;
        Player player = env.getPlayer();
        if (player.getCommonData().getRace() != Race.ELYOS)
            return false;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START)
            return false;
        qs.setStatus(QuestStatus.REWARD);
        QuestService.questFinish(env);
        return true;
    }
}
