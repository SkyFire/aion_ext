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

package quest.sanctum;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

public class _1096APastMission extends QuestHandler {
    private final static int questId = 1096;
    private final static int[] npc_ids = {203701, 203852, 790001};

    public _1096APastMission() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 203701: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 0)
                                return sendQuestDialog(env, 1011);
                        case 10000:
                            if (var == 0) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                    }
                }
                break;
                case 203852: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 1)
                                return sendQuestDialog(env, 1352);
                        case 10001:
                            if (var == 1) {
                                qs.setQuestVarById(0, var + 1);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                    }
                }
                break;
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 790001) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 1693);
                else
                    return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }
}
