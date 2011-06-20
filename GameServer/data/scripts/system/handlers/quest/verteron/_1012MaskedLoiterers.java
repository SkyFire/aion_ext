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
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.zone.ZoneName;

public class _1012MaskedLoiterers extends QuestHandler {
    private final static int questId = 1012;

    public _1012MaskedLoiterers() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203111).addOnTalkEvent(questId);
        qe.setQuestEnterZone(ZoneName.Q1012).add(questId);
        qe.addQuestLvlUp(questId);
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
            if (targetId == 203111) {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 0)
                            return sendQuestDialog(env, 1011);
                        else if (var == 2)
                            return sendQuestDialog(env, 1352);
                        else if (var == 3)
                            return sendQuestDialog(env, 1693);
                    case 10000:
                        if (var == 0) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                    case 10001:
                        if (var == 2) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                    case 33:
                        if (var == 3) {
                            if (QuestService.collectItemCheck(env, true)) {
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else
                                return sendQuestDialog(env, 2034);
                        }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203111)
                return defaultQuestEndDialog(env);
        }
        return false;
    }

    @Override
    public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (zoneName != ZoneName.Q1012)
            return false;
        if (qs == null || qs.getQuestVars().getQuestVars() != 1)
            return false;
        qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
        updateQuestStatus(env);
        return true;
    }
}
