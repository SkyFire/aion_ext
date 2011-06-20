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
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

import java.util.Collections;

/**
 * @author Nanou
 */
public class _4942ProvingProficiency extends QuestHandler {
    private final static int questId = 4942;

    public _4942ProvingProficiency() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204053).addOnQuestStart(questId);    //Kvasir
        qe.setNpcQuestData(204104).addOnTalkEvent(questId);        //Logi
        qe.setNpcQuestData(204108).addOnTalkEvent(questId);        //Lanse
        qe.setNpcQuestData(204106).addOnTalkEvent(questId);        //Kinterun
        qe.setNpcQuestData(204110).addOnTalkEvent(questId);        //Zyakia
        qe.setNpcQuestData(204100).addOnTalkEvent(questId);        //Lainita
        qe.setNpcQuestData(204102).addOnTalkEvent(questId);        //Honir
        qe.setNpcQuestData(798317).addOnTalkEvent(questId);        //Usener
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
                // 1 - Talk with Kvasir to choose the crafting skill
                case 204053:
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
                            // Get HACTION_SETPRO2 in the eddit-HyperLinks.xml
                            case 10001:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            // Get HACTION_SETPRO3 in the eddit-HyperLinks.xml
                            case 10002:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            // Get HACTION_SETPRO4 in the eddit-HyperLinks.xml
                            case 10003:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            // Get HACTION_SETPRO5 in the eddit-HyperLinks.xml
                            case 10004:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            // Get HACTION_SETPRO6 in the eddit-HyperLinks.xml
                            case 10005:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                        break;
                    }
                    // 2 - Talk with Weaponsmithing Master Logi.
                case 204104:
                    if (var == 1) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select2 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 1352);
                            // Get HACTION_SETPRO7 in the eddit-HyperLinks.xml
                            case 10006:
                                if (player.getInventory().getItemCountByItemId(152206598) == 0) {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(152206598, 1))))
                                        return true;
                                }
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    break;
                // 3 - Talk with Handicrafting Master Lanse
                case 204108:
                    if (var == 2) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select3 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 1693);
                            // Get HACTION_SETPRO7 in the eddit-HyperLinks.xml
                            case 10006:
                                if (player.getInventory().getItemCountByItemId(152206641) == 0) {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(152206641, 1))))
                                        return true;
                                }
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    break;
                // 4 - Talk with Armorsmithing Master Kinterun
                case 204106:
                    if (var == 3) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select4 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 2034);
                            // Get HACTION_SETPRO7 in the eddit-HyperLinks.xml
                            case 10006:
                                if (player.getInventory().getItemCountByItemId(152206617) == 0) {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(152206617, 1))))
                                        return true;
                                }
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    break;
                // 5 - Talk with Tailoring Master Zyakia
                case 204110:
                    if (var == 4) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select5 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 2375);
                            // Get HACTION_SETPRO7 in the eddit-HyperLinks.xml
                            case 10006:
                                if (player.getInventory().getItemCountByItemId(152206634) == 0) {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(152206634, 1))))
                                        return true;
                                }
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    break;
                // 6 - Talk with Cooking Master Lainita
                case 204100:
                    if (var == 5) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select6 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 2716);
                            // Get HACTION_SETPRO7 in the eddit-HyperLinks.xml
                            case 10006:
                                if (player.getInventory().getItemCountByItemId(152206646) == 0) {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(152206646, 1))))
                                        return true;
                                }
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    break;
                // 7 - Talk with Alchemy Master Honir
                case 204102:
                    if (var == 6) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select7 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 3057);
                            // Get HACTION_SETPRO7 in the eddit-HyperLinks.xml
                            case 10006:
                                if (player.getInventory().getItemCountByItemId(152206645) == 0) {
                                    if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(152206645, 1))))
                                        return true;
                                }
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    break;
                // 8 - Take Crafted Heart to Usener
                case 798317:
                    if (var == 7) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                // Send select8 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 3398);
                            // Get HACTION_CHECK_USER_HAS_QUEST_ITEM in the eddit-HyperLinks.xml
                            case 33:
                                if (player.getInventory().getItemCountByItemId(186000077) >= 1) {
                                    player.getInventory().removeFromBagByItemId(186000077, 1);
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
                // 10 - Take Brightly Glowing Holy Water to visit High Priest Balder for a purification ritual.
                case 204075:
                    if (var == 8) {
                        switch (env.getDialogId()) {
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 25:
                                if (player.getInventory().getItemCountByItemId(186000085) >= 1)
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
                                player.getInventory().removeFromBagByItemId(186000085, 1);
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
            // 11 - Talk with Kvasir
            if (targetId == 204053)
                return defaultQuestEndDialog(env);
        }
        return false;
    }
}