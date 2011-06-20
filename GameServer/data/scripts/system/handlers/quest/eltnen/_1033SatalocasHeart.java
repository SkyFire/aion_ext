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
package quest.eltnen;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

/**
 * @author Xitanium
 */
public class _1033SatalocasHeart extends QuestHandler {

    private final static int questId = 1033;
    private final static int[] mob_ids = {210799}; //Archon Drake

    public _1033SatalocasHeart() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203900).addOnTalkEvent(questId); //Diomedes
        qe.setNpcQuestData(203996).addOnTalkEvent(questId); //Kimeia
        for (int mob_id : mob_ids)
            qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
        qe.addQuestLvlUp(questId);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 210799, 10, 11))
            return true;
        else
            return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;
        if (targetId == 203900) //Diomedes
        {
            if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else if (env.getDialogId() == 10000) {
                    qs.setQuestVar(1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
                            .getObjectId(), 10));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            } else if (qs.getStatus() == QuestStatus.REWARD) {
                return defaultQuestEndDialog(env);
            }
        } else if (targetId == 203996) //Kimeia
        {
            if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1693);
                else if (env.getDialogId() == 10002) {
                    qs.setQuestVar(10);
                    updateQuestStatus(env);
                    PacketSendUtility
                            .sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 42));
                    return true;
                } else
                    return defaultQuestStartDialog(env);
            } else if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 11) {
                if (env.getDialogId() == 25) {
                    qs.setQuestVar(qs.getQuestVarById(0) + 1);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    return sendQuestDialog(env, 2205);
                } else
                    return defaultQuestStartDialog(env);
            }

        }
        return false;

    }
}
