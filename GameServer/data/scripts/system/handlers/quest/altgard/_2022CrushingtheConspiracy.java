/**
 * This file is part of Aion X EMU <aionxemu.com>
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

package quest.altgard;

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
import gameserver.services.QuestService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

/**
 * @author HGabor85
 */
public class _2022CrushingtheConspiracy extends QuestHandler {
    private final static int questId = 2022;

    public _2022CrushingtheConspiracy() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(203557).addOnTalkEvent(questId); // Commander Suthran
        qe.setNpcQuestData(700143).addOnTalkEvent(questId); // Abyss Gate into Bregirun
        qe.setNpcQuestData(700142).addOnTalkEvent(questId); // Guardian Stone
        qe.setNpcQuestData(210753).addOnKillEvent(questId); // Kuninasha
        qe.setNpcQuestData(700140).addOnTalkEvent(questId); // Abyss Gate out of Bregirun
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            if (targetId == 203557) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 203557: {
                    if (qs.getQuestVarById(0) == 0) {
                        if (env.getDialogId() == 25)
                        {
                            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 66));
														return sendQuestDialog(env, 1011);
												}
                        else if (env.getDialogId() == 10000)
												{
                            qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));

                            return true;
                        }
                    }
                }
                break;
                case 700143: {
                    if (qs.getQuestVarById(0) == 1) {
                        qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                        updateQuestStatus(env);
                        TeleportService.teleportTo(player, 320030000, 275.68f, 164.03f, 205.19f, 34);
                        return true;
                    }
                }
                break;
                case 700142: {
                    if (qs.getQuestVarById(0) == 2) {
                        qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), 700142, 3000, 0));
                                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, 700142), true);
                                QuestService.addNewSpawn(320030000, 1, 210753, (float) 260.12, (float) 234.93, (float) 216.00, (byte) 90, true);
                            }
                        }, 3000);
                        return true;
                    }

                }
                break;
                case 700140: {
                    if (qs.getQuestVarById(0) == 4) {
                        TeleportService.teleportTo(player, 220030000, 2453.0f, 2553.2f, 316.3f, 26);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 154));
                        return true;
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203557)
            {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 1352);
                else
                    return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 210753, 3, 4))
            return true;
        else
            return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        int[] quests = {2200, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021};
        return defaultQuestOnLvlUpEvent(env, quests);
    }
}