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

import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.world.zone.ZoneName;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.utils.PacketSendUtility;

public class _1023ANestofLepharists extends QuestHandler {
    private final static int questId = 1023;

    public _1023ANestofLepharists() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203098).addOnTalkEvent(questId);//Spatalos
        qe.setNpcQuestData(203183).addOnTalkEvent(questId);//Khidia
        qe.setQuestEnterZone(ZoneName.MYSTERIOUS_SHIPWRECK).add(questId);
        qe.addQuestLvlUp(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }

    @Override
    public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (zoneName != ZoneName.MYSTERIOUS_SHIPWRECK)
            return false;
        if (qs == null || qs.getQuestVarById(0) != 2)
            return false;
        qs.setQuestVarById(0, 3);
        updateQuestStatus(env);
		PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 23));
        return true;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        if (!super.defaultQuestOnDialogInitStart(env))
            return false;

        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START) {
            if (env.getTargetId() == 203098) {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 0)
                            return sendQuestDialog(env, 1011);
                    case 10000:
                        return defaultCloseDialog(env, 0, 1);
						                }
            } else if (env.getTargetId() == 203183)//Khidia
            {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 1)
                            return sendQuestDialog(env, 1352);
                        else if (var == 3)
                            return sendQuestDialog(env, 1693);
                        else if (var == 4)
                            return sendQuestDialog(env, 2034);
                    case 33:
                        return defaultQuestItemCheck(env, 4, 0, false, 2120, 2035);
                    case 10001:
                        return defaultCloseDialog(env, 1, 2);
                    case 10002:
                        return defaultCloseDialog(env, 3, 4);
                    case 10003:
                        return defaultCloseDialog(env, 4, 5, true, false);
                }
            }
        }
        return defaultQuestRewardDialog(env, 203098, 2375);
    }
}
