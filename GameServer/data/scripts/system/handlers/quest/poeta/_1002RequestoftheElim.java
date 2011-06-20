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
package quest.poeta;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ASCENSION_MORPH;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldMapInstance;

/**
 * @author MrPoke
 */
public class _1002RequestoftheElim extends QuestHandler {

    private final static int questId = 1002;
    private final static int[] npc_ids = {203076, 730007, 730010, 730008, 205000, 203067};

    public _1002RequestoftheElim() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.addOnEnterWorld(questId);
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }

    @Override
    public boolean onDialogEvent(final QuestCookie env) {
        if (!super.defaultQuestOnDialogInitStart(env))
            return false;

        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
                case 203076:
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 0)
                                return sendQuestDialog(env, 1011);
                        case 10000:
                            return defaultCloseDialog(env, 0, 1);
                    }
                    break;
                case 730007:
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 1)
                                return sendQuestDialog(env, 1352);
                            else if (var == 5)
                                return sendQuestDialog(env, 1693, 0, 0, 182200002, 1);
                            else if (var == 6)
                                return sendQuestDialog(env, 2034);
                            else if (var == 12)
                                return sendQuestDialog(env, 2120);
                        case 1353:
                            return defaultQuestMovie(env, 20);
                        case 33:
                            return defaultQuestItemCheck(env, 6, 12, false, 2120, 2205);
                        case 10001:
                            return defaultCloseDialog(env, 1, 2, 182200002, 1, 0, 0);
                        case 10002:
                            return defaultCloseDialog(env, 5, 6);
                        case 10003:
                            return defaultCloseDialog(env, 12, 13);
                    }
                    break;
                case 730010:
                    switch (env.getDialogId()) {
                        case -1:
                            return defaultQuestUseNpc(env, 2, 5, EmotionType.START_QUESTLOOT, EmotionType.END_QUESTLOOT, true);
                        case 25:
                            if (defaultCloseDialog(env, 2, 4) || defaultCloseDialog(env, 4, 5)) {
                                Npc npc = (Npc) env.getVisibleObject();
                                if (npc != null)
                                    npc.getController().onDie(player);
                                return true;
                            }
                    }
                    break;
                case 730008:
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 13)
                                return sendQuestDialog(env, 2375);
                            else if (var == 14)
                                return sendQuestDialog(env, 2461);
                        case 10004:
                            if (defaultCloseDialog(env, 13, 20)) {
                                WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(310010000);
                                TeleportService.teleportTo(player, 310010000, newInstance.getInstanceId(), 52, 174, 229, 0);
                                return true;
                            }
                        case 10005:
                            return defaultCloseDialog(env, 14, 0, true, false);
                    }
                    break;
                case 205000:
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 20) {
                                PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 1001, 0));
                                final QuestState qs2 = qs;
                                ThreadPoolManager.getInstance().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        qs2.setQuestVar(14);
                                        updateQuestStatus(env);
                                        TeleportService.teleportTo(player, 210010000, 1, 603, 1537, 116, (byte) 20, 0);
                                    }
                                }, 43000);
                                return true;
                            }
                    }
                    break;
            }
        }
        return defaultQuestRewardDialog(env, 203067, 2716);
    }

    @Override
    public boolean onEnterWorldEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            if (player.getWorldId() == 310010000) {
                PacketSendUtility.sendPacket(player, new SM_ASCENSION_MORPH(1));
                return true;
            }
        }
        return false;
    }

    @Override
    public void QuestUseNpcInsideFunction(QuestCookie env) {
        Player player = env.getPlayer();
        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
    }
}
