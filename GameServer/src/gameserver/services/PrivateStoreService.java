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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PrivateStore;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.items.GodStone;
import gameserver.model.items.ManaStone;
import gameserver.model.trade.TradeItem;
import gameserver.model.trade.TradeList;
import gameserver.model.trade.TradePSItem;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE;
import gameserver.network.aion.serverpackets.SM_PRIVATE_STORE_NAME;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Set;

/**
 * @author Simple, ZeroSignal
 */
public class PrivateStoreService {
    private static Logger log = Logger.getLogger(PrivateStoreService.class);
    /**
     * @param activePlayer
     * @param tradePSItems
     */
    public static void addItem(Player activePlayer, TradePSItem[] tradePSItems) {
        /**
         * Check if player try to get super speed exploit
         */
        if (CreatureState.ACTIVE.getId() != activePlayer.getState())
            return;
        /**
         * Check if player already has a store, if not create one
         */
        if (activePlayer.getStore() == null)
            createStore(activePlayer);

        /**
         * Define store to make things easier
         */
        PrivateStore store = activePlayer.getStore();

        /**
         * Check if player owns itemObjId else don't add item
         */
        for (int i = 0; i < tradePSItems.length; i++) {
            Item item = activePlayer.getInventory().getItemByObjId( tradePSItems[i].getItemObjId());
            if (item != null && item.getItemTemplate().isTradeable()) {
                if (!validateItem(item, tradePSItems[i].getItemId(), tradePSItems[i].getCount()))
                    return;
                /**
                 * Add item to private store
                 */
                store.addItemToSell(tradePSItems[i]);
            }
        }
    }

    /**
     * A check isn't really needed.....
     *
     * @return
     */
    private static boolean validateItem(Item item, int itemId, long itemAmount) {
        return !(item.getItemTemplate().getTemplateId() != itemId || itemAmount > item.getItemCount());
    }

    /**
     * This method will create the player's store
     *
     * @param activePlayer
     */
    private static void createStore(Player activePlayer) {
        activePlayer.setStore(new PrivateStore(activePlayer));
        activePlayer.setState(CreatureState.PRIVATE_SHOP);
        PacketSendUtility.broadcastPacket(activePlayer, new SM_EMOTION(activePlayer, EmotionType.OPEN_PRIVATESHOP, 0, 0), true);

        /**
         * Check if player try to get super speed exploit
         */
        if (CreatureState.PRIVATE_SHOP.getId() != activePlayer.getState())
            return;
    }

    /**
     * This method will destroy the player's store
     *
     * @param activePlayer
     */
    public static void closePrivateStore(Player activePlayer) {
        activePlayer.setStore(null);
        activePlayer.unsetState(CreatureState.PRIVATE_SHOP);
        PacketSendUtility.broadcastPacket(activePlayer, new SM_EMOTION(activePlayer, EmotionType.CLOSE_PRIVATESHOP, 0, 0), true);
    }

    /**
     * This method will move the item to the new player and move kinah to item owner
     *
     * @param seller
     * @param buyer
     * @param tradeList
     * @return tradeList thats updated.
     */
    public static TradeList sellStoreItem(Player seller, Player buyer, TradeList tradeList) {
        /**
         * 1. Check if we are busy with two valid participants
         */
        if (!validateParticipants(seller, buyer)) {
            log.warn("PrivateStoreService.sellStoreItem : invalid Participants." +
                " sellerId: " + seller.getObjectId() +
                ", buyerId: " + buyer.getObjectId());
            return null;
        }

        /**
         * Define store to make life easier
         */
        PrivateStore store = seller.getStore();

        /**
         * 2. Load all item object id's and validate if seller really owns them
         */
        tradeList = loadObjIds(seller, tradeList);
        if (tradeList == null) {
            log.warn("PrivateStoreService.sellStoreItem: loadObjIds tradeList returned null. " +
                "sellerId: " + seller.getObjectId());
            return null; // Invalid items found or store was empty
        }

        /**
         * 3. Check free slots
         */
        Storage inventory = buyer.getInventory();
        int freeSlots = inventory.getLimit() - (inventory.getAllItems().size() - 1);
        if (freeSlots < tradeList.size()) {
            PacketSendUtility.sendMessage(buyer, LanguageHandler.translate(CustomMessageId.PLAYER_INVENTORY_FULL));
            log.warn("PrivateStoreService.sellStoreItem : not enough free slots. " +
                "freeSlots: " + freeSlots +
                ", tradeList.size(): " + tradeList.size());
            return null; // TODO message
        }

        /**
         * Create total price and items
         */
        long price = getTotalPrice(store, tradeList);

        /**
         * Check if player has enough kinah and remove it
         */        
        if (buyer.getKinah() < price) {
            log.warn("PrivateStoreService.sellStoreItem : buyer not enough kinah. " +
                "buyerId: " + buyer.getObjectId() +
                ", kinah: " + buyer.getKinah() +
                ", price: " + price);
            return null;
        }

        /**
         * Decrease kinah for buyer and Increase kinah for seller
         */
        buyer.removeKinah(price);
        seller.addKinah(price);

        List<Item> newItems = new ArrayList<Item>();
        List<Integer> removeSlots = new ArrayList<Integer>();

        for (TradeItem tradeItem : tradeList.getTradeItems()) {
            Item item = seller.getInventory().getItemByObjId( tradeItem.getItemId());
            if (item == null)
                continue;

            TradePSItem storeItem = store.getTradeItemBySlot(tradeItem.getItemSlot());

            Set<ManaStone> manaStones = null;
            if (item.hasManaStones())
                manaStones = item.getItemStones();

            GodStone godStone = item.getGodStone();

            if (storeItem.getCount() == tradeItem.getCount())
                removeSlots.add(tradeItem.getItemSlot());

            decreaseItemFromPlayer(seller, item, tradeItem);
            ItemService.addFullItem(buyer, item.getItemTemplate().getTemplateId(),
                tradeItem.getCount(), item.getItemCreator(), manaStones, godStone,
                item.getEnchantLevel());
            tradeItem.setItemId(storeItem.getItemId());
        }

        /** 
         * Remove the store Items that have been sold in reverse order to avoid memory errors.
         */
        if (!removeSlots.isEmpty()) {
            Collections.sort(removeSlots, Collections.reverseOrder());
            for (int slot : removeSlots) {
                store.removeItem(slot);
            }
        }

        /**
         * Remove item from store and check if last item
         */
        if (store.getSoldItems().isEmpty())
            closePrivateStore(seller);

        return tradeList;
    }

