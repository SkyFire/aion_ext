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

package quest.beluslan;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

/**
 *
 *
 */
public class _2641PowwowWiththeMau extends QuestHandler {
    private final static int questId = 2641;

    public _2641PowwowWiththeMau() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204817).addOnQuestStart(questId);
        qe.setNpcQuestData(204871).addOnTalkEvent(questId);
        qe.setNpcQuestData(204795).addOnTalkEvent(questId);
        qe.setNpcQuestData(204798).addOnTalkEvent(questId);
        qe.setNpcQuestData(204796).addOnTalkEvent(questId);
        qe.setNpcQuestData(204700).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 204817) {
            if (qs == null) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else
                    return defaultQuestStartDialog(env);
            }
        } else if (targetId == 204795) {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 10000) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        } else if (targetId == 204798) {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 10000) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        } else if (targetId == 204796) {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 10000) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        } else if (targetId == 204700) {
            if (qs != null) {
                if (env.getDialogId() == 25 && qs.getStatus() == QuestStatus.START)
                    return sendQuestDialog(env, 2375);
                else if (env.getDialogId() == 1009) {
                    qs.setQuestVar(3);
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
