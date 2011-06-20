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

package quest.verteron;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

public class _1013HuntingLepharistRevolutionaries extends QuestHandler {

    private final static int questId = 1013;
    private final static int[] mob_ids = {210688, 210316};

    public _1013HuntingLepharistRevolutionaries() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203126).addOnTalkEvent(questId);
        qe.addQuestLvlUp(questId);
        for (int mob_id : mob_ids)
            qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 203126) {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 0)
                            return sendQuestDialog(env, 1011);
                        else if (var == 11)
                            return sendQuestDialog(env, 1352);
                        else if (var >= 12) {
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                            return sendQuestDialog(env, 2375);
                        }
                    case 1012:
                        PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 25));
                        return sendQuestDialog(env, 1012);
                    case 10000:
                    case 10001:
                        if (var == 0 || var == 11) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
                                    .getObjectId(), 10));
                            return true;
                        }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203126)
                return defaultQuestEndDialog(env);
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 210688, 1, 12) || defaultQuestOnKillEvent(env, 210316, 12, true))
            return true;
        else
            return false;
    }
}