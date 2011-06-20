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
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.WorldMapType;

/**
 * @author D3x
 */

public class _1063BrigadierIndratu extends QuestHandler {
    private final static int questId = 1063;

    public _1063BrigadierIndratu() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(204500).addOnTalkEvent(questId); // Perento
        qe.setNpcQuestData(203700).addOnTalkEvent(questId); // Fasimedes
        qe.setNpcQuestData(214159).addOnKillEvent(questId); // Brigadier Indratu
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs == null)
            return false;

        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 204500: {
                    switch (env.getDialogId()) {
                        case 25: {
                            if (qs.getQuestVarById(0) == 0) {
                                return sendQuestDialog(env, 1011);
                            }
							else if (qs.getQuestVarById(0) == 2) {
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 1693);
							}
                        }
                        case 10000: {
                            qs.setQuestVarById(0, 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203700) {
                switch (env.getDialogId()) {
                    case 25: {
                        return sendQuestDialog(env, 10002);
                    }
                    case 1009: {
                        return sendQuestDialog(env, 5);
                    }
                    default:
                        return defaultQuestEndDialog(env);
                }
            }
        }
        return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        int[] quests = {1500, 1051, 1052, 1054, 1053, 1055, 1056, 1059, 1057, 1058, 1062};
        return defaultQuestOnLvlUpEvent(env, quests);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 214159, 1, 2)) // needs a movie ?
            return true;
        else
            return false;
    }
}