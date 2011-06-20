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

package quest.sanctum;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

public class _3932StopTheShulacks extends QuestHandler {

    private final static int questId = 3932;
    private final static int[] npc_ids = {203711, 204656};
    /*
    * 203711 - Miriya
    * 204656 - Maloren
    *
    */

    public _3932StopTheShulacks() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203711).addOnQuestStart(questId);    //Miriya
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }


    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203711)//Miriya
            {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, 1011);
                else return defaultQuestStartDialog(env);

            }
            return false;
        }


        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203711)//Miriya
            {
                return defaultQuestEndDialog(env);
            }
            return false;
        } else if (qs.getStatus() == QuestStatus.START) {

            if (targetId == 203711 && var == 1)//Miriya
            {
                switch (env.getDialogId()) {
                    case 25:
                        return sendQuestDialog(env, 2375);
                    case 33:
                        if (QuestService.collectItemCheck(env, true)) {
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                            return sendQuestDialog(env, 5);
                        } else
                            return sendQuestDialog(env, 2716);
                }

            } else if (targetId == 204656 && var == 0)//Maloren
            {
                switch (env.getDialogId()) {
                    case 25:
                        return sendQuestDialog(env, 1352);
                    case 10000:
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        return true;
                }
            }


            return false;
        }
        return false;
    }

} 
