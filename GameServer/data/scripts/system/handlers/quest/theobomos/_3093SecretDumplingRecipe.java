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

package quest.theobomos;

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

public class _3093SecretDumplingRecipe extends QuestHandler {

    private final static int questId = 3093;

    public _3093SecretDumplingRecipe() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(798185).addOnQuestStart(questId); //Bororinerk
        qe.setNpcQuestData(798185).addOnTalkEvent(questId); //Bororinerk
        qe.setNpcQuestData(798177).addOnTalkEvent(questId); //Gastak
        qe.setNpcQuestData(798179).addOnTalkEvent(questId); //Jabala
        qe.setNpcQuestData(203784).addOnTalkEvent(questId); //Hestia
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 798185) //Bororinerk
        {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else if (env.getDialogId() == 1002) {
                    if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182206062, 1))))
                        return defaultQuestStartDialog(env);
                    else
                        return true;
                } else
                    return defaultQuestStartDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) //Reward
            {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 2375);
                else if (env.getDialogId() == 1009) {
                    player.getInventory().removeFromBagByItemId(182208052, 1);
                    return sendQuestDialog(env, 5);
                } else
                    return defaultQuestEndDialog(env);
            }
        } else if (targetId == 798177) //Gastak
        {

            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 10000) {
                    player.getInventory().removeFromBagByItemId(182206062, 1);
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }

        } else if (targetId == 798179) //Jabala
        {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1693);
                else if (env.getDialogId() == 10001) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        } else if (targetId == 203784) //Hestia
        {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 3) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 2034);
                else if (env.getDialogId() == 10002) {
                    if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182208052, 1)))) ;
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        }

        return false;
    }
}
