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

package quest.pandaemonium;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldMapType;

import java.util.Collections;

public class _2938SecretLibraryAccess extends QuestHandler {
    private final static int questId = 2938;
    private final static int[] npc_ids = {204267, 203557}; // 204267 - Oubliette (start and finish), 203557 - Suthran(for recomendation)

    public _2938SecretLibraryAccess() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204267).addOnQuestStart(questId);
        for (int npc_id : npc_ids) {
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
        }
    }

    //// self explanatory ////////////////////////////////////////////////////////////////////
    private boolean AreAltgardQuestsFinished(Player player) {
        QuestState qs = player.getQuestStateList().getQuestState(2022); // last quest in Altgard state (Crushing the Conspiracy)
        return ((qs == null) || (qs.getStatus() != QuestStatus.COMPLETE)) ? false : true;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);

        if (targetId == 204267) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 4762);
                else return defaultQuestStartDialog(env);
            } 
			else if (qs.getStatus() == QuestStatus.REWARD && qs.getQuestVarById(0) == 0) {
                if (env.getDialogId() == -1 && qs.getStatus() == QuestStatus.REWARD)
                    return sendQuestDialog(env, 10002);
                else if (env.getDialogId() == 17) {
                    player.getInventory().removeFromBagByItemId(182207026, 1);
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    return defaultQuestEndDialog(env);
                } 
				else if (env.getDialogId() == 1009) {
                    return defaultQuestEndDialog(env);
                }
			} else if	(qs.getStatus() == QuestStatus.COMPLETE) {
				ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        TeleportService.teleportTo(player, WorldMapType.PANDAEMONIUM.getId(), 1403.2f, 1063.7f, 206.0f, 195);
                    }
                }, 3000);
            }
        } else if (targetId == 203557) {
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
                if (env.getDialogId() == 25) {
                    if (AreAltgardQuestsFinished(player)) {
                        return sendQuestDialog(env, 1011);
                    } else return sendQuestDialog(env, 1097);
                } else if (env.getDialogId() == 10255) {
                    if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182207026, 1)))) {
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                    }
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
                    return true;
                } else return defaultQuestStartDialog(env);
            }
        }
        return false;
    }
}
