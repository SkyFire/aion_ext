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
import gameserver.model.templates.quest.CollectItem;
import gameserver.model.templates.quest.CollectItems;
import gameserver.model.templates.quest.QuestDrop;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.List;

/**
 * @author MrPoke
 */
public class ItemCollecting extends QuestHandler {

    private final int questId;
    private final int startNpcId;
    private final int actionItemId;
    private final int endNpcId;
    private final int readableItemId;

    /**
     * @param questId
     * @param startNpcId
     * @param endNpcId
     * @param actionItemId
     */
    public ItemCollecting(int questId, int startNpcId, int actionItemId, int endNpcId, int readableItemId) {
        super(questId);
        this.questId = questId;
        this.startNpcId = startNpcId;
        this.actionItemId = actionItemId;
        if (endNpcId != 0)
            this.endNpcId = endNpcId;
        else
            this.endNpcId = startNpcId;
        this.readableItemId = readableItemId;
    }

    @Override
    public void register() {
        qe.setNpcQuestData(startNpcId).addOnQuestStart(questId);
        qe.setNpcQuestData(startNpcId).addOnTalkEvent(questId);
        if (actionItemId != 0)
            qe.setNpcQuestData(actionItemId).addOnTalkEvent(questId);
        if (endNpcId != startNpcId)
            qe.setNpcQuestData(endNpcId).addOnTalkEvent(questId);
        if (readableItemId != 0)
            qe.setQuestItemIds(readableItemId).add(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
        if (defaultQuestNoneDialog(env, template, startNpcId))
            return true;

        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        int var = qs.getQuestVarById(env.getQuestVarNum());
        if (qs.getStatus() == QuestStatus.START) {
            if (env.getTargetId() == endNpcId) {
                switch (env.getDialogId()) {
                    case 25:
                        if (var == 0)
                            return sendQuestDialog(env, 2375);
                    case 33:
                        return defaultQuestItemCheck(env, 0, 1, true, 5, 2716);
                }
            } else if (env.getTargetId() == actionItemId && env.getTargetId() != 0 &&
                    itemCollected(player, template, actionItemId))
                return true;
        }
        return defaultQuestRewardDialog(env, endNpcId, 0);
    }

    private boolean itemCollected(Player player, QuestTemplate template, int actionItemId) {
        List<QuestDrop> drops = template.getQuestDrop();
        int collectedItemId = 0;
        int count = 0;

        if (drops.isEmpty())
            return false;

        for (QuestDrop drop : drops) {
            if (drop.getNpcId() == actionItemId)
                collectedItemId = drop.getItemId();
        }

        if (collectedItemId == 0)
            return false;

        CollectItems collectitems = template.getCollectItems();
        if (collectitems != null) {
            List<CollectItem> collectitem = collectitems.getCollectItem();
            if (collectitem != null) {
                for (CollectItem ci : collectitem) {
                    if (ci.getItemId() == collectedItemId) {
                        count = ci.getCount();
                    }
                }
            }
        }

        if (player.getInventory().getItemCountByItemId(collectedItemId) < count)
            return true;
        return false;
    }

    @Override
    public boolean onItemUseEvent(QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (item.getItemId() != readableItemId)
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
