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

package quest.theobomos;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

public class _3049BloodMarkstheSpot extends QuestHandler {
    private final static int questId = 3049;

    public _3049BloodMarkstheSpot() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(798211).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        if (targetId == 0) {
            if (env.getDialogId() == 1002) {
                QuestService.startQuest(env, QuestStatus.START);
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
                return true;
            }
        } else if (targetId == 798211) {
            if (qs != null) {
                if (env.getDialogId() == 25 && qs.getStatus() == QuestStatus.START) {
                    return sendQuestDialog(env, 2375);
                } else if (env.getDialogId() == 1009) {
                    player.getInventory().removeFromBagByItemId(182208034, 1);
                    qs.setQuestVar(1);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    return defaultQuestEndDialog(env);
                } else
                    return defaultQuestEndDialog(env);
            }
        }
        return false;
    }
}
