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

package quest.altgard;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_TRANSFORM;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;

/**
 * @author Mr. Poke
 */
public class _2021KnowYourEnemy extends QuestHandler {

    private final static int questId = 2021;

    public _2021KnowYourEnemy() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(203669).addOnTalkEvent(questId);
        qe.setQuestEnterZone(ZoneName.BLACK_CLAW_OUTPOST_220030000).add(questId);
        qe.setNpcQuestData(700099).addOnKillEvent(questId);
        qe.setNpcQuestData(203557).addOnTalkEvent(questId);
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

        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 203669:
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 0)
                                return sendQuestDialog(env, 1011);
                            else if (var == 2) {
                                player.setTransformedModelId(0);
                                PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player));
                                return sendQuestDialog(env, 1352);
                            } else if (var == 6)
                                return sendQuestDialog(env, 1693);
                            break;
                        case 10000:
                            if (var == 0) {
                                player.setTransformedModelId(202501);
                                PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player));
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                ThreadPoolManager.getInstance().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (player == null || player.getTransformedModelId() == 0)
                                            return;
                                        player.setTransformedModelId(0);
                                        PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player));
                                    }
                                }, 300000);
                                return true;
                            }
                            break;
                        case 10001:
                            if (var == 2) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                        case 10002:
                            if (var == 6) {
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                    }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203557) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 2034);
                else
                    return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 700099, 3, 6))
            return true;
        else
            return false;
    }

    @Override
    public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (zoneName != ZoneName.BLACK_CLAW_OUTPOST_220030000)
            return false;
        if (qs == null)
            return false;
        if (qs.getQuestVarById(0) == 1) {
            qs.setQuestVarById(0, 2);
            updateQuestStatus(env);
            return true;
        }
        return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }
}
