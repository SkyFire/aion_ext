/*
 * This file is part of Aion X EMU <aionxemu.com>.
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
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
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

import java.util.Collections;

/**
 * @author Hellboy aion4Free
 */
public class _4940DecorationsofPandaemonium extends QuestHandler {
    private final static int questId = 4940;

    public _4940DecorationsofPandaemonium() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addOnEnterWorld(questId);
        qe.setNpcQuestData(204050).addOnQuestStart(questId);
        qe.setNpcQuestData(204050).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204050) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 4762);
                else if (env.getDialogId() == 1002) {
                    qs.setStatus(QuestStatus.START);
                    updateQuestStatus(env);
                    return sendQuestDialog(env, 1003);
                } else if (env.getDialogId() == 1007) {
                    return sendQuestDialog(env, 4);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 204050: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 0) {
                                if (player.getInventory().getItemCountByItemId(186000079) >= 30) {
                                    qs.setStatus(QuestStatus.COMPLETE);
                                    updateQuestStatus(env);
                                    return sendQuestDialog(env, 1182);
                                } else
                                    return sendQuestDialog(env, 1011);
                            } else if (var == 1)
                                return sendQuestDialog(env, 1352);
                        case 10000:
                            if (var == 0) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                        case 10001:
                            if (var == 1) {
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                        case 17:
                            ItemService.addItems(player, Collections.singletonList(new QuestItems(186000079, 1)));
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        case 33:
                            if (var == 1) {
                                if (QuestService.collectItemCheck(env, true)) {
                                    qs.setQuestVarById(0, 0);
                                    updateQuestStatus(env);
                                    return sendQuestDialog(env, 5);
                                } else
                                    return sendQuestDialog(env, 10001);
                            }
                    }
                }
                break;
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
        }
        return false;
    }

    @Override
    public boolean onEnterWorldEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (player.getLevel() >= 50) {
                if (player.getWorldId() == 120010000) {
                    QuestState qs2 = player.getQuestStateList().getQuestState(4938);
                    if (qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE)
                        return false;
                    QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), 4940, env.getDialogId()), QuestStatus.NONE);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
