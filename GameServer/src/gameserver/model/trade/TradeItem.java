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

import gameserver.model.templates.item.ItemTemplate;

/**
 * @author ATracer, ZeroSignal
 */
public class TradeItem {
    protected int itemSlot = 0;
    protected int itemId;
    protected long count;
    protected ItemTemplate itemTemplate;

    public TradeItem(int itemId, long count) {
        this.itemId = itemId;
        this.count = count;
    }

    /**
     * @return the itemTemplate
     */
    public ItemTemplate getItemTemplate() {
        return itemTemplate;
    }

    /**
     * @param itemTemplate the itemTemplate to set
     */
    public void setItemTemplate(ItemTemplate itemTemplate) {
        this.itemTemplate = itemTemplate;
    }

    /**
     * @return the itemId
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * @return the count
     */
    public long getCount() {
        return this.count;
    }

    public int getItemSlot() {
        return this.itemSlot;
    }

    public void setItemSlot(int slot) {
        this.itemSlot = slot;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     * This method will decrease the current count
     */
    public void decreaseCount(long decreaseCount) {
        if (decreaseCount < this.count)
            this.count -= decreaseCount;
    }

    public String toString() {
        return "TradeItem - " +
            "itemSlot: " + itemSlot +
            ", itemId: " + itemId +
            ", count: " + count + ". ";
    }
}
