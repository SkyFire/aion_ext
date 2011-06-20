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
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

/**
 * @author Mr. Poke
 */
public class _2005TeachingaLesson extends QuestHandler {
    private final static int questId = 2005;

    public _2005TeachingaLesson() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(203540).addOnTalkEvent(questId);
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
                case 203540: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 0)
                                return sendQuestDialog(env, 1011);
                            else if (var == 1)
                                return sendQuestDialog(env, 1352);
                            break;
                        case 1012:
                            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 54));
                            break;
                        case 10000:
                            if (var == 0) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                            break;
                        case 33:
                            if (var == 1) {
                                if (QuestService.collectItemCheck(env, true)) {
                                    qs.setStatus(QuestStatus.REWARD);
                                    updateQuestStatus(env);
                                    return sendQuestDialog(env, 5);
                                } else
                                    return sendQuestDialog(env, 1693);
                            }
                            break;
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203540)
                return defaultQuestEndDialog(env);
        }
        return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }
}
