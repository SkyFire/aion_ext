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
package gameserver.questEngine.handlers.template;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

/**
 * @author MrPoke Like: Sleeping on the Job quest.
 */
public class ReportTo extends QuestHandler {

    private final int questId;
    private final int startNpc;
    private final int endNpc;
    private final int itemId;
    private final int readableItemId;

    /**
     * @param questId
     * @param startNpc
     * @param endNpc
     */
    public ReportTo(int questId, int startNpc, int endNpc, int itemId, int readableItemId) {
        super(questId);
        this.startNpc = startNpc;
        this.endNpc = endNpc;
        this.questId = questId;
        this.itemId = itemId;
        this.readableItemId = readableItemId;
    }

    @Override
    public void register() {
        qe.setNpcQuestData(startNpc).addOnQuestStart(questId);
        qe.setNpcQuestData(startNpc).addOnTalkEvent(questId);
        qe.setNpcQuestData(endNpc).addOnTalkEvent(questId);
        if (readableItemId != 0)
            qe.setQuestItemIds(readableItemId).add(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
        if (defaultQuestNoneDialog(env, template, startNpc, itemId, 1))
            return true;

        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        if (qs == null)
            return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            if (env.getTargetId() == endNpc) {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 0)
                            return sendQuestDialog(env, 2375);
                    case 1009:
                        if (qs.getStatus() != QuestStatus.COMPLETE) {
                            defaultQuestRemoveItem(env, itemId, 1);
                            return defaultCloseDialog(env, 0, 1, true, true);
                        }
                }
            }
        }
        return defaultQuestRewardDialog(env, endNpc, 0);
    }

    @Override
    public boolean onItemUseEvent(QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (id != readableItemId)
            return false;
        if (qs == null || qs.getStatus() == QuestStatus.NONE)
            return false;
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 1000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
            }
        }, 1000);
        return true;
    }
}
