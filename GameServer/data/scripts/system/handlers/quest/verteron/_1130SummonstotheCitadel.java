/**
 * This file is part of <Alpha Team Project>
 *
 * Alpha Team Project is Private Software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Private Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alpha Team Project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Alpha Team Project. If not, see <http://www.gnu.org/licenses/>.
 */

package quest.verteron;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.world.zone.ZoneName;

public class _1130SummonstotheCitadel extends QuestHandler {

    private final static int questId = 1130;

    public _1130SummonstotheCitadel() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203098).addOnTalkEvent(questId);
        qe.setQuestEnterZone(ZoneName.VERTERON_CITADEL).add(questId);
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
        if (targetId != 203098)
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
                int[] ids = {1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023};
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
        if (zoneName != ZoneName.VERTERON_CITADEL)
            return false;
        if (qs != null)
            return false;
        QuestService.startQuest(env, QuestStatus.START);
        return true;
    }
}
