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
package gameserver.model;

import javolution.util.FastMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
/**
 * @author zdead
 */
public class NpcStocks {
    Map<Integer, Map<Integer, NpcStocks.NpcStockByPlayer>> stocksByPlayer;;
    List<NpcStocks.NpcStock> stocks;
    
    public NpcStocks()
    {
        stocksByPlayer  = new FastMap<Integer, Map<Integer, NpcStocks.NpcStockByPlayer>>();
        stocks          = new ArrayList<NpcStocks.NpcStock>();
    }
    
    public List<Map<String, Integer>> getAll() {
        List<Map<String, Integer>> tmp = new ArrayList<Map<String, Integer>>();
        for (NpcStocks.NpcStock s : stocks) {
            tmp.add(s.toMap());
        }
        for (Map<Integer, NpcStocks.NpcStockByPlayer> s : stocksByPlayer.values()) {
            for (NpcStocks.NpcStockByPlayer t : s.values())
                tmp.add(t.toMap());
        }
        return tmp;
    }
    
    public void addStock(int playerId, int npcId, int itemTemplateId, int soldCount, long lastSaleDate)
    {
        if (playerId == 0) {
            stocks.add(new NpcStocks.NpcStock(npcId, itemTemplateId, soldCount));
        } else {
            if (!stocksByPlayer.containsKey(playerId))
                stocksByPlayer.put(playerId, new FastMap<Integer, NpcStocks.NpcStockByPlayer>());
            stocksByPlayer.get(playerId).put(itemTemplateId, new NpcStocks.NpcStockByPlayer(playerId, npcId, itemTemplateId, soldCount, lastSaleDate));
        }
    }
    
    public int getCountSoldToPlayer(int playerId, int npcId, int itemTemplateId) {
        if (stocksByPlayer.containsKey(playerId)) {
            if (stocksByPlayer.get(playerId).containsKey(itemTemplateId)) {
                Date date = new Date();
                long currentDate = date.getTime();
                if (currentDate > stocksByPlayer.get(playerId).get(itemTemplateId).getLastSaleDate())
                    return stocksByPlayer.get(playerId).get(itemTemplateId).getSoldCount();
            }
        }
        return 0;
    }
    
    public int getItemCountSold(int npcId, int itemTemplateId) {
        for (NpcStocks.NpcStock stock : stocks) {
            if (stock.getNpcId() == npcId && stock.getItemTemplateId() == itemTemplateId)
                return stock.getSoldCount();
        }
        return 0;
    }
    
    public void increaseSoldCount(int npcId, int itemTemplateId, int soldCount) {
        boolean found = false;
        for (NpcStocks.NpcStock stock : stocks) {
            if (stock.getNpcId() == npcId && stock.getItemTemplateId() == itemTemplateId) {
                stock.increaseSoldCount(soldCount);
                found = true;
            }
        }
        if (!found)
            addStock(0, npcId, itemTemplateId, soldCount, 0);
    }
    
    public void increaseSoldCountToPlayer(int playerId, int npcId, int itemTemplateId, int count) {
        boolean found = false;
        if (stocksByPlayer.containsKey(playerId)) {
            if (stocksByPlayer.get(playerId).containsKey(itemTemplateId)) {
                Date date = new Date();
                long currentDate = date.getTime();
                if (currentDate > stocksByPlayer.get(playerId).get(itemTemplateId).getLastSaleDate())
                    stocksByPlayer.get(playerId).get(itemTemplateId).increaseSoldCount(count);
                else
                    stocksByPlayer.get(playerId).get(itemTemplateId).setSoldCount(count);
                found = true;
            }
        }
        if (!found) {
            Date date = new Date();
            long currentDate = date.getTime();
            addStock(playerId, npcId, itemTemplateId, count, currentDate);
        }
    }
    
    public void restockNpcs() {
        for (NpcStocks.NpcStock s : stocks)
            s.resetSoldCount();
    }

    public static class NpcStock {
        protected int npcId;
        protected int itemTemplateId;
        protected int soldCount;
        
        public NpcStock(int npcId, int itemTemplateId, int soldCount) {
            this.npcId          = npcId;
            this.itemTemplateId = itemTemplateId;
            this.soldCount      = soldCount;
        }
        
        public Map<String, Integer> toMap() {
            Map<String, Integer> map = new FastMap<String, Integer>();
            map.put("playerId", 0);
            map.put("npcId",    npcId);
            map.put("itemTemplateId", itemTemplateId);
            map.put("count",    soldCount);
            map.put("lastSaleDate", 0);
            return map;
        }
        
        public int getSoldCount() {
            return soldCount;
        }
        
        public int getNpcId() {
            return npcId;
        }
        
        public int getItemTemplateId() {
            return itemTemplateId;
        }
        
        public void increaseSoldCount(int soldCount) {
            this.soldCount = this.soldCount + soldCount;
        }
        
        public void resetSoldCount() {
            this.soldCount = 0;
        }
    }
    
    public static class NpcStockByPlayer {
        protected int playerId;
        protected int npcId;
        protected int itemTemplateId;
        protected int soldCount;
        protected long lastSaleDate;
        
        public NpcStockByPlayer(int playerId, int npcId, int itemTemplateId, int soldCount, long lastSaleDate) {
            this.playerId       = playerId;
            this.npcId          = npcId;
            this.itemTemplateId = itemTemplateId;
            this.soldCount      = soldCount;
            this.lastSaleDate   = lastSaleDate;
        }
        
        public Map<String, Integer> toMap() {
            Map<String, Integer> map = new FastMap<String, Integer>();
            map.put("playerId", playerId);
            map.put("npcId",    npcId);
            map.put("itemTemplateId", itemTemplateId);
            map.put("count",    soldCount);
            map.put("lastSaleDate", (int)lastSaleDate);
            return map;
        }
        
        public int getSoldCount() {
            return soldCount;
        }
        
        public long getLastSaleDate() {
            return lastSaleDate;
        }
        
        public void increaseSoldCount(int soldCount) {
            this.soldCount = this.soldCount + soldCount;
            Date currentDate = new Date();
            this.lastSaleDate = currentDate.getTime() / 1000;
        }

        public void setSoldCount(int count) {
            soldCount = count;
            Date currentDate = new Date();
            lastSaleDate = currentDate.getTime() / 1000;
        }
    }
}
