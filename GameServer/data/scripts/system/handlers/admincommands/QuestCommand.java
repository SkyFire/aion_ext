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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author MrPoke
 */
public class QuestCommand extends AdminCommand {
    public QuestCommand() {
        super("quest");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() >= AdminConfig.COMMAND_QUESTCOMMAND) {
            if (params == null || params.length < 1) {
                PacketSendUtility.sendMessage(admin, "syntax //quest <start | set | vars>");
                return;
            }

            Player target = null;
            VisibleObject creature = admin.getTarget();
            if (admin.getTarget() instanceof Player) {
                target = (Player) creature;
            }

            if (target == null) {
                PacketSendUtility.sendMessage(admin, "Incorrect target!");
                return;
            }

            if (params[0].equals("start")) {
                if (params.length != 2) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest start <quest id>");
                    return;
                }
                int id;
                try {
                    id = Integer.valueOf(params[1]);
                }
                catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest start <quest id>");
                    return;
                }

                QuestCookie env = new QuestCookie(null, target, id, 0);

                if (QuestService.startQuest(env, QuestStatus.START)) {
                    PacketSendUtility.sendMessage(admin, "Quest started.");
                } else {
                    PacketSendUtility.sendMessage(admin, "Quest not started.");
                }
            } else if (params[0].equals("set") && params.length == 4) {
                int questId, var;
                QuestStatus questStatus;
                try {
                    questId = Integer.valueOf(params[1]);
                    questStatus = QuestStatus.valueOf(params[2]);
                    var = Integer.valueOf(params[3]);
                }
                catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest set <quest id status var>");
                    return;
                }
                catch (IllegalArgumentException e) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest set <quest id status var>");
                    return;
                }
                QuestState qs = target.getQuestStateList().getQuestState(questId);
                if (qs == null) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest set <quest id status var>");
                    return;
                }
                qs.setStatus(questStatus);
                qs.setQuestVar(var);
                PacketSendUtility.sendPacket(target, new SM_QUEST_ACCEPTED(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
            } else if (params[0].equals("set") && params.length == 5) {
                int questId, varId, var;
                QuestStatus questStatus;

                try {
                    questId = Integer.valueOf(params[1]);
                    questStatus = QuestStatus.valueOf(params[2]);
                    varId = Integer.valueOf(params[3]);
                    var = Integer.valueOf(params[4]);
                }
                catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest set <questId status varId var>");
                    return;
                }
                QuestState qs = target.getQuestStateList().getQuestState(questId);
                if (qs == null) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest set <questId status varId var>");
                    return;
                }
                qs.setStatus(questStatus);
                qs.setQuestVarById(varId, var);
                PacketSendUtility.sendPacket(target, new SM_QUEST_ACCEPTED(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
            } else if (params[0].equals("vars") && params.length == 2) {
                int questId, varId, var;
                QuestStatus questStatus;

                try {
                    questId = Integer.valueOf(params[1]);
                }
                catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest vars questId");
                    return;
                }
                QuestState qs = target.getQuestStateList().getQuestState(questId);
                if (qs == null) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest vars questId");
                    return;
                }
                PacketSendUtility.sendMessage(admin, "vars: "+ qs.getQuestVarById(0));
            } else
                PacketSendUtility.sendMessage(admin, "syntax //quest <start | set | vars>");
            return;
        } else if (admin.getAccessLevel() >= AdminConfig.COMMAND_QUESTCOMMANDPLAYERS) {
            if (params == null || params.length < 1) {
                PacketSendUtility.sendMessage(admin, "syntax //quest <restart>");
                return;
            }

            if (params[0].equals("restart")) {
                if (params.length != 2) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest restart <quest id>");
                    return;
                }
                int id;
                try {
                    id = Integer.valueOf(params[1]);
                }
                catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "syntax //quest restart <quest id>");
                    return;
                }

                QuestState qs = admin.getQuestStateList().getQuestState(id);

                if (qs == null || id == 1006 || id == 2008) {
                    PacketSendUtility.sendMessage(admin, "Quest " + id + " can't be restarted");
                    return;
                }

                if (qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.REWARD) {
                    if (qs.getQuestVarById(0) != 0) {
                        qs.setStatus(QuestStatus.START);
                        qs.setQuestVar(0);
                        PacketSendUtility.sendPacket(admin, new SM_QUEST_ACCEPTED(id, qs.getStatus(), qs.getQuestVars().getQuestVars()));
                        PacketSendUtility.sendMessage(admin, "Quest " + id + " restarted");
                    } else
                        PacketSendUtility.sendMessage(admin, "Quest " + id + " can't be restarted");
                } else {
                    PacketSendUtility.sendMessage(admin, "Quest " + id + " can't be restarted");
                }
            } else
                PacketSendUtility.sendMessage(admin, "syntax //quest <restart>");
            return;
        } else {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }
    }
}
