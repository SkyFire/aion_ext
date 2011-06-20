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
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

import java.util.Collections;

/**
 * @author MrPoke
 */
public class _2207ConversingWithaSkurv extends QuestHandler {
    private final static int questId = 2207;

    public _2207ConversingWithaSkurv() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203590).addOnQuestStart(questId);
        qe.setNpcQuestData(203590).addOnTalkEvent(questId);
        qe.setNpcQuestData(203591).addOnTalkEvent(questId);
        qe.setNpcQuestData(203557).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203590) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else if (env.getDialogId() == 1002) {
                    if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182203257, 1))))
                        return defaultQuestStartDialog(env);
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        }
        if (qs == null)
            return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203591) {
                if (var == 0) {
                    if (env.getDialogId() == 25)
                        return sendQuestDialog(env, 1352);
                    else if (env.getDialogId() == 10000) {
                        qs.setQuestVar(1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
                        return true;
                    }
                }
                if (var == 2 || var == 3) {
                    if (env.getDialogId() == 25)
                        return sendQuestDialog(env, 2375);
                    else if (env.getDialogId() == 1009) {
                        qs.setQuestVar(3);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return defaultQuestEndDialog(env);
                    } else
                        return defaultQuestEndDialog(env);
                }
            } else if (targetId == 203557) {
                if (var == 1) {
                    if (env.getDialogId() == 25)
                        return sendQuestDialog(env, 1693);
                    else if (env.getDialogId() == 10001) {
                        qs.setQuestVar(2);
                        updateQuestStatus(env);
                        player.getInventory().removeFromBagByItemId(182203257, 1);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
