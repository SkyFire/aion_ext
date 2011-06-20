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
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

import java.util.Collections;

/**
 * @author Balthazar
 */

public class _1687TheTigrakiAgreement extends QuestHandler {
    private final static int questId = 1687;
    private int Choix;

    private static final Logger log = Logger.getLogger(_1687TheTigrakiAgreement.class);

    public _1687TheTigrakiAgreement() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204601).addOnQuestStart(questId);
        qe.setNpcQuestData(204601).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE) {
            if (targetId == 204601) {
                if (env.getDialogId() == 25) {
                    return sendQuestDialog(env, 4762);
                } else
                    return defaultQuestStartDialog(env);
            }
        }

        if (qs == null)
            return false;

        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 204601: {
                    switch (env.getDialogId()) {
                        case 25: {
                            long itemCount1 = player.getInventory().getItemCountByItemId(186000035);
                            long itemCount2 = player.getInventory().getItemCountByItemId(186000036);
                            if (itemCount1 >= 2 && itemCount2 >= 5) {
                                return sendQuestDialog(env, 1352);
                            }
                        }
                        case 10009: {
                            SetChoix(1);
                            return sendQuestDialog(env, 5);
                        }
                        case 10019: {
                            SetChoix(2);
                            return sendQuestDialog(env, 6);
                        }
                        case 10029: {
                            SetChoix(3);
                            return sendQuestDialog(env, 7);
                        }
                        case 8: {
                            log.info("Received Choix id :" + getChoix());
                            switch (getChoix()) {
                                case 1: {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(
                                            111100788, 1)))) {
                                        return true;
                                    }
                                    QuestFinish(env);
                                    return true;
                                }
                                case 2: {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(
                                            112100747, 1)))) {
                                        return true;
                                    }
                                    QuestFinish(env);
                                    return true;
                                }
                                case 3: {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(
                                            114100825, 1)))) {
                                        return true;
                                    }
                                    QuestFinish(env);
                                    return true;
                                }
                            }
                        }
                        case 9: {
                            switch (getChoix()) {
                                case 1: {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(
                                            111300792, 1)))) {
                                        return true;
                                    }
                                    QuestFinish(env);
                                    return true;
                                }
                                case 2: {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(
                                            112300744, 1)))) {
                                        return true;
                                    }
                                    QuestFinish(env);
                                    return true;
                                }
                                case 3: {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(
                                            114300851, 1)))) {
                                        return true;
                                    }
                                    QuestFinish(env);
                                    return true;
                                }
                            }
                        }
                        case 10: {
                            switch (getChoix()) {
                                case 1: {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(
                                            111500775, 1)))) {
                                        return true;
                                    }
                                    QuestFinish(env);
                                    return true;
                                }
                                case 2: {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(
                                            112500732, 1)))) {
                                        return true;
                                    }
                                    QuestFinish(env);
                                    return true;
                                }
                                case 3: {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(
                                            114500797, 1)))) {
                                        return true;
                                    }
                                    QuestFinish(env);
                                    return true;
                                }
                            }
                        }
                        case 11: {
                            switch (getChoix()) {
                                case 1: {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(
                                            111600767, 1)))) {
                                        return true;
                                    }
                                    QuestFinish(env);
                                    return true;
                                }
                                case 2: {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(
                                            112600743, 1)))) {
                                        return true;
                                    }
                                    QuestFinish(env);
                                    return true;
                                }
                                case 3: {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(
                                            114600754, 1)))) {
                                        return true;
                                    }
                                    QuestFinish(env);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public int getChoix() {
        return Choix;
    }

    public void SetChoix(int Choix) {
        this.Choix = Choix;
    }

    private void QuestFinish(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        player.getInventory().removeFromBagByItemId(186000035, 2);
        player.getInventory().removeFromBagByItemId(186000036, 5);
        qs.setStatus(QuestStatus.COMPLETE);
        qs.setCompliteCount(qs.getCompliteCount() + 1);
        int rewardExp = player.getRates().getQuestXpRate() * 1535800;
        int rewardAbyssPoint = player.getRates().getQuestXpRate() * 200;
        player.getCommonData().addExp(rewardExp);
        player.getCommonData().addAp(rewardAbyssPoint);
        PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(questId, QuestStatus.COMPLETE, 2));
        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
        updateQuestStatus(env);
    }
}