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

import gameserver.services.ItemService;

/**
 * @author Simple, ZeroSignal
 */
public class TradePSItem extends TradeItem {
    private int itemObjId;
    private long price;

    /**
     * @param itemId
     * @param count
     */
    public TradePSItem(int itemObjId, int itemId, long count, long price) {
        super(itemId, count);
        this.itemObjId = itemObjId;
        this.price = price;
        this.itemTemplate = ItemService.getItemTemplate(itemId);
    }

    /**
     * @param price the price to set
     */
    public void setPrice(long price) {
        this.price = price;
    }

    /**
     * @return the price
     */
    public long getPrice() {
        return price;
    }

    /**
     * @param itemObjId the itemObjId to set
     */
    public void setItemObjId(int itemObjId) {
        this.itemObjId = itemObjId;
    }

    /**
     * @return the itemObjId
     */
    public int getItemObjId() {
        return itemObjId;
    }
    
    @Override
    public String toString() {
        return "TradePSItem - " +
            "itemId: " + itemId +
            ", count: " + count +
            ", itemObjId: " + itemObjId +
            ", price: " + price + ". ";
    }
}
