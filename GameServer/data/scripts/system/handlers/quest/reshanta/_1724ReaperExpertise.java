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

package quest.reshanta;

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

public class _1724ReaperExpertise extends QuestHandler {
    private final static int questId = 1724;

    public _1724ReaperExpertise() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(278519).addOnQuestStart(questId);
        qe.setNpcQuestData(278591).addOnTalkEvent(questId);
        qe.setNpcQuestData(278599).addOnTalkEvent(questId);
        qe.setNpcQuestData(278594).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (targetId == 278519) {
            if (qs == null) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else if (env.getDialogId() == 1003) {
                    if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182203131, 1))))
                        return defaultQuestStartDialog(env);
                    else
                        return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        } else if (qs != null && qs.getStatus() == QuestStatus.START) {
            if (targetId == 278591) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 10000) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                }
            } else if (targetId == 278599) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1693);
                else if (env.getDialogId() == 10001) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    qs.setStatus(QuestStatus.REWARD);
                    ItemService.addItems(player, Collections.singletonList(new QuestItems(182202152, 1)));
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                }
            }
        } else if (qs != null && qs.getStatus() == QuestStatus.REWARD && targetId == 278594) {
            return defaultQuestEndDialog(env);
        }
        return false;
    }
}
