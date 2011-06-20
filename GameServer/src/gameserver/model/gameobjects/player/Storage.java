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
package gameserver.model.gameobjects.player;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.items.ItemStorage;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.network.aion.serverpackets.SM_UPDATE_WAREHOUSE_ITEM;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Avol
 *         modified by ATracer, kosyachok
 */
public class Storage {
    private static final Logger log = Logger.getLogger(Storage.class);

    private Player owner;

    protected ItemStorage storage;

    private Item kinahItem;

    protected int storageType;

    protected Queue<Item> deletedItems = new ConcurrentLinkedQueue<Item>();

    /**
     * Can be of 2 types: UPDATED and UPDATE_REQUIRED
     */
    private PersistentState persistentState = PersistentState.UPDATED;

    /**
     * Will be enhanced during development.
     */
    public Storage(StorageType storageType) {
        switch (storageType) {
            case CUBE:
                storage = new ItemStorage(109);
                this.storageType = storageType.getId();
                break;
            case REGULAR_WAREHOUSE:
                storage = new ItemStorage(96);
                this.storageType = storageType.getId();
                break;
            case ACCOUNT_WAREHOUSE:
                storage = new ItemStorage(16);
                this.storageType = storageType.getId();
                break;
            case LEGION_WAREHOUSE:
                storage = new ItemStorage(56);
                this.storageType = storageType.getId();
                break;
            case PET_BAG_6:
                storage = new ItemStorage(6);
                this.storageType = storageType.getId();
                break;
            case PET_BAG_12:
            	storage = new ItemStorage(12);
            	this.storageType = storageType.getId();
            	break;
            case PET_BAG_18:
            	storage = new ItemStorage(18);
            	this.storageType = storageType.getId();
            	break;
            case PET_BAG_24:
            	storage = new ItemStorage(24);
            	this.storageType = storageType.getId();
                break;
        }
    }

    /**
     * @param owner
     */
    public Storage(Player owner, StorageType storageType) {
        this(storageType);
        this.owner = owner;
    }

    /**
     * @return the owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * @return the kinahItem
     */
    public Item getKinahItem() {
        return kinahItem;
    }

    public long getKinahCount() {
        if (kinahItem == null)
            return 0;
        else return kinahItem.getItemCount();
    }

    public int getStorageType() {
        return storageType;
    }

    /**
     * Increasing kinah amount is persisted immediately
     *
     * @param amount
     */
    public void increaseKinah(long amount) {
        kinahItem.increaseItemCount(amount);

        if (storageType == StorageType.CUBE.getId())
            PacketSendUtility.sendPacket(getOwner(), new SM_UPDATE_ITEM(kinahItem));

        if (storageType == StorageType.ACCOUNT_WAREHOUSE.getId())
            PacketSendUtility.sendPacket(getOwner(), new SM_UPDATE_WAREHOUSE_ITEM(kinahItem, storageType));

        if (storageType == StorageType.LEGION_WAREHOUSE.getId())
            PacketSendUtility.sendPacket(getOwner(), new SM_UPDATE_WAREHOUSE_ITEM(kinahItem, storageType));

        setPersistentState(PersistentState.UPDATE_REQUIRED);
    }

    /**
     * Decreasing kinah amount is persisted immediately
     *
     * @param amount
     */
    public boolean decreaseKinah(long amount) {
        boolean operationResult = kinahItem.decreaseItemCount(amount);
        if (operationResult) {
            if (storageType == StorageType.CUBE.getId())
                PacketSendUtility.sendPacket(getOwner(), new SM_UPDATE_ITEM(kinahItem));

            if (storageType == StorageType.ACCOUNT_WAREHOUSE.getId())
                PacketSendUtility.sendPacket(getOwner(), new SM_UPDATE_WAREHOUSE_ITEM(kinahItem, storageType));
            
            if (storageType == StorageType.LEGION_WAREHOUSE.getId())
            	PacketSendUtility.sendPacket(getOwner(), new SM_UPDATE_WAREHOUSE_ITEM(kinahItem, storageType));
        }
        setPersistentState(PersistentState.UPDATE_REQUIRED);
        return operationResult;
    }

    /**
     * This method should be called only for new items added to inventory (loading from DB)
     * If item is equiped - will be put to equipment
     * if item is unequiped - will be put to default bag for now
     * Kinah is stored separately as it will be used frequently
     *
     * @param item
     */
    public void onLoadHandler(Item item) {
        if (item.isEquipped()) {
            owner.getEquipment().onLoadHandler(item);
        } else if (item.getItemTemplate().isKinah()) {
            kinahItem = item;
        } else {
            storage.putToNextAvailableSlot(item);
        }
    }

    /**
     * Used to put item into storage cube at first avaialble slot (no check for existing item)
     * During unequip/equip process persistImmediately should be false
     *
     * @param item
     * @param persistImmediately
     * @return Item
     */
    public Item putToBag(Item item) {
        Item resultItem = storage.putToNextAvailableSlot(item);
        if (resultItem != null) {
            resultItem.setItemLocation(storageType);
        }
        setPersistentState(PersistentState.UPDATE_REQUIRED);
        return resultItem;
    }

    /**
     * Removes item completely from inventory.
     * Every remove operation is persisted immediately now
     *
     * @param item
     */
    public void removeFromBag(Item item, boolean persist) {
        boolean operationResult = storage.removeItemFromStorage(item);
        if (operationResult && persist) {
            item.setPersistentState(PersistentState.DELETED);
            deletedItems.add(item);
            setPersistentState(PersistentState.UPDATE_REQUIRED);
        }
    }


