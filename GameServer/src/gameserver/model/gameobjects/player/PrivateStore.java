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

import gameserver.model.trade.TradePSItem;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Xav, Simple, ZeroSignal
 */
public class PrivateStore {
    private Player owner;
    private List<TradePSItem> items = new ArrayList<TradePSItem>();
    private String storeMessage;

    /**
     * This method binds a player to the store and creates a list of items
     *
     * @param owner
     */
    public PrivateStore(Player owner) {
        this.owner = owner;
    }

    /**
     * This method will return the owner of the store
     *
     * @return Player
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * This method will return the items being sold
     *
     * @return List<TradePSItem>
     */
    public List<TradePSItem> getSoldItems() {
        return items;
    }

    /**
     * This method will add an item to the list and price
     *
     * @param tradeList
     * @param price
     */
    public void addItemToSell(TradePSItem tradeItem) {
        items.add(tradeItem);
    }

    /**
     * This method will remove an item from the list
     *
     * @param itemSlot
     */
    public boolean removeItem(int itemSlot) {
        return (items.remove(itemSlot) != null);
    }

    /**
     * @param itemSlot return tradeItem
     */
    public TradePSItem getTradeItemBySlot(int itemSlot) {
        try {
            return items.get(itemSlot);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * @param storeMessage the storeMessage to set
     */
    public void setStoreMessage(String storeMessage) {
        this.storeMessage = storeMessage;
    }

    /**
     * @return the storeMessage
     */
    public String getStoreMessage() {
        return storeMessage;
    }

    public String toString() {
        String output;
        output = "PrivateStore - " +
            "owner: " + owner.getObjectId() +
            ",storeMessage: " + storeMessage;
        for (TradePSItem storeItem : items) {
            output += ",storeItem: " + storeItem.toString();
        }
        return output;
    }
}
