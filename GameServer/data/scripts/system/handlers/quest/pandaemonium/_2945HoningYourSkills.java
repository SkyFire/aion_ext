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
package quest.pandaemonium;

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
 * @author Hellboy aion4Free
 */
public class _2945HoningYourSkills extends QuestHandler {
    private final static int questId = 2945;

    public _2945HoningYourSkills() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(204075).addOnTalkEvent(questId);
        qe.setNpcQuestData(204088).addOnTalkEvent(questId);
    }
    
    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null)
            return false;
        boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
        if (!lvlCheck)
            return false;
        QuestService.startQuest(env, QuestStatus.START);
        return true;
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
                case 204075: {
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
                case 204088: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 1)
                                return sendQuestDialog(env, 1352);
                        case 10255:
                            if (var == 1) {
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
            if (targetId == 204075) {
                if (env.getDialogId() == -1){
                    int[] ids = {2946, 2947, 2042};
                    for (int id : ids) {
                        QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), id, env.getDialogId()), QuestStatus.LOCKED);
                        }
                    return sendQuestDialog(env, 10002);
                }
                else {
                    return defaultQuestEndDialog(env);
                }
            }
        }
        return false;
    }

}