    /**
     * Used to reduce item count in bag or completely remove by ITEMID
     * This method operates in iterative manner overl all items with specified ITEMID.
     * Return value can be the following:
     * - true - item removal was successfull
     * - false - not enough amount of items to reduce
     * or item is not present
     *
     * @param itemId
     * @param count
     * @return true or false
     */
    public boolean removeFromBagByItemId(int itemId, long count) {
        if (count < 1)
            return false;

        List<Item> items = storage.getItemsFromStorageByItemId(itemId);

        for (Item item : items) {
            count = decreaseItemCount(item, count);

            if (count == 0)
                break;
        }
        boolean result = count >= 0;
        if (result)
            setPersistentState(PersistentState.UPDATE_REQUIRED);
        return result;
    }

    /**
     * @param itemId
     * @return Item
     */
    public Item getFirstItemByItemId(int itemId) {
        List<Item> items = storage.getItemsFromStorageByItemId(itemId);
        if (items.size() == 0)
            return null;
        return items.get(0);
    }

    /**
     * Used to reduce item count in bag or completely remove by OBJECTID
     * Return value can be the following:
     * - true - item removal was successfull
     * - false - not enough amount of items to reduce
     * or item is not present
     *
     * @param itemObjId
     * @param count
     * @return true or false
     */
    public boolean removeFromBagByObjectId(int itemObjId, long count) {
        if (count < 1)
            return false;

        Item item = storage.getItemFromStorageByItemObjId(itemObjId);
        if (item == null) { // the item doesn't exist, return false if the count is bigger then 0.
            log.warn("An item from player '" + getOwner().getName() + "' that should be removed doesn't exist.");
            return count == 0;
        }
        boolean result = decreaseItemCount(item, count) >= 0;
        if (result)
            setPersistentState(PersistentState.UPDATE_REQUIRED);
        return result;
    }

    /**
     * This method decreases inventory's item by count and sends
     * appropriate packets to owner.
     * Item will be saved in database after update or deleted if count=0 (and persist=true)
     *
     * @param count should be > 0
     * @param item
     * @return
     */
    public long decreaseItemCount(Item item, long count) {
        long itemCount = item.getItemCount();
        if (itemCount >= count) {
            item.decreaseItemCount(count);
            count = 0;
        } else {
            item.decreaseItemCount(itemCount);
            count -= itemCount;
        }
        if (item.getItemCount() == 0) {
            storage.removeItemFromStorage(item);
            PacketSendUtility.sendPacket(getOwner(), new SM_DELETE_ITEM(item.getObjectId()));
            deletedItems.add(item);
        } else
            PacketSendUtility.sendPacket(getOwner(), new SM_UPDATE_ITEM(item));

        setPersistentState(PersistentState.UPDATE_REQUIRED);
        return count;
    }

    /**
     * Method primarily used when saving to DB
     *
     * @return List<Item>
     */
    public List<Item> getAllItems() {
        List<Item> allItems = new ArrayList<Item>();
        if (kinahItem != null)
            allItems.add(kinahItem);
        allItems.addAll(storage.getStorageItems());
        return allItems;
    }

    /**
     * All deleted items with persistent state DELETED
     *
     * @return
     */
    public Queue<Item> getDeletedItems() {
        return deletedItems;
    }

    /**
     * Searches for item with specified itemId in equipment and cube
     *
     * @param itemId
     * @return List<Item>
     */
    public List<Item> getAllItemsByItemId(int itemId) {
        List<Item> allItemsByItemId = new ArrayList<Item>();

        for (Item item : storage.getStorageItems()) {
            if (item.getItemTemplate().getTemplateId() == itemId)
                allItemsByItemId.add(item);
        }
        return allItemsByItemId;
    }


    public List<Item> getStorageItems() {
        return storage.getStorageItems();
    }

    /**
     * Will look item in default item bag
     *
     * @param value
     * @return Item
     */
    public Item getItemByObjId(int value) {
        Item item = storage.getItemFromStorageByItemObjId(value);
        return item;
    }

    /**
     * Will look item in default item bag
     *
     * @param value
     * @return isItem contained.
     */
    public boolean isItemByObjId(int value) {
        return storage.isItemFromStorageByItemObjId(value);
    }

    /**
     * @param value
     * @return List<Item>
     */
    public List<Item> getItemsByItemId(int value) {
        return storage.getItemsFromStorageByItemId(value);
    }

    /**
     * @param itemId
     * @return number of items using search by itemid
     */
    public long getItemCountByItemId(int itemId) {
        List<Item> items = getItemsByItemId(itemId);
        long count = 0;
        for (Item item : items) {
            count += item.getItemCount();
        }
        return count;
    }

    /**
     * Checks whether default cube is full
     *
     * @return true or false
     */
    public boolean isFull() {
        return storage.isFull();
    }

    /**
     * Number of available slots of the underlying storage
     *
     * @return
     */
    public int getNumberOfFreeSlots() {
        return storage.getNumberOfFreeSlots();
    }

    /**
     * Sets the Inventory Limit from Cube Size
     *
     * @param Limit
     */
    public void setLimit(int limit) {
        this.storage.setLimit(limit);
    }

    /**
     * Limit value of the underlying storage
     *
     * @return
     */
    public int getLimit() {
        return this.storage.getLimit();
    }

    /**
     * @return the persistentState
     */
    public PersistentState getPersistentState() {
        return persistentState;
    }

    /**
     * @param persistentState the persistentState to set
     */
    public void setPersistentState(PersistentState persistentState) {
        this.persistentState = persistentState;
    }

    /**
	 * @param item
	 * @param count
	 */
	public void increaseItemCount(Item item, long count)
	{
		item.increaseItemCount(count);
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
     * Size of underlying storage
     *
     * @return
     */
	public int size()
	{
		return storage.size();
	}
}
