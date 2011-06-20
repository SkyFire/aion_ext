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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import gameserver.configs.main.CustomConfig;
import gameserver.dao.DropListDAO;
import gameserver.model.DescriptionId;
import gameserver.model.EmotionType;
import gameserver.model.drop.DropItem;
import gameserver.model.drop.DropList;
import gameserver.model.drop.DropTemplate;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.DropNpc;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.templates.item.ItemQuality;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.stats.NpcRank;
import gameserver.network.aion.serverpackets.*;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.stats.DropRewardEnum;
import gameserver.world.Executor;
import gameserver.world.World;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.lang.Object;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ATracer
 * @author Jego
 */
public class DropService {
    private static final Logger log = Logger.getLogger(DropService.class);

    private DropList dropList;

    private Map<Integer, Set<DropItem>> currentDropMap = new FastMap<Integer, Set<DropItem>>().shared();
    private Map<Integer, DropNpc> dropRegistrationMap = new FastMap<Integer, DropNpc>().shared();

    /**
     * Integer is the group/alliance Id
     */
    private Map<Integer, DropNpc> specialDropMap = new FastMap<Integer, DropNpc>().shared();
    private final ReentrantLock specialDropLock = new ReentrantLock();

    public static final DropService getInstance() {
        return SingletonHolder.instance;
    }

    private DropService() {
        dropList = DAOManager.getDAO(DropListDAO.class).load();
        log.info(dropList.getSize() + " npc drops loaded");
    }

    /**
     * @return the dropList
     */
    public DropList getDropList() {
        return dropList;
    }

    /**
     * After NPC dies - it can register arbitrary drop
     *
     * @param npc
     */
    public void registerDrop(Npc npc, Player player, int lvl) {
        List<Player> players = new ArrayList<Player>();
        players.add(player);
        registerDrop(npc, player, lvl, players);
    }

    private int getDropPoints(Npc npc)
    {
        int value;
        NpcRank quality = npc.getObjectTemplate().getRank();
        switch (quality) {
            case NORMAL:
                value = CustomConfig.DROP_POINTS_NPC_NORMAL;
                break;
            case ELITE:
                value = CustomConfig.DROP_POINTS_NPC_ELITE;
                break;
            case JUNK:
                value = CustomConfig.DROP_POINTS_NPC_JUNK;
                break;
            case HERO:
                value = CustomConfig.DROP_POINTS_NPC_HERO;
                break;
            case LEGENDARY:
                value = CustomConfig.DROP_POINTS_NPC_CHAMPION;
                break;
            default:
                value = 10;
        }
        return value;
    }

    private int getDropItemPointsByQuality(ItemQuality quality)
    {
        int value;
        switch (quality) {
            case MYTHIC:
                value = CustomConfig.DROP_POINTS_ITEM_MYTHIC;
                break;
            case EPIC:
                value = CustomConfig.DROP_POINTS_ITEM_EPIC;
                break;
            case UNIQUE:
                value = CustomConfig.DROP_POINTS_ITEM_UNIQUE;
                break;
            case LEGEND:
                value = CustomConfig.DROP_POINTS_ITEM_LEGEND;
                break;
            case RARE:
                value = CustomConfig.DROP_POINTS_ITEM_RARE;
                break;
            case COMMON:
                value = CustomConfig.DROP_POINTS_ITEM_COMMON;
                break;
            case JUNK:
                value = CustomConfig.DROP_POINTS_ITEM_JUNK;
                break;
            default:
                value = 1;
        }
        return value;
    }

