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
import gameserver.dao.InventoryDAO;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.trade.Exchange;
import gameserver.model.trade.ExchangeItem;
import gameserver.network.aion.serverpackets.*;
import gameserver.restrictions.RestrictionsManager;
import gameserver.taskmanager.AbstractFIFOPeriodicTaskManager;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ATracer
 */
public class ExchangeService {
    private static Logger log = Logger.getLogger(ExchangeService.class);

    private Map<Integer, Exchange> exchanges = new HashMap<Integer, Exchange>();

    private ExchangePeriodicTaskManager saveManager;

    private final int DELAY_EXCHANGE_SAVE = 5000;

    public static final ExchangeService getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Default constructor
     */
    private ExchangeService() {
        saveManager = new ExchangePeriodicTaskManager(DELAY_EXCHANGE_SAVE);
    }

    /**
     * @param objectId
     * @param objectId2
     */
    public void registerExchange(Player player1, Player player2) {
        if (!validateParticipants(player1, player2))
            return;

        player1.setTrading(true);
        player2.setTrading(true);

        exchanges.put(player1.getObjectId(), new Exchange(player1, player2));
        exchanges.put(player2.getObjectId(), new Exchange(player2, player1));

        PacketSendUtility.sendPacket(player2, new SM_EXCHANGE_REQUEST(player1.getName()));
        PacketSendUtility.sendPacket(player1, new SM_EXCHANGE_REQUEST(player2.getName()));
    }

    /**
     * @param player1
     * @param player2
     */
    private boolean validateParticipants(Player player1, Player player2) {
        return RestrictionsManager.canTrade(player1) && RestrictionsManager.canTrade(player2);
    }

    private Player getCurrentParter(Player player) {
        Exchange exchange = exchanges.get(player.getObjectId());
        return exchange != null ? exchange.getTargetPlayer() : null;
    }

    /**
     * @param player
     * @return Exchange
     */
    private Exchange getCurrentExchange(Player player) {
        return exchanges.get(player.getObjectId());
    }

    /**
     * @param player
     * @return Exchange
     */
    public Exchange getCurrentParnterExchange(Player player) {
        Player partner = getCurrentParter(player);
        return partner != null ? getCurrentExchange(partner) : null;
    }

    /**
     * @param activePlayer
     * @param itemCount
     */
    public void addKinah(Player activePlayer, long itemCount) {
        Exchange currentExchange = getCurrentExchange(activePlayer);
        if (currentExchange.isLocked())
            return;

        if (itemCount < 1)
            return;

        //count total amount in inventory
        long availableCount = activePlayer.getInventory().getKinahItem().getItemCount();

        //count amount that was already added to exchange
        availableCount -= currentExchange.getKinahCount();

        long countToAdd = availableCount > itemCount ? itemCount : availableCount;

        if (countToAdd > 0) {
            Player partner = getCurrentParter(activePlayer);
            PacketSendUtility.sendPacket(activePlayer, new SM_EXCHANGE_ADD_KINAH(countToAdd, 0));
            PacketSendUtility.sendPacket(partner, new SM_EXCHANGE_ADD_KINAH(countToAdd, 1));
            currentExchange.addKinah(countToAdd);
        }
    }

    /**
     * @param activePlayer
     * @param itemObjId
     * @param itemCount
     */
    public void addItem(Player activePlayer, int itemObjId, long itemCount) {
        Item item = activePlayer.getInventory().getItemByObjId(itemObjId);
        if (item == null)
            return;

        // Check Trade Hack
        if (!item.getItemTemplate().isTradeable())
            return;

        if (itemCount < 1)
            return;

        if (itemCount > item.getItemCount())
            return;

        Player partner = getCurrentParter(activePlayer);
        Exchange currentExchange = getCurrentExchange(activePlayer);

        if (currentExchange == null)
            return;

        if (currentExchange.isLocked())
            return;

        if (currentExchange.isExchangeListFull())
            return;

        ExchangeItem exchangeItem = currentExchange.getItems().get(item.getObjectId());

        long actuallAddCount = 0;
        //item was not added previosly
        if (exchangeItem == null) {
            Item newItem = null;
            if (itemCount < item.getItemCount()) {
                newItem = ItemService.newItem(item.getItemId(), itemCount,
                    item.getItemCreator());
            } else {
                newItem = item;
            }
            exchangeItem = new ExchangeItem(itemObjId, itemCount, newItem, item.getItemCount());
            currentExchange.addItem(itemObjId, exchangeItem);
            actuallAddCount = itemCount;
        }
        //item was already added
        else {
            //if player add item count that is more than possible
            //happens with exploits
            if (item.getItemCount() == exchangeItem.getItemCount())
                return;

            long possibleToAdd = item.getItemCount() - exchangeItem.getItemCount();
            actuallAddCount = itemCount > possibleToAdd ? possibleToAdd : itemCount;
            exchangeItem.addCount(actuallAddCount);
        }

        PacketSendUtility.sendPacket(activePlayer, new SM_EXCHANGE_ADD_ITEM(0, exchangeItem.getItem()));
        PacketSendUtility.sendPacket(partner, new SM_EXCHANGE_ADD_ITEM(1, exchangeItem.getItem()));
    }

