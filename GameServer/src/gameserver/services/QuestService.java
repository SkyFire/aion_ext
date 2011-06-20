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
package gameserver.services;

import com.aionemu.commons.utils.Rnd;
import gameserver.configs.main.GroupConfig;
import gameserver.dataholders.BonusData;
import gameserver.dataholders.DataManager;
import gameserver.dataholders.QuestsData;
import gameserver.model.PlayerClass;
import gameserver.model.TaskId;
import gameserver.model.drop.DropItem;
import gameserver.model.drop.DropTemplate;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.model.gameobjects.player.RewardType;
import gameserver.model.gameobjects.player.SkillListEntry;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.items.ItemId;
import gameserver.model.templates.QuestTemplate;
import gameserver.model.templates.bonus.AbstractInventoryBonus;
import gameserver.model.templates.bonus.BonusTemplate;
import gameserver.model.templates.bonus.SimpleCheckItemBonus;
import gameserver.model.templates.bonus.WrappedBonus;
import gameserver.model.templates.quest.*;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.questEngine.HandlerResult;
import gameserver.questEngine.QuestEngine;
import gameserver.questEngine.model.*;
import gameserver.spawnengine.SpawnEngine;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * @author Mr. Poke
 */
public final class QuestService {
    static QuestsData questsData = DataManager.QUEST_DATA;
    static BonusData bonusData = DataManager.BONUS_DATA;

    public static boolean questFinish(QuestCookie env) {
        return questFinish(env, 0);
    }

