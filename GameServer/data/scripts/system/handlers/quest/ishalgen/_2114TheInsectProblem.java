/*
 * This file is part of Aion X EMU <aionxemu.com>.
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.ishalgen;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

/**
 * @author Mr. Poke
 */
public class _2114TheInsectProblem extends QuestHandler {
    private final static int questId = 2114;

    public _2114TheInsectProblem() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203533).addOnQuestStart(questId);
        qe.setNpcQuestData(203533).addOnTalkEvent(questId);
        qe.setNpcQuestData(210734).addOnKillEvent(questId);
        qe.setNpcQuestData(210380).addOnKillEvent(questId);
        qe.setNpcQuestData(210381).addOnKillEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 203533) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                switch (env.getDialogId()) {
                    case 25:
                        return sendQuestDialog(env, 1011);
                    case 10000:
                        if (QuestService.startQuest(env, QuestStatus.START)) {
                            qs = player.getQuestStateList().getQuestState(questId);
                            qs.setQuestVar(1);
                            this.updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                    case 10001:
                        if (QuestService.startQuest(env, QuestStatus.START)) {
                            qs = player.getQuestStateList().getQuestState(questId);
                            qs.setQuestVar(11);
                            this.updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                }
            } else if (qs.getStatus() == QuestStatus.REWARD) {
                int var = qs.getQuestVarById(0);
                switch (env.getDialogId()) {
                    case -1:
                        if (var == 10)
                            return sendQuestDialog(env, 5);
                        else if (var == 20)
                            return sendQuestDialog(env, 6);
                    case 17:
                        if (QuestService.questFinish(env, var / 10 - 1)) {
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                }
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        int[] mobs = {210380, 210381};
        if (defaultQuestOnKillEvent(env, 210734, 1, 10) || defaultQuestOnKillEvent(env, 210734, 10, true) || defaultQuestOnKillEvent(env, mobs, 11, 20) || defaultQuestOnKillEvent(env, mobs, 20, true))
            return true;
        else
            return false;
    }
}
