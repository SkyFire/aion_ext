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
import gameserver.utils.PacketSendUtility;

public class _3934TheQuestForTemplars extends QuestHandler {
    private final static int questId = 3934;

    public _3934TheQuestForTemplars() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(203701).addOnQuestStart(questId);    //Lavirintos
        qe.setNpcQuestData(798359).addOnTalkEvent(questId);        //Nianalo
        qe.setNpcQuestData(798360).addOnTalkEvent(questId);        //Navid
        qe.setNpcQuestData(798361).addOnTalkEvent(questId);        //Pavel
        qe.setNpcQuestData(798362).addOnTalkEvent(questId);        //Pendaon
        qe.setNpcQuestData(798363).addOnTalkEvent(questId);        //Poevius
        qe.setNpcQuestData(798364).addOnTalkEvent(questId);        //Belicanon
        qe.setNpcQuestData(798365).addOnTalkEvent(questId);        //Mahelnu
        qe.setNpcQuestData(798366).addOnTalkEvent(questId);        //Pater
        qe.setNpcQuestData(203752).addOnTalkEvent(questId);        //Jucl�as
        qe.setNpcQuestData(203701).addOnTalkEvent(questId);        //Lavirintos
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        // Instanceof
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        // ------------------------------------------------------------
        // NPC Quest :
        // 0 - Start to Lavirintos
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203701) {
                // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                if (env.getDialogId() == 25)
                    // Send select_none to eddit-HtmlPages.xml
                    return sendQuestDialog(env, 4762);
                else
                    return defaultQuestStartDialog(env);

            }
        }

        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START) {

            switch (targetId) {
                // 1 - Talk with Nianalo
                case 798359:
                    switch (env.getDialogId()) {
                        // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                        case 25:
                            // Send select1 to eddit-HtmlPages.xml
                            return sendQuestDialog(env, 1011);
                        // Get HACTION_SETPRO1 in the eddit-HyperLinks.xml
                        case 10000:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
                    break;
                // 2 - Talk with Navid
                case 798360:
                    if (var == 1) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select2 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 1352);
                            // Get HACTION_SETPRO2 in the eddit-HyperLinks.xml
                            case 10001:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    break;
                // 3 - Talk with Pavel
                case 798361:
                    if (var == 2) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select3 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 1693);
                            // Get HACTION_SETPRO3 in the eddit-HyperLinks.xml
                            case 10002:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    break;
                // 4 - Talk with Pendaon
                case 798362:
                    if (var == 3) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select4 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 2034);
                            // Get HACTION_SETPRO4 in the eddit-HyperLinks.xml
                            case 10003:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    break;
                // 5 - Talk with Poevius
                case 798363:
                    if (var == 4) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select5 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 2375);
                            // Get HACTION_SETPRO5 in the eddit-HyperLinks.xml
                            case 10004:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    break;
                // 6 - Talk with Belicanon
                case 798364:
                    if (var == 5) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select6 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 2716);
                            // Get HACTION_SETPRO6 in the eddit-HyperLinks.xml
                            case 10005:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    break;
                // 7 - Talk with Mahelnu
                case 798365:
                    if (var == 6) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select7 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 3057);
                            // Get HACTION_SETPRO7 in the eddit-HyperLinks.xml
                            case 10006:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    break;
                // 8 - Talk with Pater
                case 798366:
                    if (var == 7) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select8 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 3398);
                            // Get HACTION_SETPRO8 in the eddit-HyperLinks.xml
                            case 10007:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    break;
                // 9 - Report the result to Jucleas with the Oath Stone
                case 203752:
                    if (var == 8) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                if (player.getInventory().getItemCountByItemId(186000080) >= 1)
                                    // Send select9 to eddit-HtmlPages.xml
                                    return sendQuestDialog(env, 3739);
                                else
                                    // Send select9_2 to eddit-HtmlPages.xml
                                    return sendQuestDialog(env, 3825);
                                // Get HACTION_SET_SUCCEED in the eddit-HyperLinks.xml
                            case 10255:
                                // Send select_success to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 10002);
                            // Get HACTION_SELECT_QUEST_REWARD in the eddit-HyperLinks.xml
                            case 1009:
                                player.getInventory().removeFromBagByItemId(186000080, 1);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                // Send select_quest_reward1 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 5);
                        }
                    }
                    break;
                // No match
                default:
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203701)
                return defaultQuestEndDialog(env);
        }
        return false;
    }
}
