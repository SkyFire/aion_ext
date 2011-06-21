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

import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.services.ItemService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ATracer, ZeroSignal
 */
public class TradeList {
    private int sellerObjId;

    private List<TradeItem> tradeItems = new ArrayList<TradeItem>();

    private long requiredKinah;

    private int requiredAp;

    private Map<Integer, Integer> requiredItems = new HashMap<Integer, Integer>();

    /**
     * @param itemId
     * @param count
     */
    public boolean addBuyItem(int itemId, long count) {

        ItemTemplate itemTemplate = ItemService.getItemTemplate(itemId);
        if (itemTemplate == null) {
            return false;
        }

        TradeItem tradeItem = new TradeItem(itemId, count);
        tradeItem.setItemTemplate(itemTemplate);
        if (!tradeItems.add(tradeItem)) {
            return false;
        }
        return true;
    }

    /**
     * @param tradeItem
     * @param tradePSItem
     */
    public boolean addSellPSItem(TradeItem tradeItem, TradePSItem tradePSItem) {
        ItemTemplate itemTemplate = ItemService.getItemTemplate(tradePSItem.getItemId());
        if (itemTemplate == null) {
            return false;
        }

        TradeItem newTradeItem = new TradeItem(tradePSItem.getItemObjId(), tradeItem.getCount());
        newTradeItem.setItemTemplate(itemTemplate);
        newTradeItem.setItemSlot(tradeItem.getItemId());
        if (!tradeItems.add(newTradeItem)) {
            return false;
        }
        return true;
    }

    /**
     * @param itemObjId
     * @param count
     */
    public boolean addSellItem(int itemObjId, long count) {
        TradeItem tradeItem = new TradeItem(itemObjId, count);
        if (!tradeItems.add(tradeItem)) {
            return false;
        }
        return true;
    }

    /**
     * @return price TradeList sum price
     */
    public boolean calculateBuyListPrice(Player player) {
        long availableKinah = player.getInventory().getKinahItem().getItemCount();
        requiredKinah = 0;

        for (TradeItem tradeItem : tradeItems) {
            requiredKinah += player.getPrices().getKinahForBuy(tradeItem.getItemTemplate().getPrice(), player.getCommonData().getRace()) * tradeItem.getCount();
        }

        return availableKinah >= requiredKinah;
    }

    /**
     * @return true or false
     */
    public boolean calculateAbyssBuyListPrice(Player player) {
        int ap = player.getAbyssRank().getAp();

        this.requiredAp = 0;
        this.requiredItems.clear();

        for (TradeItem tradeItem : tradeItems) {
            requiredAp += tradeItem.getItemTemplate().getAbyssPoints() * tradeItem.getCount();
            int itemId = tradeItem.getItemTemplate().getAbyssItem();

            Integer alreadyAddedCount = requiredItems.get(itemId);
            if (alreadyAddedCount == null) {
                requiredItems.put(itemId, tradeItem.getItemTemplate().getAbyssItemCount());
            } else {
                requiredItems.put(itemId, alreadyAddedCount + tradeItem.getItemTemplate().getAbyssItemCount());
            }
        }

        if (ap < requiredAp) {
            return false;
        }

        for (Integer itemId : requiredItems.keySet()) {
            long count = player.getInventory().getItemCountByItemId(itemId);
            if (count < requiredItems.get(itemId)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return true or false
     */
    public boolean calculateSpecialBuyListPrice(Player player) {
        this.requiredItems.clear();

        for (TradeItem tradeItem : tradeItems) {
            int itemId = tradeItem.getItemTemplate().getExtraCurrencyItem();
            Integer alreadyAddedCount = requiredItems.get(itemId);

            if (alreadyAddedCount == null) {
                requiredItems.put(itemId, tradeItem.getItemTemplate().getExtraCurrencyItemCount() * (int) tradeItem.getCount());
            } else {
                requiredItems.put(itemId, alreadyAddedCount + tradeItem.getItemTemplate().getExtraCurrencyItemCount() * (int) tradeItem.getCount());
            }
        }

        for (Integer itemId : requiredItems.keySet()) {
            long count = player.getInventory().getItemCountByItemId(itemId);
            if (count < requiredItems.get(itemId)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return the tradeItems
     */
    public List<TradeItem> getTradeItems() {
        return tradeItems;
    }

    public int size() {
        return tradeItems.size();
    }

    /**
     * @return the npcId
     */
    public int getSellerObjId() {
        return sellerObjId;
    }

    /**
     * @param sellerObjId the npcId to set
     */
    public void setSellerObjId(int npcObjId) {
        this.sellerObjId = npcObjId;
    }

    /**
     * @return the requiredAp
     */
    public int getRequiredAp() {
        return requiredAp;
    }

    /**
     * @return the requiredKinah
     */
    public long getRequiredKinah() {
        return requiredKinah;
    }

    /**
     * @return the requiredItems
     */
    public Map<Integer, Integer> getRequiredItems() {
        return requiredItems;
    }
}