    private int getDropLimitByQuality(ItemQuality quality)
    {
        int value;
        switch (quality) {
            case MYTHIC:
                value = CustomConfig.DROP_LIMIT_ITEM_MYTHIC;
                break;
            case EPIC:
                value = CustomConfig.DROP_LIMIT_ITEM_EPIC;
                break;
            case UNIQUE:
                value = CustomConfig.DROP_LIMIT_ITEM_UNIQUE;
                break;
            case LEGEND:
                value = CustomConfig.DROP_LIMIT_ITEM_LEGEND;
                break;
            case RARE:
                value = CustomConfig.DROP_LIMIT_ITEM_RARE;
                break;
            case COMMON:
                value = CustomConfig.DROP_LIMIT_ITEM_COMMON;
                break;
            case JUNK:
                value = CustomConfig.DROP_LIMIT_ITEM_JUNK;
                break;
            default:
                value = 1;
        }
        return value;
    }


    /**
     * After NPC dies - it can register arbitrary drop
     *
     * @param npc
     * @param player
     * @param lvl
     * @param players List of all the group members in range.
     */
    public void registerDrop(Npc npc, Player player, int lvl, List<Player> players) {
        int npcUniqueId = npc.getObjectId();
        int npcTemplateId = npc.getObjectTemplate().getTemplateId();

        Set<DropItem> droppedItems = new HashSet<DropItem>();
        Set<DropTemplate> templates = dropList.getDropsFor(npcTemplateId);

        int normalDropPercentage = 100;
        int craftItemDropPercentage = 100;
        if (!CustomConfig.DISABLE_DROP_REDUCTION) {
            normalDropPercentage = DropRewardEnum.dropRewardFrom(npc.getLevel() - lvl);
            // craft items will keep dropping if the player is killing low level mobs:
            craftItemDropPercentage = 100 - ((100 - normalDropPercentage) / 2);
        }

        float normalDropRate = player.getRates().getDropRate() * normalDropPercentage / 100F;
        float craftItemDropRate = player.getRates().getDropRate() * craftItemDropPercentage / 100F;

        if (templates != null) {
            for (DropTemplate dropTemplate : templates) {
                DropItem dropItem = new DropItem(dropTemplate);
                if (dropTemplate.getItemId() >= 152000000 && dropTemplate.getItemId() < 153000000)
                    dropItem.calculateCount(craftItemDropRate);
                else
                    dropItem.calculateCount(normalDropRate);

                if (dropItem.getCount() > 0) {
                    if (dropTemplate.getItemId() == 182400001) {
                        dropItem.setCount(dropItem.getCount() * player.getRates().getKinahRate());
                    }
                    droppedItems.add(dropItem);
                }
            }
        }


        if (CustomConfig.SCORING_DROP_ENABLE) {
            DropItem                        DropItemType;
            int                             dropPoints                  = getDropPoints(npc);
            Set<DropItem>                   finalDroppedItems           = new HashSet<DropItem>();
            Map<ItemQuality, Set<DropItem>> dropByQuality               = new FastMap<ItemQuality, Set<DropItem>>();

            ItemQuality[] qq = ItemQuality.values();
            for ( int a = 0 ; a < qq.length ; a++)
                dropByQuality.put(qq[a], new HashSet<DropItem>());

            for (DropItem drop : droppedItems) {
                ItemTemplate tpl = ItemService.getItemTemplate(drop.getDropTemplate().getItemId());
                if (tpl == null)
                    continue;
                ItemQuality quality = tpl.getItemQuality();
                if (quality == null)
                    continue;
                dropByQuality.get(quality).add(drop);
            }

            ItemQuality[]    itemQualities   = {ItemQuality.MYTHIC, ItemQuality.EPIC, ItemQuality.UNIQUE, ItemQuality.LEGEND, ItemQuality.RARE, ItemQuality.COMMON, ItemQuality.JUNK};

            for ( int i = 0 ; i < itemQualities.length ; i++ ) {
                List<DropItem> items      = new ArrayList<DropItem>();
                if (!dropByQuality.containsKey(itemQualities[i])) {
                    continue;
                }
                items.addAll(dropByQuality.get(itemQualities[i]));
                int itemLimit       = getDropLimitByQuality(        itemQualities[i]);
                int itemPoints      = getDropItemPointsByQuality(   itemQualities[i]);
                int itemCount       = items.size();

                while ((itemLimit > 0) && (dropPoints >= itemPoints) && (itemCount > 0)) {

                    int tmpKey  = Rnd.get(0, items.size() - 1);

                    finalDroppedItems.add(items.get(tmpKey));

                    dropPoints  = dropPoints - itemPoints;

                    itemLimit--;
                    itemCount--;
                }
            }
            droppedItems = finalDroppedItems;
        }
        QuestService.getQuestDrop(droppedItems, npc, player);

        // Now set correct indexes
        int index = 1;
        for (DropItem drop : droppedItems)
            drop.setIndex(index++);

        currentDropMap.put(npcUniqueId, droppedItems);

        // TODO: Player should not be null
        if (player != null) {
            List<Player> dropPlayers = new ArrayList<Player>();
            if (player.isInAlliance()) {
                // Register drop to all alliance members.
                List<Integer> dropMembers = new ArrayList<Integer>();
                for (Player member : players) {
                    dropMembers.add(member.getObjectId());
                    dropPlayers.add(member);
                }
                dropRegistrationMap.put(npcUniqueId, new DropNpc(dropMembers, npcUniqueId));
                // Fetch players in range
                DropNpc dropNpc = dropRegistrationMap.get(npcUniqueId);
                dropNpc.setInRangePlayers(players);
                dropNpc.setGroupSize(dropNpc.getInRangePlayers().size());
            } else if (player.isInGroup()) {
                dropRegistrationMap.put(npcUniqueId, new DropNpc(GroupService.getInstance().getMembersToRegistrateByRules(player,
                        player.getPlayerGroup(), npc), npcUniqueId));
                //Fetch players in range
                DropNpc dropNpc = dropRegistrationMap.get(npcUniqueId);
                dropNpc.setInRangePlayers(players);
                dropNpc.setGroupSize(dropNpc.getInRangePlayers().size());
                for (Player member : player.getPlayerGroup().getMembers()) {
                    if (dropNpc.containsKey(member.getObjectId()))
                        dropPlayers.add(member);
                }
            } else {
                List<Integer> singlePlayer = new ArrayList<Integer>();
                singlePlayer.add(player.getObjectId());
                dropPlayers.add(player);
                dropRegistrationMap.put(npcUniqueId, new DropNpc(singlePlayer, npcUniqueId));
            }

            for (Player p : dropPlayers) {
                PacketSendUtility.sendPacket(p, new SM_LOOT_STATUS(npcUniqueId, 0));
            }
        }
    }

