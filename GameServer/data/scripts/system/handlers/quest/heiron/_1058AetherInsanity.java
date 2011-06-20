/*
 * This file is part of Aion X EMU <aionxemu.com>.
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.heiron;

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
 * @author Rhys2002
 */
public class _1058AetherInsanity extends QuestHandler {
    private final static int questId = 1058;

    public _1058AetherInsanity() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(204020).addOnTalkEvent(questId);
        qe.setNpcQuestData(204501).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 1500);
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

        if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204501)
                return defaultQuestEndDialog(env);
        } else if (qs.getStatus() != QuestStatus.START) {
            return false;
        }
        if (targetId == 204020) {
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
        } else if (targetId == 204501) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 1)
                        return sendQuestDialog(env, 1352);
                    else if (var == 2)
                        return sendQuestDialog(env, 1693);
                case 33:
                    if (QuestService.collectItemCheck(env, true)) {
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 5);
                    } else
                        return sendQuestDialog(env, 10001);
                case 1353:
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 191));
                    break;
                case 10001:
                    if (var == 1) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        }
        return false;
    }
}
