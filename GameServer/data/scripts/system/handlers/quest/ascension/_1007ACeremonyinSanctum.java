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

package quest.ascension;

import gameserver.configs.main.CustomConfig;
import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.network.aion.serverpackets.SM_TELEPORT_LOC;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;

/**
 *
 *
 */
public class _1007ACeremonyinSanctum extends QuestHandler {
    private final static int questId = 1007;

    public _1007ACeremonyinSanctum() {
        super(questId);
    }

    @Override
    public void register() {
        if (CustomConfig.ENABLE_SIMPLE_2NDCLASS)
            return;
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(790001).addOnTalkEvent(questId);
        qe.setNpcQuestData(203725).addOnTalkEvent(questId);
        qe.setNpcQuestData(203752).addOnTalkEvent(questId);
        qe.setNpcQuestData(203758).addOnTalkEvent(questId);
        qe.setNpcQuestData(203759).addOnTalkEvent(questId);
        qe.setNpcQuestData(203760).addOnTalkEvent(questId);
        qe.setNpcQuestData(203761).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        int var = qs.getQuestVars().getQuestVars();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 790001) {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 0)
                            return sendQuestDialog(env, 1011);
                    case 10000:
                        if (var == 0) {
                            qs.setQuestVar(1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));

                            PacketSendUtility.sendPacket(player, new SM_TELEPORT_LOC(110010000, 1313, 1512, 568));
                            TeleportService.scheduleTeleportTask(player, 110010000, 1313, 1512, 568);
                            return true;
                        }
                }
            } else if (targetId == 203725) {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 1)
                            return sendQuestDialog(env, 1352);
                    case 1353:
                        if (var == 1) {
                            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 92));
                            return false;
                        }
                    case 10001:
                        if (var == 1) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
                                    .getObjectId(), 10));
                            return true;
                        }
                }
            } else if (targetId == 203752) {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 2)
                            return sendQuestDialog(env, 1693);
                    case 1694:
                        if (var == 2) {
                            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 91));
                            return false;
                        }
                    case 10002:
                        if (var == 2) {
                            PlayerClass playerClass = PlayerClass.getStartingClassFor(player.getCommonData().getPlayerClass());
                            if (playerClass == PlayerClass.WARRIOR)
                                qs.setQuestVar(10);
                            else if (playerClass == PlayerClass.SCOUT)
                                qs.setQuestVar(20);
                            else if (playerClass == PlayerClass.MAGE)
                                qs.setQuestVar(30);
                            else if (playerClass == PlayerClass.PRIEST)
                                qs.setQuestVar(40);
                            updateQuestStatus(env);
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
                                    .getObjectId(), 10));
                            return true;
                        }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203758 && var == 10) {
                switch (env.getDialogId()) {
                    case -1:
                        return sendQuestDialog(env, 2034);
                    case 1009:
                        return sendQuestDialog(env, 5);
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                        if (QuestService.questFinish(env, 0)) {
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                }
            } else if (targetId == 203759 && var == 20) {
                switch (env.getDialogId()) {
                    case -1:
                        return sendQuestDialog(env, 2375);
                    case 1009:
                        return sendQuestDialog(env, 6);
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                        if (QuestService.questFinish(env, 1)) {
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                }
            } else if (targetId == 203760 && var == 30) {
                switch (env.getDialogId()) {
                    case -1:
                        return sendQuestDialog(env, 2716);
                    case 1009:
                        return sendQuestDialog(env, 7);
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                        if (QuestService.questFinish(env, 2)) {
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                }
            } else if (targetId == 203761 && var == 40) {
                switch (env.getDialogId()) {
                    case -1:
                        return sendQuestDialog(env, 3057);
                    case 1009:
                        return sendQuestDialog(env, 8);
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                        if (QuestService.questFinish(env, 3)) {
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                }
            }
        }
        return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null)
            return false;
        QuestState qs2 = player.getQuestStateList().getQuestState(1006);
        if (qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE)
            return false;
        QuestService.startQuest(env, QuestStatus.START);
        return true;
    }
}
