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
package quest.inggison;

import java.util.Collections;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.model.templates.quest.QuestItems;
import gameserver.services.QuestService;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

/**
 * @author Dta3000
 */
    public class _11033_YouMakeMeSick extends QuestHandler {
    private final static int questId = 11033;

    public _11033_YouMakeMeSick() {
        super(questId);
       }

       @Override
       public void register() {
        int[] npcs = {798959};
        for (int npc : npcs)
        qe.setNpcQuestData(npc).addOnTalkEvent(questId);
         qe.setQuestItemIds(182206728).add(questId);
        qe.setNpcQuestData(798959).addOnQuestStart(questId);
    }

       @Override
        public boolean onDialogEvent(QuestCookie env) {
        if (defaultQuestNoneDialog(env, 798959, 4762))
        return true;

        final Player player = env.getPlayer();

        QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;
        int var = qs.getQuestVarById(0);
         if (qs.getStatus() == QuestStatus.REWARD) {
            if (env.getTargetId() == 798959) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else if (env.getDialogId() == 1009)
                    return sendQuestDialog(env, 5);
                else return defaultQuestEndDialog(env);
            }
            return false;
        }
        if (qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
                case 798959:
                    switch (env.getDialogId()) {
                        case 25:
                            if (var == 0)
                                return sendQuestDialog(env, 1011);
                             else if (var == 1)
                            return sendQuestDialog(env, 1352);
                        case 33:
                        if (var == 0) {
                        if (QuestService.collectItemCheck(env, true)) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        } else
                            return sendQuestDialog(env, 10001);
                    }
                         case 10001:
                            if (var == 1){
                            if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182206728, 1))))  
                             return true;
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);                          
                            }
                            return true;
                    }    
            }
        }
          return false;
    }

       @Override
       public boolean onItemUseEvent(final QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (id != 182206728)
            return false;
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 1000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                defaultQuestRemoveItem(env, 182206728, 1);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
            }
        }, 1000);
        return true;
    }
}
