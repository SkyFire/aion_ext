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
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

public class _1011DangerFromAbove extends QuestHandler {

    private final static int questId = 1011;

    public _1011DangerFromAbove() {
        super(questId);
    }

    @Override
    public void register() {
        int[] talkNpcs = {203109, 203122, 203109};
        qe.setNpcQuestData(700091).addOnKillEvent(questId);
        qe.addQuestLvlUp(questId);
        for (int id : talkNpcs)
            qe.setNpcQuestData(id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 700091, 1, 4) || defaultQuestOnKillEvent(env, 700091, 4, true))
            return true;
        else
            return false;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 203109) {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 0)
                            return sendQuestDialog(env, 1011);
                    case 10000:
                        if (var == 0) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
                                    .getObjectId(), 10));
                            return true;
                        }
                }
            } else if (targetId == 203122) {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 1)
                            return sendQuestDialog(env, 1352);
                    case 1353:
                        defaultQuestMovie(env, 24);
                        return sendQuestDialog(env, 1353);
                    case 10001:
                        if (var == 1) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
                                    .getObjectId(), 10));
                            return true;
                        }
                }
            }

        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203109) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 1693);
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 1130);
    }
}
