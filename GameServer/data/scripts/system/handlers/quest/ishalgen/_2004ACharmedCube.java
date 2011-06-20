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
package quest.ishalgen;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

/**
 * @author Mr. Poke
 */
public class _2004ACharmedCube extends QuestHandler {
    private final static int questId = 2004;

    public _2004ACharmedCube() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(203539).addOnTalkEvent(questId);
        qe.setNpcQuestData(700047).addOnTalkEvent(questId);
        qe.setNpcQuestData(203550).addOnTalkEvent(questId);
        qe.setNpcQuestData(210402).addOnKillEvent(questId);
        qe.setNpcQuestData(210403).addOnKillEvent(questId);
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
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 203539: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 0)
                                return sendQuestDialog(env, 1011);
                            else if (var == 1)
                                return sendQuestDialog(env, 1352);
                            break;
                        case 10000:
                        case 10001:
                            if (var == 0 || var == 1) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                            break;
                        case 33:
                            if (var == 1) {
                                if (player.getInventory().getItemCountByItemId(182203005) > 0) {
                                    return sendQuestDialog(env, 1438);
                                } else
                                    return sendQuestDialog(env, 1353);
                            }
                            break;
                    }
                    return false;
                }
                case 700047: {
                    final int targetObjectId = env.getVisibleObject().getObjectId();
                    if (env.getDialogId() == -1) {
                        if (var == 1) {
                            PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
                                    1));
                            PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                                    targetObjectId), true);
                            ThreadPoolManager.getInstance().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    Npc npc = (Npc) player.getTarget();
                                    if (!player.isTargeting(targetObjectId))
                                        return;
                                    PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
                                            targetObjectId, 3000, 0));
                                    PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                                            targetObjectId), true);
                                    QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 211755, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true);
                                    npc.getController().onDie(null); //TODO check null or player
                                }
                            }, 3000);
                        }
                    }
                    return false;
                }
                case 203550: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 2)
                                return sendQuestDialog(env, 1693);
                            else if (var == 6)
                                return sendQuestDialog(env, 2034);
                            break;
                        case 10002:
                            if (var == 2) {
                                player.getInventory().removeFromBagByItemId(182203005, 1);
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                            break;
                        case 10003:
                            if (var == 6) {
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203539) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 2375);
                else
                    return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        int[] mobs = {210402, 210403};
        if (defaultQuestOnKillEvent(env, mobs, 3, 8) || defaultQuestOnKillEvent(env, mobs, 8, true))
            return true;
        else
            return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }
}
