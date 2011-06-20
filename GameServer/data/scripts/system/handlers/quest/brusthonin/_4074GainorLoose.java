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

package quest.brusthonin;

import gameserver.dataholders.DataManager;
import gameserver.dataholders.QuestsData;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.model.templates.bonus.AbstractInventoryBonus;
import gameserver.model.templates.bonus.InventoryBonusType;
import gameserver.model.templates.bonus.RedeemBonus;
import gameserver.questEngine.HandlerResult;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;

/**
 *
 *
 */
public class _4074GainorLoose extends QuestHandler {

    static QuestsData questsData = DataManager.QUEST_DATA;
    private final static int questId = 4074;

    public _4074GainorLoose() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(205181).addOnQuestStart(questId);
        qe.setNpcQuestData(205181).addOnTalkEvent(questId);
        qe.setQuestBonusType(InventoryBonusType.REDEEM).add(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        if (player.getLevel() < 45)
            return false;
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestTemplate template = questsData.getQuestById(questId);

        if (targetId == 205181) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 53) {
                    QuestService.startQuest(env, QuestStatus.START);
                    return sendQuestDialog(env, 1011);
                }
            } else if (qs != null && qs.getStatus() == QuestStatus.START) {
                if (env.getDialogId() == 53) {
                    return sendQuestDialog(env, 1011);
                } else if (env.getDialogId() == 1011) {
                    if (player.getInventory().getItemCountByItemId(186000038) >= 1) {
                        if (player.getInventory().getKinahCount() < 1000)
                            return sendQuestDialog(env, 1011);
                        qs.setQuestVarById(1, 0);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 5);
                    } else {
                        return sendQuestDialog(env, 1009);
                    }
                } else if (env.getDialogId() == 1352) {
                    if (player.getInventory().getItemCountByItemId(186000038) >= 1) {
                        if (player.getInventory().getKinahCount() < 5000)
                            return sendQuestDialog(env, 1011);
                        qs.setQuestVarById(1, 1);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 6);
                    } else {
                        return sendQuestDialog(env, 1009);
                    }
                } else if (env.getDialogId() == 1693) {
                    if (player.getInventory().getItemCountByItemId(186000038) >= 1) {
                        if (player.getInventory().getKinahCount() < 25000)
                            return sendQuestDialog(env, 1011);
                        qs.setQuestVarById(1, 2);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 7);
                    } else {
                        return sendQuestDialog(env, 1009);
                    }
                }
            } else if (qs.getStatus() == QuestStatus.COMPLETE) {
                if (env.getDialogId() == 53) {
                    if ((qs.getCompliteCount() <= template.getMaxRepeatCount())) {
                        QuestService.startQuest(env, QuestStatus.START);
                        return sendQuestDialog(env, 1011);
                    } else
                        return true;
                }
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                return defaultQuestEndDialog(env, qs.getQuestVarById(1));
            }
        }
        return false;
    }

    @Override
    public HandlerResult onBonusApplyEvent(QuestCookie env, int index, AbstractInventoryBonus bonus) {
        if (!(bonus instanceof RedeemBonus))
            return HandlerResult.UNKNOWN;
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
            if (index == 0 && qs.getQuestVarById(1) == 0 ||
                    index == 1 && qs.getQuestVarById(1) == 1 ||
                    index == 2 && qs.getQuestVarById(1) == 2)
                return HandlerResult.SUCCESS;
        }
        return HandlerResult.FAILED;
    }
}
