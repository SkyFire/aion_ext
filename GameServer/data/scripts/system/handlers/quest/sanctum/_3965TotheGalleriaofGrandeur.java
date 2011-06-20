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

package quest.sanctum;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;

import java.util.Collections;

public class _3965TotheGalleriaofGrandeur extends QuestHandler {
    private final static int questId = 3965;

    public _3965TotheGalleriaofGrandeur() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(798311).addOnQuestStart(questId); // Senarinrinerk
        qe.setNpcQuestData(798391).addOnTalkEvent(questId);  // Andu
        qe.setNpcQuestData(798390).addOnTalkEvent(questId);  // Palentine
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 798311)  // Senarinrinerk
        {
            if (env.getDialogId() == 25)
                return sendQuestDialog(env, 1011);
            else if (env.getDialogId() == 1002) {
                if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182206120, 2))))
                    return defaultQuestStartDialog(env);
                return true;
            } else
                return defaultQuestStartDialog(env);
        }

        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);

        if (targetId == 798391) // Andu
        {
            if (qs.getStatus() == QuestStatus.START && var == 0) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 10000) {
                    if (player.getInventory().getItemCountByItemId(182206120) > 1) {
                        player.getInventory().removeFromBagByItemId(182206120, 1);
                        qs.setQuestVar(++var);
                        updateQuestStatus(env);
                    }
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        } else if (targetId == 798390)  // Palentine
        {
            if (qs.getStatus() == QuestStatus.START && var == 1) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 2375);
                else if (env.getDialogId() == 1009) {
                    if (player.getInventory().getItemCountByItemId(182206120) > 0) {
                        player.getInventory().removeFromBagByItemId(182206120, 1);
                        qs.setQuestVar(++var);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return defaultQuestEndDialog(env);
                    }
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            } else if (qs.getStatus() == QuestStatus.REWARD) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 2375);
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }
}
