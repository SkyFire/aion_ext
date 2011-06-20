/**
 * This file is part of Aion Core <aioncore.com>
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
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.Collections;


/**
 *
 * Fixed for AXE by D3x
 *
 */
public class _2092GravesoftheRedSkyLegion extends QuestHandler {


    private final static int questId = 2092;
    private final static int[] npc_ids = {205150, 205188, 700394, 205190, 730156, 730158, 730159, 730160, 730161, 730162, 730163, 205208, 205209, 205210, 205211, 205212, 205213, 205214};

    public _2092GravesoftheRedSkyLegion() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(214402).addOnKillEvent(questId);
        qe.setNpcQuestData(214403).addOnKillEvent(questId);
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 2091);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        int[] mobs = {214402, 214403};
        if (defaultQuestOnKillEvent(env, mobs, 6, 20) || defaultQuestOnKillEvent(env, mobs, 20, true))
            return true;
        else
            return false;
    }

    @Override
    public boolean onDialogEvent(final QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final Npc npc = (Npc) env.getVisibleObject();
		final int targetObjectId = env.getVisibleObject().getObjectId();
		
        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 205150) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else return defaultQuestEndDialog(env);
            }
            return false;
        } else if (qs.getStatus() != QuestStatus.START) {
            return false;
        }
        if (targetId == 205150) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 0)
                        return sendQuestDialog(env, 1011);
                case 1012:
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 395));
                    break;
                case 10000:
                    if (var == 0) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        } else if (targetId == 205188) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 2)
                        return sendQuestDialog(env, 1693);
                case 10002:
                    if (var == 2) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        } else if (targetId == 205190) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 3)
                        return sendQuestDialog(env, 2034);
                    if (var == 4)
                        return sendQuestDialog(env, 2375);
                case 10003:
                    if (var == 3) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                case 10004:
                    if (var == 4) {
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                case 33:
                    if (var == 4) {
                        if (QuestService.collectItemCheck(env, true)) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            ItemService.addItems(player, Collections.singletonList(new QuestItems(182209009, 1)));
                            return sendQuestDialog(env, 10000);
                        } else
                            return sendQuestDialog(env, 10001);
                    }
            }
		} else if (targetId == 730156) { // Surdi's Tombstone
			if (var == 5) {
				QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 
				205208, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true); // Surdi's Ghost
				return true;
			}						
        } else if (targetId == 730158) { // Angeiya's Tombstone
			if (var == 5) {
				QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 
				205209, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true); // Angeiya's Ghost
				return true;
			}						
        } else if (targetId == 730159) { // Erna's Tombstone
			if (var == 5) {
				QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 
				205210, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true); // Erna's Ghost
				return true;
			}						
        } else if (targetId == 730160) { // Genta's Tombstone
			if (var == 5) {
				QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 
				205211, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true); // Genta's Ghost
				return true;
			}						
        } else if (targetId == 730161) { // Sith's Tombstone
			if (var == 5) {
				QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 
				205212, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true); // Sith's Ghost
				return true;
			}						
        } else if (targetId == 730162) { // Barache's Tombstone
			if (var == 5) {
				QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 
				205213, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true); // Barache's Ghost
				return true;
			}						
        } else if (targetId == 730163) { // Bert's Tombstone
			if (var == 5) {
				QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 
				205214, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true); // Bert's Ghost
				return true;
			}						
        } else if (targetId == 205208) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 5)
                        return sendQuestDialog(env, 2717);
                case 10005:
                    if (var == 5) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        player.getInventory().removeFromBagByItemId(182209009, 1);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        npc.getController().onDespawn(true);
						return true;
                    }
            }
        } else if (targetId == 205209) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 5)
                        return sendQuestDialog(env, 2802);
                case 10005:
                    if (var == 5) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        player.getInventory().removeFromBagByItemId(182209009, 1);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        npc.getController().onDespawn(true);
						return true;
                    }
            }
        } else if (targetId == 205210) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 5)
                        return sendQuestDialog(env, 2887);
                case 10005:
                    if (var == 5) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        player.getInventory().removeFromBagByItemId(182209009, 1);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						npc.getController().onDespawn(true);
                        return true;
                    }
            }
        } else if (targetId == 205211) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 5)
                        return sendQuestDialog(env, 2887);
                case 10005:
                    if (var == 5) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        player.getInventory().removeFromBagByItemId(182209009, 1);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						npc.getController().onDespawn(true);
                        return true;
                    }
            }
        } else if (targetId == 205212) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 5)
                        return sendQuestDialog(env, 2972);
                case 10005:
                    if (var == 5) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        player.getInventory().removeFromBagByItemId(182209009, 1);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						npc.getController().onDespawn(true);
                        return true;
                    }
            }
        } else if (targetId == 205213) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 5)
                        return sendQuestDialog(env, 3058);
                case 10005:
                    if (var == 5) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        player.getInventory().removeFromBagByItemId(182209009, 1);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						npc.getController().onDespawn(true);
                        return true;
                    }
            }
        } else if (targetId == 205214) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 5)
                        return sendQuestDialog(env, 3143);
                case 10005:
                    if (var == 5) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        player.getInventory().removeFromBagByItemId(182209009, 1);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						npc.getController().onDespawn(true);
                        return true;
                    }
            }
        } else if (targetId == 700394) {
            switch (env.getDialogId()) {
                case -1:
                    if (var == 1) {

                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                Npc npc = (Npc) player.getTarget();
                                if (npc == null || npc.getObjectId() != targetObjectId)
                                    return;
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
                                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);

                                qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                                updateQuestStatus(env);
                            }

                        }, 3000);
                        return false;
                    }
            }
        }
        return false;
    }
}