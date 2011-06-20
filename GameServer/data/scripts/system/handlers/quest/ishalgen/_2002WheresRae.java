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
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.InstanceService;
import gameserver.services.QuestService;
import gameserver.services.TeleportService;
import gameserver.skillengine.SkillEngine;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldMapInstance;

/**
 * @author Mr. Poke, modified by Hellboy
 */
public class _2002WheresRae extends QuestHandler {
    private final static int questId = 2002;
    private final static int[] npc_ids = {203519, 203534, 203553, 700045, 203516, 205020, 203538};

    public _2002WheresRae() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(210377).addOnKillEvent(questId);
        qe.setNpcQuestData(210378).addOnKillEvent(questId);
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(final QuestCookie env) {
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
                case 203519: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 0)
                                return sendQuestDialog(env, 1011);
                        case 10000:
                            if (var == 0) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                    }
                }
                case 203534: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 1)
                                return sendQuestDialog(env, 1352);
                        case 1353:
                            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 52));
                            break;
                        case 10001:
                            if (var == 1) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                    }
                }
                case 790002: {
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 2)
                                return sendQuestDialog(env, 1693);
                            else if (var == 10)
                                return sendQuestDialog(env, 2034);
                            else if (var == 11)
                                return sendQuestDialog(env, 2375);
                            else if (var == 12)
                                return sendQuestDialog(env, 2462);
                            else if (var == 13)
                                return sendQuestDialog(env, 2716);
                        case 10002:
                        case 10003:
                        case 10005:
                            if (var == 2 || var == 10) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            } else if (var == 13) {
                                qs.setQuestVarById(0, 14);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                            break;
                        case 10004:
                            if (var == 12) {
                                qs.setQuestVarById(0, 99);
                                updateQuestStatus(env);
                                WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(320010000);
                                InstanceService.registerPlayerWithInstance(newInstance, player);
                                TeleportService.teleportTo(player, 320010000, newInstance.getInstanceId(), 457.65f, 426.8f, 230.4f, (byte) 75);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                        case 33:
                            if (var == 11) {
                                if (QuestService.collectItemCheck(env, true)) {
                                    qs.setQuestVarById(0, 12);
                                    updateQuestStatus(env);
                                    return sendQuestDialog(env, 2461);
                                } else
                                    return sendQuestDialog(env, 2376);
                            }
                    }
                }
                break;
                case 205020: {
                    switch (env.getDialogId()) {
                        case 25:
                            PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 3001, 0));
                            ThreadPoolManager.getInstance().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    qs.setQuestVar(13);
                                    updateQuestStatus(env);
                                    TeleportService.teleportTo(player, 220010000, 1, 940.15f, 2295.64f, 265.7f, (byte) 43);
                                }
                            }, 38000);
                            return true;
                        default:
                            return false;
                    }
                }
                case 700045:
                    if (var == 11 && env.getDialogId() == -1) {
                        final int targetObjectId = env.getVisibleObject().getObjectId();
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
                                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
                                SkillEngine.getInstance().getSkill(player, 8343, 1, player).useSkill();
                            }
                        }, 3000);
                    }
                    return true;
                case 203538:
                    if (var == 14 && env.getDialogId() == -1) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        Npc npc = (Npc) env.getVisibleObject();
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 203553, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true);
                        npc.getController().onDie(null); //TODO check null or player
                        npc.getController().onDespawn(true);
                        PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 256));
                        return true;
                    }
                    break;
                case 203553:
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 15)
                                return sendQuestDialog(env, 3057);
                        case 10006:
                            if (var == 15) {
                                env.getVisibleObject().getController().delete();
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                    }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203516) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 3398);
                else if (env.getDialogId() == 10007)
                    return sendQuestDialog(env, 5);
                else
                    return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        int[] mobs = {210377, 210378};
        if (defaultQuestOnKillEvent(env, mobs, 3, 10))
            return true;
        else
            return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }
}