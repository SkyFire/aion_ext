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
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

import java.util.Collections;

/**
 * @author Nanou
 */
public class _4943LuckandPersistence extends QuestHandler {
    private final static int questId = 4943;

    private static final Logger log = Logger.getLogger(_4943LuckandPersistence.class);

    public _4943LuckandPersistence() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204053).addOnQuestStart(questId);    //Kvasir
        qe.setNpcQuestData(204096).addOnTalkEvent(questId);        //Latatusk
        qe.setNpcQuestData(204097).addOnTalkEvent(questId);        //Relir
        qe.setNpcQuestData(204075).addOnTalkEvent(questId);        //Balder
        qe.setNpcQuestData(204053).addOnTalkEvent(questId);    //Kvasir
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
                // 1 - Talk with Latatusk
                case 204096:
                    if (var == 0) {
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
                    }
                    // 3 - Collect the Light of Luck and take it to Latatusk
                    if (var == 2) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select3 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 1693);
                            // Get HACTION_CHECK_USER_HAS_QUEST_ITEM in the eddit-HyperLinks.xml
                            case 33:
                                if (player.getInventory().getItemCountByItemId(182207124) >= 20) {
                                    player.getInventory().removeFromBagByItemId(182207124, 20);
                                    qs.setQuestVarById(0, var + 1);
                                    updateQuestStatus(env);
                                    // Send check_user_item_ok to eddit-HtmlPages.xml
                                    return sendQuestDialog(env, 10000);
                                } else
                                    // Send check_user_item_fail to eddit-HtmlPages.xml
                                    return sendQuestDialog(env, 10001);
                        }
                    }
                    break;
                // 2 - Talk with Relir.
                case 204097:
                    if (var == 1) {
                        log.info("Received dialog id :" + env.getDialogId());
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select2 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 1352);
                            // Get HACTION_SETPRO2 in the eddit-HyperLinks.xml
                            case 1354:
                                PacketSendUtility.sendMessage(player, String.valueOf(player.getInventory().getKinahItem().getItemCount()));
                                if (player.getInventory().getKinahItem().getItemCount() >= 3400000) {
                                    player.getInventory().decreaseKinah(3400000);
                                    PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(player.getInventory().getKinahItem()));

                                    if (player.getInventory().getItemCountByItemId(182207123) == 0) {
                                        if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182207123, 1))))
                                            return true;
                                    }

                                    qs.setQuestVarById(0, var + 1);
                                    updateQuestStatus(env);
                                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                    return true;
                                } else {
                                    return sendQuestDialog(env, 1438);
                                }
                        }
                    }
                    break;
                // 4 - Better purify yourself! Take Glossy Holy Water and visit High Priest Balder
                case 204075:
                    if (var == 3) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                if (player.getInventory().getItemCountByItemId(186000084) >= 1)
                                    // Send select5 to eddit-HtmlPages.xml
                                    return sendQuestDialog(env, 2034);
                                else
                                    // Send select5_2 to eddit-HtmlPages.xml
                                    return sendQuestDialog(env, 2120);
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
            // 5 - Talk with Kvasir
            if (targetId == 204053)
                return defaultQuestEndDialog(env);
        }
        return false;
    }
}