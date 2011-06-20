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

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.TeleportService;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.Collections;

/**
 * @author Balthazar, fixed nur0ff
 */

public class _1636AFluteForTheFixing extends QuestHandler {
    private final static int questId = 1636;

    public _1636AFluteForTheFixing() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204535).addOnQuestStart(questId);
        qe.setNpcQuestData(204535).addOnTalkEvent(questId);
        qe.setNpcQuestData(203792).addOnTalkEvent(questId);
        qe.setQuestItemIds(182201785).add(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204535) {
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
                case 203792: {
                    switch (env.getDialogId()) {
                        case 25: {
                            long itemCount1 = player.getInventory().getItemCountByItemId(182201786);
                            long itemCount2 = player.getInventory().getItemCountByItemId(152020034);
                            long itemCount3 = player.getInventory().getItemCountByItemId(152020091);
                            long itemCount4 = player.getInventory().getItemCountByItemId(169400060);

                            if (qs.getQuestVarById(0) == 0) {
                                return sendQuestDialog(env, 1011);
                            } else if (qs.getQuestVarById(0) == 1 && itemCount1 >= 1 && itemCount2 >= 1 && itemCount3 >= 1
                                    && itemCount4 >= 1) {
                                return sendQuestDialog(env, 1352);
                            } else
                                return sendQuestDialog(env, 10001);
                        }
                        case 10000: {
                            qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
                                    .getObjectId(), 0));
                            return true;
                        }
                        case 33: {
                            qs.setQuestVarById(0, qs.getQuestVarById(0) + 2);
                            updateQuestStatus(env);

                            if (player.getInventory().getItemCountByItemId(182201785) == 0) {
                                if (!ItemService.addItems(player, Collections
                                        .singletonList(new QuestItems(182201785, 1)))) {
                                    return true;
                                }
                            }
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
                                    .getObjectId(), 0));
                            return true;
                        }
                    }
                }
				case 730033:
				{
					ThreadPoolManager.getInstance().schedule(new Runnable()
					{
						@Override
						public void run()
						{
							TeleportService.teleportTo(player, 210040000, (float) 186.27f, 2710.73f, 141.91f, 0);
						}
					}, 3000);
					return true;
				}
            }
        } 
		else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204535) {
                if (env.getDialogId() == 1009)
                    return sendQuestDialog(env, 5);
                else
                    return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onItemUseEvent(final QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (id != 182201785) {
            return false;
        }
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        }

        int var = qs.getQuestVars().getQuestVars();
        if (var != 3) {
            return false;
        }

        if (MathUtil.getDistance(182, 2703, 143, player.getPosition().getX(), player.getPosition().getY(), player
                .getPosition().getZ()) > 10) {
            return false;
        }

        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id,
                3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId,
                        id, 0, 1, 0), true);
                player.getInventory().removeFromBagByObjectId(itemObjId, 1);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
            }
        }, 3000);
        return true;
    }
}