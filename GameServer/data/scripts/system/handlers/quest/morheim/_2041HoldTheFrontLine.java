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
package quest.morheim;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;

import gameserver.model.EmotionType;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Atomics @edit by Mcrizza -- @@edit by Dx
 */
public class _2041HoldTheFrontLine extends QuestHandler {

    private final static int questId = 2041;
    private final static int[] npcIds = {204301, 204403, 204432, 700183};
	//	204301 		- Aegir
	//	204403		- Taisan
	//	204432		- Kargate
	//	700183		- Morheim Abyss Gate

    public _2041HoldTheFrontLine() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addOnEnterWorld(questId);
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(214103).addOnKillEvent(questId);
        qe.addOnDie(questId);
        for (int npcId : npcIds)
            qe.setNpcQuestData(npcId).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
        if (qs == null || qs.getStatus() != QuestStatus.LOCKED || !lvlCheck)
            return false;
        int[] quests = {2300};
        for (int id : quests) {
            QuestState qs2 = player.getQuestStateList().getQuestState(id);
            if (qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE)
                return false;
        }

        qs.setStatus(QuestStatus.START);
        updateQuestStatus(env);
        return true;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;
        final int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204301) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 2375);
                return defaultQuestEndDialog(env);
            }
        } else if (qs.getStatus() != QuestStatus.START)
            return false;
        switch (targetId) {
            case 204301: // Aegir
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 0)
                            return sendQuestDialog(env, 1011);
                    case 10000:
                        if (var == 0) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            return sendQuestDialog(env, 0);
                        }
                    default:
                        return false;
                }
            case 204403: // Taisan
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 1)
                            return sendQuestDialog(env, 1352);
                    case 10001:
                        if (var == 1) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            return sendQuestDialog(env, 0);
                        }
                    default:
                        return false;
                }
			case 700183: { // Morheim Abyss Gate
                    if (qs.getQuestVarById(0) == 2) {
						//PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, 700183), true); // use item wait delay
						//Nees some fine touches - make it group, make it harder, more npc spawn in waves and attack Kargate (like retail)
						//mayb this needs core scripting.
						//Bregirun might need to be turned into instance and add separate spawns for both quests.
						//updateQuestStatus(env);
						TeleportService.teleportTo(player, 320030000, 274.96f, 168.04f, 204.40f, 34);
                        return true;
                    }
                }
            case 204432: // Kargate 
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 2)
                            return sendQuestDialog(env, 1693);
                        else if (var == 4)
                            return sendQuestDialog(env, 2034);
                    case 10002:
                        if (var == 2) {
                            qs.setQuestVarById(0, var + 2);
							//qs.setStatus(QuestStatus.REWARD);
					
							List<Npc> mobs = new ArrayList<Npc>();
							mobs.add((Npc) QuestService.addNewSpawn(320030000, 1, 213575, 282.54f, 179.13f, 204.32f, (byte) 63, true));
							mobs.add((Npc) QuestService.addNewSpawn(320030000, 1, 213575, 270.79f, 186.59f, 205.68f, (byte) 94, true));
							mobs.add((Npc) QuestService.addNewSpawn(320030000, 1, 213575, 262.48f, 175.50f, 204.87f, (byte) 3, true));
								for (Npc mob : mobs) {
									mob.getAggroList().addDamage(player, 1000);
								//	mob.getAggroList().addDamage(204432, 1000);
								}
                            updateQuestStatus(env);
                            return sendQuestDialog(env, 0);
                        }
                  case 10003:
                    if (var == 4) {
						// set reward + update
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						// end dialog quest - tp out
						TeleportService.teleportTo(player, 220020000, 3029.40f, 873.38f, 362.90f, 74);
						return sendQuestDialog(env, 0);
                      }
                    default:
                        return false;
                }
            default:
                return false;
        }
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START)
            return false;

        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (targetId == 213575) {
            if (var > 2 && var < 8) {
                qs.setQuestVarById(0, var + 1);
                return true;
            } else if (var == 8) {
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                return true;
            }
        }
        return false;
    }
}
