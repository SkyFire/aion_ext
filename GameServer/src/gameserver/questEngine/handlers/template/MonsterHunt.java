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
package gameserver.questEngine.handlers.template;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.handlers.models.MonsterInfo;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import javolution.util.FastMap;

/**
 * @author MrPoke
 */
public class MonsterHunt extends QuestHandler {
    private final int questId;
    private final int startNpc;
    private final int endNpc;
    private final FastMap<Integer, MonsterInfo> monsterInfo;

    /**
     * @param questId
     */
    public MonsterHunt(int questId, int startNpc, int endNpc, FastMap<Integer, MonsterInfo> monsterInfo) {
        super(questId);
        this.questId = questId;
        this.startNpc = startNpc;
        if (endNpc != 0)
            this.endNpc = endNpc;
        else
            this.endNpc = startNpc;
        this.monsterInfo = monsterInfo;
    }

    @Override
    public void register() {
        qe.setNpcQuestData(startNpc).addOnQuestStart(questId);
        qe.setNpcQuestData(startNpc).addOnTalkEvent(questId);
        for (int monsterId : monsterInfo.keySet())
            qe.setNpcQuestData(monsterId).addOnKillEvent(questId);
        if (endNpc != startNpc)
            qe.setNpcQuestData(endNpc).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();

        QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
        if (defaultQuestNoneDialog(env, template, startNpc))
            return true;

        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START) {
            for (MonsterInfo mi : monsterInfo.values()) {
                if (mi.getMaxKill() < qs.getQuestVarById(mi.getVarId()))
                    return false;
            }
            if (env.getTargetId() == endNpc) {
                switch (env.getDialogId()) {
                    case 25:
                        return sendQuestDialog(env, 1352);
                    case 1009:
                        return defaultCloseDialog(env, var, var + 1, true, true);
                }
            }
        }
        return defaultQuestRewardDialog(env, endNpc, 0);
    }


    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (!super.defaultQuestOnDialogInitStart(env))
            return false;

        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        if (qs.getStatus() != QuestStatus.START)
            return false;
        MonsterInfo mi = monsterInfo.get(env.getTargetId());
        if (mi == null)
            return false;
        return defaultQuestOnKillEvent(env, env.getTargetId(), 0, mi.getMaxKill(), mi.getVarId());
    }
}