    /**
     * @param activePlayer
     */
    public void lockExchange(Player activePlayer) {
        Exchange exchange = getCurrentExchange(activePlayer);
        if (exchange != null) {
            exchange.lock();
            Player currentParter = getCurrentParter(activePlayer);
            PacketSendUtility.sendPacket(currentParter, new SM_EXCHANGE_CONFIRMATION(3));
        }
    }

    /**
     * @param activePlayer
     */
    public void cancelExchange(Player activePlayer) {
        Player currentParter = getCurrentParter(activePlayer);
        cleanupExchanges(activePlayer, currentParter);
        if (currentParter != null)
            PacketSendUtility.sendPacket(currentParter, new SM_EXCHANGE_CONFIRMATION(1));
    }

    /**
     * @param activePlayer
     */
    public void confirmExchange(Player activePlayer) {
        Exchange currentExchange = getCurrentExchange(activePlayer);

        // Why is exchange null ?
        if (currentExchange == null)
            return;

        currentExchange.confirm();

        Player currentPartner = getCurrentParter(activePlayer);
        PacketSendUtility.sendPacket(currentPartner, new SM_EXCHANGE_CONFIRMATION(2));

        if (getCurrentExchange(currentPartner).isConfirmed()) {
            performTrade(activePlayer, currentPartner);
        }
    }

    /**
     * @param activePlayer
     * @param currentPartner
     */
    private void performTrade(Player activePlayer, Player currentPartner) {
        if (!validateExchange(activePlayer, currentPartner)) {
            cancelExchange(activePlayer);
            PacketSendUtility.sendPacket(activePlayer, new SM_EXCHANGE_CONFIRMATION(1));
            PacketSendUtility.sendMessage(activePlayer, LanguageHandler.translate(CustomMessageId.ERROR_INVALID_EXCHANGE));
            PacketSendUtility.sendMessage(currentPartner, LanguageHandler.translate(CustomMessageId.ERROR_INVALID_EXCHANGE));
            return;
        }

        PacketSendUtility.sendPacket(activePlayer, new SM_EXCHANGE_CONFIRMATION(0));
        PacketSendUtility.sendPacket(currentPartner, new SM_EXCHANGE_CONFIRMATION(0));

        Exchange exchange1 = getCurrentExchange(activePlayer);
        Exchange exchange2 = getCurrentExchange(currentPartner);

        cleanupExchanges(activePlayer, currentPartner);

        removeItemsFromInventory(activePlayer, exchange1);
        removeItemsFromInventory(currentPartner, exchange2);

        putItemToInventory(currentPartner, exchange1, exchange2);
        putItemToInventory(activePlayer, exchange2, exchange1);

        saveManager.add(new ExchangeOpSaveTask(exchange1.getActiveplayer().getObjectId(), exchange2.getActiveplayer()
                .getObjectId(), exchange1.getItemsToUpdate(), exchange2.getItemsToUpdate()));
    }

    /**
     * @param activePlayer
     * @param currentPartner
     */
    private void cleanupExchanges(Player activePlayer, Player currentPartner) {
        if (activePlayer != null) {
            exchanges.remove(activePlayer.getObjectId());
            activePlayer.setTrading(false);
        }

        if (currentPartner != null) {
            exchanges.remove(currentPartner.getObjectId());
            currentPartner.setTrading(false);
        }
    }