    /**
     * Decrease item count and update inventory
     *
     * @param seller
     * @param item
     */
    private static void decreaseItemFromPlayer(Player seller, Item item, TradeItem tradeItem) {
        seller.getInventory().decreaseItemCount(item, tradeItem.getCount());
        PacketSendUtility.sendPacket(seller, new SM_UPDATE_ITEM(item));
        seller.getStore().getTradeItemBySlot(tradeItem.getItemSlot()).decreaseCount(tradeItem.getCount());
    }

    /**
     * @param seller
     * @param tradeList
     * @return
     */
    private static TradeList loadObjIds(Player seller, TradeList tradeList) {
        PrivateStore store = seller.getStore();
        TradeList newTradeList = new TradeList();

        for (TradeItem tradeItem : tradeList.getTradeItems()) {
            TradePSItem psItem = store.getTradeItemBySlot(tradeItem.getItemId());
            if (psItem == null)
                continue;
            if (!newTradeList.addSellPSItem(tradeItem, psItem))
            {
                log.error("TradeList.addSellPSItem failed. " + psItem.toString() + tradeItem.toString());
            }
        }

        /**
         * Check if player still owns items
         */
        if (!validateBuyItems(seller, newTradeList))
            return null;

        return newTradeList;
    }

    /**
     * @param player1
     * @param player2
     */
    private static boolean validateParticipants(Player itemOwner, Player newOwner) {
        return itemOwner != null && newOwner != null && itemOwner.isOnline() && newOwner.isOnline();
    }

    /**
     * @param tradeList
     */
    private static boolean validateBuyItems(Player seller, TradeList tradeList) {
        for (TradeItem tradeItem : tradeList.getTradeItems()) {
            Item item = seller.getInventory().getItemByObjId(tradeItem.getItemId());
            if (item == null)
                return false;

            // check amount of item, if amount to buy is not higher than amount available
            if (!validateItem(item, item.getItemId(), tradeItem.getCount()))
                return false;
        }
        return true;
    }

    /**
     * This method will return the total price of the tradelist
     *
     * @param store
     * @param tradeList
     * @return
     */
    private static long getTotalPrice(PrivateStore store, TradeList tradeList) {
        long totalprice = 0;
        for (TradeItem tradeItem : tradeList.getTradeItems()) {
            TradePSItem item = store.getTradeItemBySlot(tradeItem.getItemSlot());
            totalprice += item.getPrice() * tradeItem.getCount();
        }
        return totalprice;
    }

    /**
     * @param activePlayer
     */
    public static void openPrivateStore(Player activePlayer, String name) {
        if (name != null) {
            activePlayer.getStore().setStoreMessage(name);
            PacketSendUtility.broadcastPacket(activePlayer,
                    new SM_PRIVATE_STORE_NAME(activePlayer.getObjectId(), name), true);
        } else {
            PacketSendUtility.broadcastPacket(activePlayer, new SM_PRIVATE_STORE_NAME(activePlayer.getObjectId(), ""),
                true);
        }
    }
}
