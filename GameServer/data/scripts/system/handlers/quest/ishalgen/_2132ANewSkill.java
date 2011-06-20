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
package quest.ishalgen;

import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;

/**
 * @author MrPoke
 */
public class _2132ANewSkill extends QuestHandler {
    private final static int questId = 2132;

    public _2132ANewSkill() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(203527).addOnTalkEvent(questId); // Warrior
        qe.setNpcQuestData(203528).addOnTalkEvent(questId); // Scout
        qe.setNpcQuestData(203529).addOnTalkEvent(questId); // Mage
        qe.setNpcQuestData(203530).addOnTalkEvent(questId); // Priest
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
            switch (player.getCommonData().getPlayerClass()) {
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
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.REWARD)
            return false;

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        PlayerClass playerClass = player.getCommonData().getPlayerClass();
        switch (targetId) {
            case 203527:
                if (playerClass == PlayerClass.WARRIOR) {
                    if (env.getDialogId() == -1)
                        return sendQuestDialog(env, 1011);
                    else if (env.getDialogId() == 1009)
                        return sendQuestDialog(env, 5);
                    else
                        return this.defaultQuestEndDialog(env);
                }
                return false;
            case 203528:
                if (playerClass == PlayerClass.SCOUT) {
                    if (env.getDialogId() == -1)
                        return sendQuestDialog(env, 1352);
                    else if (env.getDialogId() == 1009)
                        return sendQuestDialog(env, 6);
                    else
                        return this.defaultQuestEndDialog(env);
                }
                return false;
            case 203529:
                if (playerClass == PlayerClass.MAGE) {
                    if (env.getDialogId() == -1)
                        return sendQuestDialog(env, 1693);
                    else if (env.getDialogId() == 1009)
                        return sendQuestDialog(env, 7);
                    else
                        return this.defaultQuestEndDialog(env);
                }
                return false;
            case 203530:
                if (playerClass == PlayerClass.PRIEST) {
                    if (env.getDialogId() == -1)
                        return sendQuestDialog(env, 2034);
                    else if (env.getDialogId() == 1009)
                        return sendQuestDialog(env, 8);
                    else
                        return this.defaultQuestEndDialog(env);
                }
                return false;
        }
        return false;
    }
}
