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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.Collections;

/**
 *
 *
 */
public class _4004TheSeedsOfHope extends QuestHandler {

    private final static int questId = 4004;

    public _4004TheSeedsOfHope() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(205128).addOnQuestStart(questId); //Randet
        qe.setNpcQuestData(700340).addOnTalkEvent(questId); //Earth Mound
    }

    @Override
    public boolean onDialogEvent(final QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        if (qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 205128) //Randet
            {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 4762);
                else if (env.getDialogId() == 1013) {
                    if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182209002, 1))))
                        return sendQuestDialog(env, 4);
                    else
                        return true;
                } else
                    return defaultQuestStartDialog(env);
            }
        }
        else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            switch (targetId) {
                case 700340: //Earth Mound
                {
                    if (env.getDialogId() == -1 && var >= 0 && var < 5) {
                        final int targetObjectId = env.getVisibleObject().getObjectId();
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                if (player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId)
                                    return;
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
                                        targetObjectId, 3000, 0));
                                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                                        targetObjectId), true);
                                player.getInventory().removeFromBagByItemId(182209002, 1);
                                qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                                if (qs.getQuestVarById(0) == 5)
                                    qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                ((Npc) player.getTarget()).getController().onDie(null);
                            }
                        }, 3000);
                    }
                }
            }
        }
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 205128) //Randet
            {
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }
}