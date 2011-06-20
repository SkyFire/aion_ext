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
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

public class _3936DecorationsOfSanctum extends QuestHandler {
    private final static int questId = 3936;

    public _3936DecorationsOfSanctum() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203710).addOnQuestStart(questId);    //Dairos
        qe.setNpcQuestData(203710).addOnTalkEvent(questId);        //Dairos
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        // Instanceof
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        // ------------------------------------------------------------
        // NPC Quest :
        // Start to Dairos
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203710) {
                // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                if (env.getDialogId() == 25)
                    // Send select_none to eddit-HtmlPages.xml
                    return sendQuestDialog(env, 4762);
                else
                    return defaultQuestStartDialog(env);

            }
        }

        if (qs == null)
            return false;

        if (qs.getStatus() == QuestStatus.START) {

            switch (targetId) {
                // 1 - Report the result to Dairos.
                case 203710:
                    switch (env.getDialogId()) {
                        // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                        case 25:
                            // Send select1 to eddit-HtmlPages.xml
                            return sendQuestDialog(env, 1011);
                        // Get HACTION_SETPRO1 in the eddit-HyperLinks.xml
                        case 10000:
                            // Send select2 to eddit-HtmlPages.xml
                            return sendQuestDialog(env, 1352);
                        // Get HACTION_CHECK_USER_HAS_QUEST_ITEM in the eddit-HyperLinks.xml
                        case 33:
                            // Collect Bloodwing Wings (10)
                            // Collect Sylphen Saliva (10)
                            // Collect Hornskull's Tongue (10)
                            // Collect Abex Horn (10)
                            if (player.getInventory().getItemCountByItemId(182206091) >= 10 &&
                                    player.getInventory().getItemCountByItemId(182206092) >= 10 &&
                                    player.getInventory().getItemCountByItemId(182206093) >= 10 &&
                                    player.getInventory().getItemCountByItemId(182206094) >= 10) {
                                player.getInventory().removeFromBagByItemId(182206091, 10);
                                player.getInventory().removeFromBagByItemId(182206092, 10);
                                player.getInventory().removeFromBagByItemId(182206093, 10);
                                player.getInventory().removeFromBagByItemId(182206094, 10);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                // Send select_quest_reward1 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 5);
                            } else {
                                // Send check_user_item_fail to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 10001);
                            }
                    }
                    break;
                // No match
                default:
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203710)
                return defaultQuestEndDialog(env);
        }
        return false;
    }
}