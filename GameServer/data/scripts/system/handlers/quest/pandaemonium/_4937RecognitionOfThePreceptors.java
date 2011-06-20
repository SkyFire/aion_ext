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
package quest.pandaemonium;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
/**
 * @author Nanou
 */
public class _4937RecognitionOfThePreceptors extends QuestHandler {
    private final static int questId = 4937;

    public _4937RecognitionOfThePreceptors() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204053).addOnQuestStart(questId);    //Kvasir
        qe.setNpcQuestData(204059).addOnTalkEvent(questId);        //Freyr
        qe.setNpcQuestData(204058).addOnTalkEvent(questId);        //Sif
        qe.setNpcQuestData(204057).addOnTalkEvent(questId);        //Sigyn
        qe.setNpcQuestData(204056).addOnTalkEvent(questId);        //Traufnir
        qe.setNpcQuestData(204075).addOnTalkEvent(questId);        //Balder
        qe.setNpcQuestData(204053).addOnTalkEvent(questId);        //Kvasir
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
        // 0 - Start to Kvasir
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204053) {
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
                // 1 - Get Freyr's signature on the Fenris's Fangs recommendation letter
                case 204059:
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
                // 2 - Get Sif's signature on the Fenris's Fangs recommendation letter.
                case 204058:
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
                // 3 - Get Sigyn's signature on the Fenris's Fangs recommendation letter.
                case 204057:
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
                // 4 - Get Traufnir's signature on the Fenris's Fangs recommendation letter.
                case 204056:
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
                // 5 - Take Glossy Holy Water to High Priest Balder for a purification ritual.
                case 204075:
                    if (var == 4) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                if (player.getInventory().getItemCountByItemId(186000084) >= 1)
                                    // Send select5 to eddit-HtmlPages.xml
                                    return sendQuestDialog(env, 2375);
                                else
                                    // Send select5_2 to eddit-HtmlPages.xml
                                    return sendQuestDialog(env, 2461);
                                // Get HACTION_SET_SUCCEED in the eddit-HyperLinks.xml
                            case 10255:
                                // Send select_success to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 10002);
                            // Get HACTION_SELECT_QUEST_REWARD in the eddit-HyperLinks.xml
                            case 1009:
                                player.getInventory().removeFromBagByItemId(186000084, 1);
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
            // 6 - Report the result to Kvasir
            if (targetId == 204053)
                return defaultQuestEndDialog(env);
        }
        return false;
    }
}