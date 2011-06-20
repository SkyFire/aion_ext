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

import gameserver.dao.InventoryDAO;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.gameobjects.player.StorageType;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_DELETE_WAREHOUSE_ITEM;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aionemu.commons.database.dao.DAOManager;

/**
 * Handling of rented items
 * @author SuneC
 */
public class RentalService {
	private static Logger log = Logger.getLogger(RentalService.class);
	
	private Map<Item, Integer> rentalItems = new HashMap<Item, Integer>();
	
	private int periodicUpdateInterval = 10; //seconds
	
	private RentalService() {
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				performUpdate();
			}
		}, periodicUpdateInterval * 1000, periodicUpdateInterval * 1000);
		log.info("RentalService started!");
	}
	
	private void performUpdate() {
		List<Item> itemsToRemove = new ArrayList<Item>();
		
		Player player;
		for(Item item : rentalItems.keySet()) {
			if(item.getExpireTime() != null && item.getExpireTime().getTime() != 0) {
				if(item.getExpireTime().getTime() <= System.currentTimeMillis()) {
					player = World.getInstance().findPlayer(rentalItems.get(item));
					if(player == null) {
						itemsToRemove.add(item);
						continue;
					}
					
					if(removeItem(player, item.getObjectId()))
						itemsToRemove.add(item);
					else
						log.error("RentalService: Unable to remove " + item.getObjectId() + " from " + player.getName());
				}
			}
		}
		
		for(Item item : itemsToRemove) {
			removeRentalItem(item);
		}
	}
	
	private boolean removeItem(Player player, Integer itemObjectId) {
		Storage storage = null;
		Item item = null;
		
		for(StorageType storageType : StorageType.values()) {
			storage = player.getStorage(storageType.getId());
			if(storage == null)
				continue;
			item = storage.getItemByObjId(itemObjectId);
			if(item != null)
				break;
		}
		
		if(item == null || item.isEquipped()) {
			item = player.getEquipment().getEquippedItemByObjId(itemObjectId);
			if (item == null)
				return false;
			
			player.getEquipment().unEquipItem(itemObjectId, item.getEquipmentSlot());
		}
		
		if(storage == null)
		    return false;
		
		storage.removeFromBag(item, true);
		if (storage.getStorageType() == StorageType.CUBE.getId())
            PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(item.getObjectId()));
        else
            PacketSendUtility.sendPacket(player, new SM_DELETE_WAREHOUSE_ITEM(storage.getStorageType(), item.getObjectId()));
		
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DELETE_CASH_ITEM_BY_TIMEOUT(item.getNameID()));
		DAOManager.getDAO(InventoryDAO.class).store(item, player.getObjectId());
		
		//log.info("Removed " + item.getObjectId() + " from " + player.getName());
		
		return true;
	}
	
	public void addRentalItem(Player player, Item item) {
		if(player != null && item != null)
			addRentalItem(player.getObjectId(), item);
	}
	
	public void addRentalItem(Integer playerObjId, Item item) {
		if(playerObjId != null && item != null)
			rentalItems.put(item, playerObjId);
		//log.info("Item " + item.getObjectId() + " added to rental list!");
	}
	
	public void removeRentalItem(Item item) {
		if(rentalItems.containsKey(item))
			rentalItems.remove(item);
		//log.info("Removed item " + item.getObjectId() + " from rental list!");
	}
	
	public boolean isRentalItem(Item item) {
		if(item.getExpireTime() == null || item.getExpireTime().getTime() == 0)
			return false;
		return true;
	}
	
	public int getRentalTimeLeft(Item item) {
		if(isRentalItem(item))
			return (int)Math.round((item.getExpireTime().getTime() - System.currentTimeMillis()) / 1000);
		return 0;
	}
	
	@SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final RentalService instance = new RentalService();
    }
	
	public static final RentalService getInstance() {
		return SingletonHolder.instance;
	}
}
