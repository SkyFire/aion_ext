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

import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;

/**
 * @author MrPoke
 */
public class _1205ANewSkill extends QuestHandler {
    private final static int questId = 1205;

    public _1205ANewSkill() {
        super(questId);
    }

    @Override
    public void register() {
        int[] npcs = {203087, 203088, 203089, 203090};
        qe.addQuestLvlUp(questId);
        for (int npc : npcs)
            qe.setNpcQuestData(npc).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
        if (!lvlCheck)
            return false;
        if (qs != null)
            return false;
        if (QuestService.startQuest(env, QuestStatus.START)) {
            qs = player.getQuestStateList().getQuestState(questId);
            qs.setStatus(QuestStatus.REWARD);
            switch (PlayerClass.getStartingClassFor(player.getCommonData().getPlayerClass())) {
                case WARRIOR:
                    qs.setQuestVar(1);
                    break;
                case SCOUT:
                    qs.setQuestVar(2);
                    break;
                case MAGE:
                    qs.setQuestVar(3);
                    break;
                case PRIEST:
                    qs.setQuestVar(4);
                    break;
            }
            updateQuestStatus(env);
        }
        return true;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.REWARD)
            return false;
        switch (PlayerClass.getStartingClassFor(player.getCommonData().getPlayerClass())) {
            case WARRIOR:
                return defaultQuestRewardDialog(env, 203087, 1011, 0);
            case SCOUT:
                return defaultQuestRewardDialog(env, 203088, 1352, 1);
            case MAGE:
                return defaultQuestRewardDialog(env, 203089, 1693, 2);
            case PRIEST:
                return defaultQuestRewardDialog(env, 203090, 2034, 3);
        }
        return false;
    }
}
