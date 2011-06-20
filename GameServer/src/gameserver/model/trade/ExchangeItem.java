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
package gameserver.model.trade;

import gameserver.model.gameobjects.Item;

/**
 * @author ATracer
 */
public class ExchangeItem {
    private int itemObjId;
    private long itemCount;
    private long originalItemCount;
    private int itemDesc;
    private Item item;

    /**
     * Used when exchange item != original item
     *
     * @param itemObjId
     * @param itemCount
     * @param item
     */
    public ExchangeItem(int itemObjId, long itemCount, Item item) {
        Init(itemObjId, itemCount, item, 0);
    }

    public ExchangeItem(int itemObjId, long itemCount, Item item, long originalItemCount) {
        Init(itemObjId, itemCount, item, originalItemCount);
    }

    public void Init(int itemObjId, long itemCount, Item item, long originalItemCount) {
        this.itemObjId = itemObjId;
        this.itemCount = itemCount;
        this.originalItemCount = originalItemCount;
        this.item = item;
        this.itemDesc = item.getItemTemplate().getNameId();
    }

    public long getOriginalItemCount() {
        return originalItemCount;
    }

    /**
     * @param item the item to set
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * @param countToAdd
     */
    public void addCount(long countToAdd) {
        this.itemCount += countToAdd;
        this.item.setItemCount(itemCount);
    }

    /**
     * @return the newItem
     */
    public Item getItem() {
        return item;
    }

    /**
     * @return the itemObjId
     */
    public int getItemObjId() {
        return itemObjId;
    }

    /**
     * @return the itemCount
     */
    public long getItemCount() {
        return itemCount;
    }

    /**
     * @return the itemDesc
     */
    public int getItemDesc() {
        return itemDesc;
	}
}