    public static boolean questFinish(QuestCookie env, int reward) {
        Player player = env.getPlayer();
        int id = env.getQuestId();
        QuestState qs = player.getQuestStateList().getQuestState(id);
        if (qs == null || qs.getStatus() != QuestStatus.REWARD)
            return false;
        QuestTemplate template = questsData.getQuestById(id);
        Storage inventory = player.getInventory();
        Rewards rewards = null;
        if (reward < template.getRewards().size())
            rewards = template.getRewards().get(reward);
        else
            rewards = new Rewards();
        List<QuestItems> questItems = new ArrayList<QuestItems>();
        questItems.addAll(rewards.getRewardItem());

        int dialogId = env.getDialogId();
        if (dialogId != 17 && dialogId != 0) {
            if (template.isUseClassReward()) {
                QuestItems classRewardItem = null;
                PlayerClass playerClass = player.getCommonData().getPlayerClass();
                switch (playerClass) {
                    case ASSASSIN:
                        classRewardItem = template.getAssassinSelectableReward().get(dialogId - 8);
                        break;
                    case CHANTER:
                        classRewardItem = template.getChanterSelectableReward().get(dialogId - 8);
                        break;
                    case CLERIC:
                        classRewardItem = template.getPriestSelectableReward().get(dialogId - 8);
                        break;
                    case GLADIATOR:
                        classRewardItem = template.getFighterSelectableReward().get(dialogId - 8);
                        break;
                    case RANGER:
                        classRewardItem = template.getRangerSelectableReward().get(dialogId - 8);
                        break;
                    case SORCERER:
                        classRewardItem = template.getWizardSelectableReward().get(dialogId - 8);
                        break;
                    case SPIRIT_MASTER:
                        classRewardItem = template.getElementalistSelectableReward().get(dialogId - 8);
                        break;
                    case TEMPLAR:
                        classRewardItem = template.getKnightSelectableReward().get(dialogId - 8);
                        break;
                }
                if (classRewardItem != null)
                    questItems.add(classRewardItem);
            } else {
                List<QuestItems> selectableRewardItems = rewards.getSelectableRewardItem();
                if (selectableRewardItems.size() <= (dialogId - 8))
                    return false;
                QuestItems selectebleRewardItem = selectableRewardItems.get(dialogId - 8);
                if (selectebleRewardItem != null)
                    questItems.add(selectebleRewardItem);
            }
        }
        if (ItemService.addItems(player, questItems)) {
            if (rewards.getGold() != null) {
                inventory.increaseKinah((player.getRates().getQuestKinahRate() * rewards.getGold()));
            }
            if (rewards.getExp() != null) {
                int rewardExp = rewards.getExp();
                player.getCommonData().addExp(rewardExp, RewardType.QUEST);
            }

            if (rewards.getTitle() != null) {
                player.getTitleList().addTitle(rewards.getTitle());
            }

            if (rewards.getRewardAbyssPoint() != null) {
                player.getCommonData().addAp(rewards.getRewardAbyssPoint());
            }

            if (rewards.getExtendInventory() != null) {
                if (rewards.getExtendInventory() == 1)
                    CubeExpandService.expand(player);
                else if (rewards.getExtendInventory() == 2)
                    WarehouseService.expand(player);
            }

            if (rewards.getExtendStigma() != null) {
                PlayerCommonData pcd = player.getCommonData();
                pcd.setAdvancedStigmaSlotSize(pcd.getAdvancedStigmaSlotSize() + 1);
                PacketSendUtility.sendPacket(player, new SM_CUBE_UPDATE(player, 6, pcd.getAdvancedStigmaSlotSize()));
            }

            //remove all worker list item if finished.
            QuestWorkItems qwi = questsData.getQuestById(id).getQuestWorkItems();

            if (qwi != null) {
                long count = 0;
                for (QuestItems qi : qwi.getQuestWorkItem()) {
                    if (qi != null) {
                        count = player.getInventory().getItemCountByItemId(qi.getItemId());
                        if (count > 0)
                            player.getInventory().removeFromBagByItemId(qi.getItemId(), count);
                    }
                }
            }

            BonusTemplate bonusTemplate = bonusData.getBonusInfoByQuestId(id);
            if (bonusTemplate != null) {
                QuestTemplate questTemplate = questsData.getQuestById(env.getQuestId());
                // if collectItems not null, the bonus for SimpleCheckItemBonus classes
                // was already applied, then skip them.
                boolean useCheckItems = questTemplate.getCollectItems() == null;

                List<AbstractInventoryBonus> bi = bonusTemplate.getItemBonuses();
                for (int i = 0; i < bi.size(); i++) {
                    AbstractInventoryBonus bonus = bi.get(i);
                    HandlerResult result = QuestEngine.getInstance().onBonusApply(env, i, bonus);
                    if (result == HandlerResult.FAILED)
                        continue; // bonus can not be applied

                    if (bonus instanceof SimpleCheckItemBonus) {
                        if (!useCheckItems)
                            continue;
                        SimpleCheckItemBonus scb = (SimpleCheckItemBonus) bonus;
                        if (!scb.canApply(player, env.getQuestId()))
                            continue;

                        player.getInventory().removeFromBagByItemId(scb.getCheckedItemId(), scb.getCount());
                        scb.apply(player, null);
                    } else if (!(bonus instanceof WrappedBonus)) {
                        bonus.apply(player, null);
                    }
                }
            }

            QuestEngine.getInstance().onQuestFinish(env);
            qs.setStatus(QuestStatus.COMPLETE);
            qs.setCompliteCount(qs.getCompliteCount() + 1);
            PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(id, qs.getStatus(), qs.getQuestVars().getQuestVars()));
            player.getController().updateNearbyQuests();
            QuestEngine.getInstance().onLvlUp(env);
            return true;
        }
        return true;
    }

    public static boolean checkStartCondition(QuestCookie env) {
        Player player = env.getPlayer();
        QuestTemplate template = questsData.getQuestById(env.getQuestId());

        if (template == null)
            return false;

        if (template.getRacePermitted() != null) {
            if (template.getRacePermitted().ordinal() != player.getCommonData().getRace().ordinal())
                return false;
        }

        // min level - 2 so that the gray quest arrow shows when quest is almost available
        // quest level will be checked again in QuestService.startQuest() when attempting to start
        if (player.getLevel() < template.getMinlevelPermitted() - 2)
            return false;

        if (template.getClassPermitted().size() != 0) {
            if (!template.getClassPermitted().contains(player.getCommonData().getPlayerClass()))
                return false;
        }

        if (template.getGenderPermitted() != null) {
            if (template.getGenderPermitted().ordinal() != player.getGender().ordinal())
                return false;
        }

        QuestStartConditions startConditions = template.getFinishedQuestConds();
        if (startConditions != null) {
            for (QuestStartCondition condition : startConditions.getCondition()) {
                QuestState qs = player.getQuestStateList().getQuestState(condition.getQuestId());
                if (qs == null)
                    return false;
                if (condition.getStep() == 0 && qs.getStatus() != QuestStatus.COMPLETE)
                    return false;
                if (condition.getStep() > qs.getQuestVarById(0))
                    return false;
            }
        }

        startConditions = template.getUnfinishedQuestConds();
        if (startConditions != null) {
            for (QuestStartCondition condition : startConditions.getCondition()) {
                QuestState qs = player.getQuestStateList().getQuestState(condition.getQuestId());
                if (qs != null && qs.getStatus() == QuestStatus.COMPLETE)
                    return false;
            }
        }

        startConditions = template.getAcquiredQuestConds();
        if (startConditions != null) {
            for (QuestStartCondition condition : startConditions.getCondition()) {
                QuestState qs = player.getQuestStateList().getQuestState(condition.getQuestId());
                if (qs == null)
                    return false;
            }
        }

        startConditions = template.getNoacquiredQuestConds();
        if (startConditions != null) {
            for (QuestStartCondition condition : startConditions.getCondition()) {
                QuestState qs = player.getQuestStateList().getQuestState(condition.getQuestId());
                if (qs != null)
                    return false;
            }
        }

        if (template.getCombineSkill() != null) {
            SkillListEntry skill = player.getSkillList().getSkillEntry(template.getCombineSkill());
            if (skill == null)
                return false;
            if (skill.getSkillLevel() < template.getCombineSkillPoint() || skill.getSkillLevel() - 40 > template.getCombineSkillPoint())
                return false;
            return true;
        }

        QuestState qs = player.getQuestStateList().getQuestState(template.getId());
        if (qs != null && qs.getStatus().value() > 0) {
            if (qs.getStatus() == QuestStatus.COMPLETE && (qs.getCompliteCount() <= template.getMaxRepeatCount())) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean startQuest(QuestCookie env, QuestStatus questStatus) {
        Player player = env.getPlayer();
        int id = env.getQuestId();
        QuestTemplate template = questsData.getQuestById(env.getQuestId());
        if (questStatus != QuestStatus.LOCKED) {
            if (!checkStartCondition(env))
                return false;

            if (player.getLevel() < template.getMinlevelPermitted()) {
                // Should not reach this point. Except for a location started quest.
                return false;
            }
        }
        PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(id, questStatus.value(), 0));
        QuestState qs = player.getQuestStateList().getQuestState(id);
        if (qs == null) {
            qs = new QuestState(template.getId(), questStatus, 0, 0);
            player.getQuestStateList().addQuest(id, qs);
        } else {
            if (template.getMaxRepeatCount() >= qs.getCompliteCount()) {
                qs.setStatus(questStatus);
                qs.setQuestVar(0);
            }
        }

        player.getController().updateNearbyQuests();
        return true;
    }

    public boolean questComplite(QuestCookie env) {
        Player player = env.getPlayer();
        int id = env.getQuestId();
        QuestState qs = player.getQuestStateList().getQuestState(id);
        if (qs == null || qs.getStatus() != QuestStatus.START)
            return false;

        qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
        qs.setStatus(QuestStatus.REWARD);
        PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(id, qs.getStatus(), qs.getQuestVars().getQuestVars()));
        player.getController().updateNearbyQuests();
        return true;
    }

    public static boolean collectItemCheck(QuestCookie env, boolean removeItem) {
        Player player = env.getPlayer();
        int id = env.getQuestId();
        QuestState qs = player.getQuestStateList().getQuestState(id);
        if (qs == null)
            return false;

        QuestTemplate template = questsData.getQuestById(env.getQuestId());
        CollectItems collectItems = template.getCollectItems();
        if (collectItems == null)
            return true;

        for (CollectItem collectItem : collectItems.getCollectItem()) {
            int itemId = collectItem.getItemId();
            long count = itemId == ItemId.KINAH.value() ?
                    player.getInventory().getKinahCount() :
                    player.getInventory().getItemCountByItemId(itemId);
            if (collectItem.getCount() > count)
                return false;
        }
        if (removeItem) {
            BonusTemplate bonusTemplate = bonusData.getBonusInfoByQuestId(id);
            List<SimpleCheckItemBonus> appliedBonuses = null;
            if (bonusTemplate != null) {
                List<AbstractInventoryBonus> bi = bonusTemplate.getItemBonuses();
                int checks = 0;
                for (int i = 0; i < bi.size(); i++) {
                    AbstractInventoryBonus bonus = bi.get(i);
                    if (!(bonus instanceof SimpleCheckItemBonus))
                        continue;
                    checks++;
                    SimpleCheckItemBonus scb = (SimpleCheckItemBonus) bonus;
                    HandlerResult result = QuestEngine.getInstance().onBonusApply(env, i, bonus);
                    if (result == HandlerResult.UNKNOWN) {
                        if (scb.canApply(player, env.getQuestId())) {
                            if (appliedBonuses == null)
                                appliedBonuses = new ArrayList<SimpleCheckItemBonus>();
                            appliedBonuses.add(scb);
                        }
                    } else if (result == HandlerResult.SUCCESS)
                        appliedBonuses.add(scb);
                }
                // If check constraints added (like in redeems), returns no bonus
                if (checks > 0 && appliedBonuses == null)
                    return false;
            }

            for (CollectItem collectItem : collectItems.getCollectItem()) {
                if (collectItem.getItemId() == 182400001)
                    player.getInventory().decreaseKinah(collectItem.getCount());
                else
                    player.getInventory().removeFromBagByItemId(collectItem.getItemId(), collectItem.getCount());
            }

            if (appliedBonuses != null) {
                for (AbstractInventoryBonus bonus : appliedBonuses) {
                    // TODO: we can not check if the inventory is full
                    // when called from the script
                    bonus.apply(player, null);
                }
            }
        }
        return true;
    }

    public static VisibleObject addNewSpawn(int worldId, int instanceId, int templateId, float x, float y, float z, byte heading, boolean noRespawn) {
        SpawnTemplate spawn = SpawnEngine.getInstance().addNewSpawn(worldId, instanceId, templateId, x, y, z, heading, 0, 0, noRespawn);
        return SpawnEngine.getInstance().spawnObject(spawn, instanceId);
    }

    public static void getQuestDrop(Set<DropItem> dropItems, Npc npc, Player player) {
        List<QuestDrop> drops = QuestEngine.getInstance().getQuestDrop(npc.getNpcId());
        if (drops.isEmpty() || player.isInAlliance())
            return;
        List<Player> players = new ArrayList<Player>();
        if (player.isInGroup()) {
            players.add(player); // add the first
            for (Player member : player.getPlayerGroup().getMembers()) {
                if (player.equals(member))
                    continue;
                if (MathUtil.isInRange(member, npc, GroupConfig.GROUP_MAX_DISTANCE)) {
                    players.add(member);
                }
            }
        } else {
            players.add(player);
        }
        for (QuestDrop drop : drops) {
            boolean droppedOnce = false;
            boolean isItemDropped = false;
            DropItem droppedOnceItem = null;

            // create new DropItem with player not set, so we could compare hashes
            DropItem item = new DropItem(new DropTemplate(drop.getNpcId(), drop.getItemId(), 1, 1, drop.getChance()));
            // protection against overriding chances from DB
            if (dropItems.contains(item)) // for this player from DB data
                dropItems.remove(item); // removes DB drop

            for (Player member : players) {
                if (!hasQuestForDrop(member, drop))
                    continue;

                if (!drop.isDropEachMember()) {
                    if (!droppedOnce) {
                        isItemDropped = isDrop(member, drop);
                        droppedOnce = true;
                    } else {
                        if (isItemDropped)
                            droppedOnceItem.addQuestPlayerObjId(member.getObjectId());
                        continue;
                    }
                } else {
                    isItemDropped = isDrop(player, drop);
                }

                // create a new DropItem if its null
                if (item == null)
                    item = new DropItem(new DropTemplate(drop.getNpcId(), drop.getItemId(), 1, 1, drop.getChance()));

                if (drop.isDropEachMember())
                    item.setQuestDropForEachMemeber();
                if (droppedOnce && isItemDropped)
                    droppedOnceItem = item;

                item.addQuestPlayerObjId(member.getObjectId());
                item.setCount(1);

                if (isItemDropped && !droppedOnce)
                    dropItems.add(item);
                item = null;
            }
            if (droppedOnce && isItemDropped)
                dropItems.add(droppedOnceItem);
        }
    }

    private static boolean isDrop(Player player, QuestDrop drop) {
        if (Rnd.get() * 100 > drop.getChance())
            return false;
        return true;
    }

    /**
     * Check if a player is doing a quest for the given drop.
     *
     * @param player
     * @param drop
     * @return
     */
    private static boolean hasQuestForDrop(Player player, QuestDrop drop) {
        int questId = drop.getQuestId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START)
            return false;
        QuestTemplate template = questsData.getQuestById(questId);
        CollectItems collectItems = template.getCollectItems();
        if (collectItems == null)
            return true;

        for (CollectItem collectItem : collectItems.getCollectItem()) {
            int collectItemId = collectItem.getItemId();
            int dropItemId = drop.getItemId();
            if (collectItemId != dropItemId)
                continue;
            long count = player.getInventory().getItemCountByItemId(collectItemId);
            if (collectItem.getCount() > count)
                return true;
        }
        return false;
    }

    /**
     * @param id
     * @param playerLevel
     * @return false if player is 2 or more levels below quest level
     */
    public static boolean checkLevelRequirement(int questId, int playerLevel) {
        QuestTemplate template = questsData.getQuestById(questId);
        return (playerLevel >= template.getMinlevelPermitted());
    }

    public static boolean questTimerStart(QuestCookie env, int timeInSeconds) {
        final Player player = env.getPlayer();
        final int id = env.getQuestId();

        // Schedule Action When Timer Finishes
        Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                QuestEngine.getInstance().onQuestTimerEnd(new QuestCookie(null, player, 0, 0));
                QuestEngine.getInstance().deleteQuest(player, id);
                PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(id));
                player.getController().updateNearbyQuests();
            }
        }, timeInSeconds * 1000);
        player.getController().addTask(TaskId.QUEST_TIMER, task);
        PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(id, timeInSeconds));
        return true;
    }

    public static boolean questTimerEnd(QuestCookie env) {
        final Player player = env.getPlayer();
        final int id = env.getQuestId();

        player.getController().cancelTask(TaskId.QUEST_TIMER);
        PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(id, 0));
	return true;
	}
}