    /**
     * After NPC respawns - drop should be unregistered //TODO more correct - on despawn
     *
     * @param npc
     */
    public void unregisterDrop(Npc npc) {
        int npcUniqueId = npc.getObjectId();
        currentDropMap.remove(npcUniqueId);
        if (dropRegistrationMap.containsKey(npcUniqueId)) {
            dropRegistrationMap.remove(npcUniqueId);
        }
    }

    /**
     * When player clicks on dead NPC to request drop list
     *
     * @param player
     * @param npcId
     */
    public void requestDropList(Player player, int npcId) {
        if (player == null || !dropRegistrationMap.containsKey(npcId))
            return;

        DropNpc dropNpc = dropRegistrationMap.get(npcId);
        if (!dropNpc.containsKey(player.getObjectId())) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_LOOT_NO_RIGHT());
            return;
        }

        if (dropNpc.isBeingLooted()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_LOOT_FAIL_ONLOOTING());
            return;
        }

        dropNpc.setBeingLooted(player);

        Set<DropItem> dropItems = currentDropMap.get(npcId);

        if (dropItems == null) {
            dropItems = Collections.emptySet();
        }

        PacketSendUtility.sendPacket(player, new SM_LOOT_ITEMLIST(npcId, dropItems, player));
        // PacketSendUtility.sendPacket(player, new SM_LOOT_STATUS(npcId, size > 0 ? size - 1 : size));
        PacketSendUtility.sendPacket(player, new SM_LOOT_STATUS(npcId, 2));
        player.unsetState(CreatureState.ACTIVE);
        player.setState(CreatureState.LOOTING);
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, npcId), true);
    }

    /**
     * This method will change looted corpse to not in use
     *
     * @param player
     * @param npcId
     * @param close
     */
    public void requestDropList(Player player, int npcId, boolean close) {
        if (!dropRegistrationMap.containsKey(npcId))
            return;

        DropNpc dropNpc = dropRegistrationMap.get(npcId);
        dropNpc.setBeingLooted(null);

        player.unsetState(CreatureState.LOOTING);
        player.setState(CreatureState.ACTIVE);
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_LOOT, 0, npcId), true);

        Set<DropItem> dropItems = currentDropMap.get(npcId);
        AionObject obj = World.getInstance().findAionObject(npcId);
        if (obj instanceof Npc) {
            Npc npc = (Npc) obj;
            if (npc != null) {
                if (dropItems == null || dropItems.size() == 0) {
                    npc.getController().onDespawn(true);
                    return;
                }

                PacketSendUtility.broadcastPacket(npc, new SM_LOOT_STATUS(npcId, 0));
                dropNpc.setFreeLooting();
            }
        }
    }

    /**
     * Request an item from a killed mob.
     *
     * @param player    The player that loots the mob.
     * @param npcId     The mob that gets looted.
     * @param itemIndex The index of the looted item.
     */
    public void requestDropItem(final Player player, int npcId, int itemIndex) {
        final Set<DropItem> dropItems = currentDropMap.get(npcId);
        final DropNpc dropNpc = dropRegistrationMap.get(npcId);

        // drop was unregistered
        if (dropItems == null || dropNpc == null) {
            return;
        }

        // TODO prevent possible exploits

        DropItem requestedItem = null;

        synchronized (dropItems) {
            for (DropItem dropItem : dropItems) {
                if (dropItem.getIndex() == itemIndex) {
                    requestedItem = dropItem;
                    break;
                }
            }
        }

        if (requestedItem == null || requestedItem.isProcessed())
            return;

        ItemTemplate itemTemplate = ItemService.getItemTemplate(requestedItem.getDropTemplate().getItemId());
        if (itemTemplate == null) {
            log.warn("Item id " + requestedItem.getDropTemplate().getItemId()
                    + " can't be found in the item template.");
            return;
        }

        if (!itemTemplate.isTradeable() && !requestedItem.isQuestDropForEachMemeber() && (player.isInGroup() || player.isInAlliance())) {
            final DropItem lootItem = requestedItem;
            RequestResponseHandler rrh = new RequestResponseHandler(player) {
                @Override
                public void acceptRequest(Creature requester, Player responder) {
                    continueRequestDropItem(player, dropItems, dropNpc, lootItem);
                }

                @Override
                public void denyRequest(Creature requester, Player responder) {
                    // do nothing
                }
            };
            player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_CONFIRM_LOOT, rrh);
            PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_CONFIRM_LOOT, 0,
                    new DescriptionId(itemTemplate.getNameId())));
        } else
            continueRequestDropItem(player, dropItems, dropNpc, requestedItem);
    }

    private void continueRequestDropItem(final Player player, Set<DropItem> dropItems, DropNpc dropNpc, final DropItem requestedItem) {
        if (requestedItem == null || requestedItem.isProcessed())
            return;

        if (CustomConfig.ANNOUNCE_RAREDROPS && !player.getInventory().isFull()) {
            ItemTemplate itemTemplate = ItemService.getItemTemplate(requestedItem.getDropTemplate().getItemId());
            if (itemTemplate.getItemQuality() == ItemQuality.UNIQUE || itemTemplate.getItemQuality() == ItemQuality.EPIC) {
                final int pRaceId = player.getCommonData().getRace().getRaceId();
                final int pMap = player.getWorldId();
                final int pRegion = player.getActiveRegion().getRegionId();
                final int pInstance = player.getInstanceId();

                World.getInstance().doOnAllPlayers(new Executor<Player>() {
                    @Override
                    public boolean run(Player other) {
                        if (other.getObjectId() == player.getObjectId() || !other.isSpawned()) {
                            return true;
                        }

                        int oRaceId = other.getCommonData().getRace().getRaceId();
                        int oMap = other.getWorldId();
                        int oRegion = other.getActiveRegion().getRegionId();
                        int oInstance = other.getInstanceId();

                        if (oRaceId == pRaceId && oMap == pMap && oRegion == pRegion && oInstance == pInstance) {
                            // TODO send a message id instead of a string
                            PacketSendUtility.sendMessage(other, player.getCommonData().getName() + " has acquired "
                                    + "[item:" + requestedItem.getDropTemplate().getItemId() + ";ver1;;]");
                        }
                        return true;
                    }
                });
            }
        }

        if (requestedItem != null) {
            if (requestedItem.isItemWonNotCollected() && player != requestedItem.getWinningPlayer()) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LOOT_ANOTHER_OWNER_ITEM());
                return;
            }

            long currentDropItemCount = requestedItem.getCount();
            int itemId = requestedItem.getDropTemplate().getItemId();

            ItemTemplate itemTemplate = ItemService.getItemTemplate(itemId);
            ItemQuality quality = ItemQuality.COMMON;
            if (itemTemplate == null)
                log.warn("Item id " + itemId + " can't be found in the item template.");
            else
                quality = itemTemplate.getItemQuality();

            if (!requestedItem.isItemWonNotCollected() && !requestedItem.isFreeForAll()) {
                if (player.isInGroup() || player.isInAlliance()) {
                    if (player.isInGroup())
                        requestedItem.setDistributionType(player.getPlayerGroup().getLootGroupRules().getQualityRule(quality));
                    else // TODO alliances don't have loot rules yet.
                        requestedItem.setDistributionType(0);

                    if (requestedItem.getDistributionType() > 1) {
                        int groupAllianceId = 0;
                        if (player.isInGroup()) {
                            groupAllianceId = player.getPlayerGroup().getObjectId();
                        } else {
                            groupAllianceId = player.getPlayerAlliance().getObjectId();
                        }

                        addSpecialItem(groupAllianceId, dropNpc, requestedItem);
                    }
                }
            }

            //If looting player not in Group/Alliance or distribution is set to NORMAL
            //or all party members have passed, making item FFA....
            if ((!player.isInGroup() && !player.isInAlliance()) || requestedItem.getDistributionType() == 0
                    || requestedItem.isFreeForAll()
                    || (requestedItem.isItemWonNotCollected() && player == requestedItem.getWinningPlayer())) {
                currentDropItemCount = ItemService.addItem(player, itemId, currentDropItemCount);
            }

            if (currentDropItemCount == 0) {
                requestedItem.setProcessed();
                dropItems.remove(requestedItem);
            } else {
                // If player didn't got all item stack
                requestedItem.setCount(currentDropItemCount);
            }

            // show updated drop list
            resendDropList(dropNpc.getBeingLooted(), dropNpc.getNpcId(), dropItems);
        }
    }

    private void resendDropList(Player player, int npcId, Set<DropItem> dropItems) {
        if (player != null) {
            player.unsetState(CreatureState.LOOTING);
            player.setState(CreatureState.ACTIVE);
        }
        if (dropItems.size() != 0) {
            if (player != null) {
                boolean hasItemsForPlayer = false;
                for (DropItem item : dropItems) {
                    if (item.hasQuestPlayerObjId(player.getObjectId())) {
                        hasItemsForPlayer = true;
                        break;
                    }
                }
                if (hasItemsForPlayer) {
                    PacketSendUtility.sendPacket(player, new SM_LOOT_ITEMLIST(npcId, dropItems, player));
                } else {
                    PacketSendUtility.sendPacket(player, new SM_LOOT_STATUS(npcId, 3));
                    PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_LOOT, 0, npcId), true);
                }
            }
        } else {
            if (player != null) {
                PacketSendUtility.sendPacket(player, new SM_LOOT_STATUS(npcId, 3));
                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_LOOT, 0, npcId), true);
            }
            AionObject obj = World.getInstance().findAionObject(npcId);
            if (obj instanceof Npc) {
                Npc npc = (Npc) obj;
                if (npc != null) {
                    npc.getController().onDespawn(true);
                }
            }
        }
    }

    /**
     * Add an item that should be rolled/bid on.
     *
     * @param groupAllianceId The id of the group or alliance.
     * @param dropNpc
     * @param specialItem
     */
    private void addSpecialItem(int groupAllianceId, DropNpc dropNpc, DropItem specialItem) {
        if (!dropNpc.addSpecialItem(specialItem))
            return;

        specialDropLock.lock();
        try {
            DropNpc currentSpecialNpc = specialDropMap.get(groupAllianceId);
            if (currentSpecialNpc == null) {
                specialDropMap.put(groupAllianceId, dropNpc);
                sendBidRollPackets(groupAllianceId);
            } else {
                try {
                    currentSpecialNpc.addSpecialDropNpc(dropNpc);
                }
                catch (StackOverflowError soe) {
                    // This does NOT fix any errors, it just wraps up the StackOverflowError error so it doesn't take
                    // 1000 lines in the error log.
                    specialDropMap.remove(groupAllianceId);
                    throw new Error("StackOverflowError");
                }
            }
        }
        finally {
            specialDropLock.unlock();
        }
    }

    /**
     * Sends the packets to roll/bid on an item.
     *
     * @param groupAllianceId
     */
    private void sendBidRollPackets(final int groupAllianceId) {
        // FIXME find the players in range when the rolling/bidding starts.
        // Store the npc location in DropNpc.
        final DropNpc dropNpc = getNextSpecialNpc(groupAllianceId);
        if (dropNpc == null)
            return;

        final DropItem requestedItem = dropNpc.getNextSpecialItem();
        if (requestedItem == null) {
            // For an unknown reason requestedItem can be null.
            // When this happens assume that the current NPC has no more special items and restart the method to check
            // for other NPC's
            dropNpc.resetSpecialItems();
            specialDropMap.remove(groupAllianceId);
            return;
        }

        int itemId = requestedItem.getDropTemplate().getItemId();

        // Start the timeout task and let it wait 20 seconds of it's rolling or 35 seconds if it's bidding.
        int timeout = 20000;
        if (requestedItem.getDistributionType() == 3)
            timeout = 35000;
        ScheduledFuture<?> future = ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                specialLootTimeout(groupAllianceId, dropNpc, requestedItem);
            }
        }, timeout);
        requestedItem.setSpecialDropTimeout(future);

        // Send the packet to all members
        for (Player member : dropNpc.getInRangePlayers()) {
            if (member.isOnline()) {
                requestedItem.addSpecialPlayer(member);
                PacketSendUtility.sendPacket(member, new SM_GROUP_LOOT(groupAllianceId, itemId,
                        requestedItem.getIndex(), dropNpc.getNpcId(), requestedItem.getDistributionType()));
            }
        }
    }

    /**
     * Cancel the current special item loot.
     *
     * @param groupAllianceId
     */
    private void specialLootTimeout(int groupAllianceId, DropNpc dropNpc, DropItem requestedItem) {
        requestedItem.setSpecialDropTimeout(null);

        distributeSpecialItem(requestedItem, groupAllianceId, dropNpc.getNpcId(), requestedItem.getIndex());
    }

    /**
     * @param groupAllianceId The id of the group or alliance.
     * @return The DropNpc that has special items to roll/bid on, or null if there is no next Npc.
     */
    private DropNpc getNextSpecialNpc(int groupAllianceId) {
        specialDropLock.lock();
        try {
            DropNpc currentSpecialNpc = specialDropMap.get(groupAllianceId);
            if (currentSpecialNpc == null)
                return null;

            if (!currentSpecialNpc.hasSpecialItems()) { // changed the special npc to the next npc with roll/bid items or null.
                currentSpecialNpc = currentSpecialNpc.getNextSpecialDropNpc();
                specialDropMap.put(groupAllianceId, currentSpecialNpc);
            }

            if (currentSpecialNpc == null) {
                specialDropMap.remove(groupAllianceId);
            }
            return currentSpecialNpc;
        }
        finally {
            specialDropLock.unlock();
        }
    }

    /**
     * Called from CM_GROUP_LOOT to handle rolls
     *
     * @param player
     * @param groupAllianceId
     * @param roll
     * @param itemId
     * @param itemIndex
     * @param npcId
     */
    public void handleRoll(Player player, int groupAllianceId, int roll, int itemId, int itemIndex, int npcId) {
        if (dropRegistrationMap.get(npcId) == null)
            return;

        switch (roll) {
            case 0:
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_GIVEUP_ME());
                if (player.isInGroup() || player.isInAlliance()) {
                    for (Player member : dropRegistrationMap.get(npcId).getInRangePlayers()) {
                        if (!player.equals(member))
                            PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_MSG_DICE_GIVEUP_OTHER(player.getName()));
                    }
                }
                handleSpecialLoot(player, groupAllianceId, 0, itemId, itemIndex, npcId);
                break;
            case 1:
                int luck = Rnd.get(1, 100);
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_RESULT_ME(luck));
                if (player.isInGroup() || player.isInAlliance()) {
                    for (Player member : dropRegistrationMap.get(npcId).getInRangePlayers()) {
                        if (!player.equals(member))
                            PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_MSG_DICE_RESULT_OTHER(player.getName(), luck));
                    }
                }
                handleSpecialLoot(player, groupAllianceId, luck, itemId, itemIndex, npcId);
                break;
        }
    }

    /**
     * Called from CM_GROUP_LOOT to handle bids
     *
     * @param player
     * @param groupAllianceId
     * @param bid
     * @param itemId
     * @param itemIndex
     * @param npcId
     */
    public void handleBid(Player player, int groupAllianceId, long bid, int itemId, int itemIndex, int npcId) {
        long kinahAmount = player.getInventory().getKinahItem().getItemCount();
        if (bid > 0) {
            if (kinahAmount < bid) {
                bid = 0;// Set BID to 0 if player has bid more KINAH then they have in inventory
            }
            handleSpecialLoot(player, groupAllianceId, bid, itemId, itemIndex, npcId);
        } else
            handleSpecialLoot(player, groupAllianceId, 0, itemId, itemIndex, npcId);
    }

    /**
     * @param Checks all players have Rolled or Bid then Distributes items accordingly
     */
    private void handleSpecialLoot(Player player, int groupAllianceId, long bidRollValue, int itemId, int itemIndex, int npcId) {
        DropNpc dropNpc = dropRegistrationMap.get(npcId);

        Set<DropItem> dropItems = currentDropMap.get(npcId);
        if (dropNpc == null || dropItems == null)
            return;

        DropItem requestedItem = null;

        synchronized (dropItems) {
            for (DropItem dropItem : dropItems) {
                if (dropItem.getIndex() == itemIndex) {
                    requestedItem = dropItem;
                    break;
                }
            }
        }
        if (requestedItem == null || requestedItem.getDropTemplate().getItemId() != itemId
                || requestedItem.isProcessed())
            return;

        //Removes player from ARRAY once they have rolled or bid
        if (requestedItem.containsSpecialPlayer(player)) {
            requestedItem.delSpecialPlayer(player);
        } else
            return;

        if (bidRollValue > requestedItem.getHighestValue()) {
            requestedItem.setHighestValue(bidRollValue);
            requestedItem.setWinningPlayer(player);
        }

        if (requestedItem.getSpecialPlayerSize() != 0)
            return;

        // Cancel the timeout task
        requestedItem.cancelTimeoutTask();

        distributeSpecialItem(requestedItem, groupAllianceId, npcId, itemIndex);
    }

    private void distributeSpecialItem(DropItem requestedItem, int groupAllianceId, int npcId, int itemIndex) {
        DropNpc dropNpc = dropRegistrationMap.get(npcId);
        if (dropNpc == null)
            return;

        //Check if there is a Winning Player registered if not all members must have passed...
        if (requestedItem.getWinningPlayer() == null) {
            requestedItem.setFreeForAll(true);
        } else {
            Player player = requestedItem.getWinningPlayer();
            long currentDropItemCount = requestedItem.getCount();
            int itemId = requestedItem.getDropTemplate().getItemId();

            switch (requestedItem.getDistributionType()) {
                case 2:
                    winningRollActions(player, itemId, npcId);
                    break;
                case 3:
                    winningBidActions(player, itemId, npcId, requestedItem.getHighestValue());
                    break;
            }

            // handles distribution of item to correct player and messages accordingly
            if (player.getInventory().isFull()) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_INVEN_ERROR);
                requestedItem.setItemWonNotCollected(true);
            } else {
                Set<DropItem> dropItems = currentDropMap.get(npcId);
                currentDropItemCount = ItemService.addItem(player, itemId, currentDropItemCount);

                if (currentDropItemCount != 0) {
                    requestedItem.setCount(currentDropItemCount);
                    requestedItem.setItemWonNotCollected(true);
                } else {
                    requestedItem.setProcessed();
                    dropItems.remove(requestedItem);
                }

                // show updated drop list
                resendDropList(dropNpc.getBeingLooted(), npcId, dropItems);
            }
        }

        sendBidRollPackets(groupAllianceId);
    }

    /**
     * @param Displays messages when item gained via ROLLED
     */
    private void winningRollActions(Player player, int itemId, int npcId) {
        DescriptionId itemNameId = new DescriptionId(ItemService.getItemTemplate(itemId).getNameId());
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LOOT_GET_ITEM_ME(itemNameId));

        if (player.isInGroup() || player.isInAlliance()) {
            for (Player member : dropRegistrationMap.get(npcId).getInRangePlayers()) {
                if (!player.equals(member))
                    PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_MSG_LOOT_GET_ITEM_OTHER(player.getName(),
                            itemNameId));
            }
        }
    }

    /**
     * @param Displays messages/removes and shares kinah when item gained via BID
     */
    private void winningBidActions(Player player, int itemId, int npcId, long highestValue) {
        DropNpc dropNpc = dropRegistrationMap.get(npcId);

        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_PAY_ACCOUNT_ME(highestValue));
        player.getInventory().decreaseKinah(highestValue);

        if (player.isInGroup() || player.isInAlliance()) {
            long distributeKinah = highestValue / (dropNpc.getGroupSize() - 1);
            for (Player member : dropNpc.getInRangePlayers()) {
                if (!player.equals(member)) {
                    PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_MSG_PAY_ACCOUNT_OTHER(player.getName(), highestValue));
                    member.getInventory().increaseKinah(distributeKinah);
                    PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_MSG_PAY_DISTRIBUTE(highestValue, dropNpc.getGroupSize() - 1, distributeKinah));
                }
            }
        }
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder
    {
        protected static final DropService instance = new DropService();
    }
}