    /**
     * @param player
     * @param exchange
     */
    private void removeItemsFromInventory(Player player, Exchange exchange) {
        Storage inventory = player.getInventory();

        for (ExchangeItem exchangeItem : exchange.getItems().values()) {
            Item item = exchangeItem.getItem();
            Item itemInInventory = inventory.getItemByObjId(exchangeItem.getItemObjId());

            if (item == null || itemInInventory == null)
                return;

            long itemCount = exchangeItem.getItemCount();

            if (itemCount < itemInInventory.getItemCount()) {
                inventory.decreaseItemCount(itemInInventory, itemCount);
                exchange.addItemToUpdate(itemInInventory);
            } else {
                inventory.removeFromBag(itemInInventory, false);
                exchangeItem.setItem(itemInInventory);
                PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(itemInInventory.getObjectId()));
            }
        }
        player.getInventory().decreaseKinah(exchange.getKinahCount());
        exchange.addItemToUpdate(player.getInventory().getKinahItem());
    }

    /**
     * @param activePlayer
     * @param currentPartner
     * @return
     */
    private boolean validateExchange(Player activePlayer, Player currentPartner) {
        Exchange exchange1 = getCurrentExchange(activePlayer);
        Exchange exchange2 = getCurrentExchange(currentPartner);

        // Validate correct amount of Kinah still present.
        if (activePlayer.getInventory().getKinahCount() < exchange1.getKinahCount())
            return false;
        if (currentPartner.getInventory().getKinahCount() < exchange2.getKinahCount())
            return false;

        // Validate all Items still present and have correct Amounts.
        for (ExchangeItem exchangeItem : exchange1.getItems().values()) {
            Item item = exchangeItem.getItem();
            Item itemInInventory = activePlayer.getInventory().getItemByObjId(exchangeItem.getItemObjId());

            if (item == null || itemInInventory == null ||
                itemInInventory.getItemCount() < exchangeItem.getItemCount() ||
                exchangeItem.getOriginalItemCount() != itemInInventory.getItemCount())
            {
                return false;
            }
        }
        for (ExchangeItem exchangeItem : exchange2.getItems().values()) {
            Item item = exchangeItem.getItem();
            Item itemInInventory = currentPartner.getInventory().getItemByObjId(exchangeItem.getItemObjId());

            if (item == null || itemInInventory == null ||
                itemInInventory.getItemCount() < exchangeItem.getItemCount() ||
                exchangeItem.getOriginalItemCount() != itemInInventory.getItemCount())
            {
                return false;
            }
        }

        return validateInventorySize(activePlayer, exchange2)
                && validateInventorySize(currentPartner, exchange1);
    }

    private boolean validateInventorySize(Player activePlayer, Exchange exchange) {
        int numberOfFreeSlots = activePlayer.getInventory().getNumberOfFreeSlots();
        return numberOfFreeSlots >= exchange.getItems().size();
    }

    /**
     * @param player
     * @param exchange1
     * @param exchange2
     */
    private void putItemToInventory(Player player, Exchange exchange1, Exchange exchange2) {
        for (ExchangeItem exchangeItem : exchange1.getItems().values()) {
            Item itemToPut = exchangeItem.getItem();
            itemToPut.setEquipmentSlot(0);
            player.getInventory().putToBag(itemToPut);

            ItemService.updateItem(player, itemToPut, true);
            exchange2.addItemToUpdate(itemToPut);
        }
        long kinahToExchange = exchange1.getKinahCount();
        if (kinahToExchange > 0) {
            player.getInventory().increaseKinah(exchange1.getKinahCount());
            exchange2.addItemToUpdate(player.getInventory().getKinahItem());
        }
    }

    /**
     * Frequent running save task
     */
    public static final class ExchangePeriodicTaskManager extends AbstractFIFOPeriodicTaskManager<ExchangeOpSaveTask> {
        private static final String CALLED_METHOD_NAME = "exchangeOperation()";

        /**
         * @param period
         */
        public ExchangePeriodicTaskManager(int period) {
            super(period);
        }

        @Override
        protected void callTask(ExchangeOpSaveTask task) {
            task.run();
        }

        @Override
        protected String getCalledMethodName() {
            return CALLED_METHOD_NAME;
        }


    }

    /**
     * This class is used for storing all items in one shot after any exchange operation
     */
    public static final class ExchangeOpSaveTask implements Runnable {
        private int player1Id;
        private int player2Id;
        private Set<Item> player1Items;
        private Set<Item> player2Items;

        /**
         * @param player1Id
         * @param plaer2Id
         * @param player1Items
         * @param player2Items
         */
        public ExchangeOpSaveTask(int player1Id, int player2Id, Set<Item> player1Items, Set<Item> player2Items) {
            this.player1Id = player1Id;
            this.player2Id = player2Id;
            this.player1Items = player1Items;
            this.player2Items = player2Items;
        }


        @Override
        public void run() {
            for (Item item : player1Items) {
                DAOManager.getDAO(InventoryDAO.class).store(item, player1Id);
            }

            for (Item item : player2Items) {
                DAOManager.getDAO(InventoryDAO.class).store(item, player2Id);
            }
        }
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final ExchangeService instance = new ExchangeService();
	}
}
