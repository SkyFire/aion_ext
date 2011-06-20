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

package quest.altgard;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

/**
 * @author Mr. Poke
 */
public class _2015TaketheInitiative extends QuestHandler {

    private final static int questId = 2015;

    public _2015TaketheInitiative() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(203631).addOnTalkEvent(questId);
        qe.setNpcQuestData(210510).addOnKillEvent(questId);
        qe.setNpcQuestData(210504).addOnKillEvent(questId);
        qe.setNpcQuestData(210506).addOnKillEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        final int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 203631:
                    switch (env.getDialogId()) {
                        case -1:
                            if (qs.getQuestVarById(1) >= 1 && qs.getQuestVarById(2) >= 5 && qs.getQuestVarById(3) >= 5) {
                                qs.setQuestVarById(0, var + 1);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 1352);
                            }
                            break;
                        case 25:
                            if (var == 0)
                                return sendQuestDialog(env, 1011);
                            break;
                        case 10000:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203631) {
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 210510, 0, 1, 1) || defaultQuestOnKillEvent(env, 210504, 0, 5, 2) || defaultQuestOnKillEvent(env, 210506, 0, 5, 3))
            return true;
        else
            return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 2014);
    }
}
