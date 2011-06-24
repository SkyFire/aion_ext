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
package gameserver.model.inGameShop;

/**
 * @author PZIKO333
 */

public class InGameShop {
    private int objectId;
    private int itemId;
    private int itemCount;
    private int itemPrice;
    private int category;
    private int list;
    private int salesRanking;
    private String description;

    public InGameShop(int objectId, int itemId, int itemCount, int itemPrice, int category, int list, int salesRanking, String description) {
        this.objectId = objectId;
        this.itemId = itemId;
        this.itemCount = itemCount;
        this.itemPrice = itemPrice;
        this.category = category;
        this.list = list;
        this.salesRanking = salesRanking;
        this.description = description;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public int getItemId() {
        return this.itemId;
    }

    public int getItemCount() {
        return this.itemCount;
    }

    public int getItemPrice() {
        return this.itemPrice;
    }

    public int getCategory() {
        return this.category;
    }

    public int getList() {
        return this.list;
    }

    public int getSalesRanking() {
        return this.salesRanking;
    }

    public String getDescription() {
        return this.description;
    }
}