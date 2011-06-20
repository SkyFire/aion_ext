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
import gameserver.configs.main.CustomConfig;
import gameserver.dataholders.DataManager;
import gameserver.dataholders.GoodsListData;
import gameserver.dataholders.TradeListData;
import gameserver.dao.NpcStocksDAO;
import gameserver.model.NpcStocks;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.AbyssRank;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.templates.TradeListTemplate;
import gameserver.model.templates.TradeListTemplate.TradeTab;
import gameserver.model.templates.goods.GoodsList;
import gameserver.model.trade.TradeItem;
import gameserver.model.trade.TradeList;
import gameserver.network.aion.serverpackets.SM_ABYSS_RANK;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.scheduler.Scheduler;
import gameserver.world.World;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author ATracer, Rama, Flay
 */
public class TradeService {
    private static final Logger log = Logger.getLogger(TradeService.class);


    private static final TradeListData tradeListData = DataManager.TRADE_LIST_DATA;
    private static final GoodsListData goodsListData = DataManager.GOODSLIST_DATA;
    
    private final NpcStocks npcStocks;
    
    public TradeService()
    {
        npcStocks = DAOManager.getDAO(NpcStocksDAO.class).getStocks();
        Scheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                restockNpc();
            }
        }, "0 10-24/2");
    }
    
    /**
     * @param player
     * @param tradeList
     * @return true or false
     */

    public static boolean performBuyFromShop(Player player, TradeList tradeList) {

        if (!validateBuyItems(tradeList, player)) {
            PacketSendUtility.sendMessage(player, "Some items are not allowed to be sold by this npc.");
            if (CustomConfig.ARTMONEY_HACK)
            PunishmentService.setIsInPrison(player, true, CustomConfig.ARTMONEY_HACKBUY_TIME);
            return false;
        }

        Storage inventory = player.getInventory();
        Item kinahItem = inventory.getKinahItem();

        // 1. check kinah
        if (!tradeList.calculateBuyListPrice(player))
            return false;

        // 2. check free slots, need to check retail behaviour
        int freeSlots = inventory.getLimit() - inventory.getAllItems().size() + 1;
        if (freeSlots < tradeList.size())
            return false; // TODO message

        long tradeListPrice = tradeList.getRequiredKinah();
        
        Npc npc = (Npc) World.getInstance().findAionObject(tradeList.getSellerObjId());
        TradeListTemplate tradeListTemplate = tradeListData.getTradeListTemplate(npc.getObjectTemplate()
                .getTemplateId());

        List<Integer> stockLimitedItems = new ArrayList<Integer>();
        for (TradeTab tradeTab : tradeListTemplate.getTradeTablist()) {
            GoodsList goodsList = goodsListData.getGoodsListById(tradeTab.getId());
            if (goodsList != null && goodsList.getItemList() != null) {
                for (GoodsList.Item tmpItem : goodsList.getItemList()) {
                    if (tmpItem.isLimited())
                        stockLimitedItems.add(tmpItem.getId());
                }
            }
        }


        List<Item> addedItems = new ArrayList<Item>();
        for (TradeItem tradeItem : tradeList.getTradeItems()) {
            long count = ItemService.addItem(player, tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount());
            /*
            */
            int itemTemplateId = tradeItem.getItemTemplate().getTemplateId();
            if (stockLimitedItems.contains(itemTemplateId)) {
                int npcId = tradeList.getSellerObjId();
                int playerId = player.getCommonData().getPlayerObjId();
                TradeService.getInstance().increaseItemSoldToPlayer(npcId, playerId, itemTemplateId, (int)tradeItem.getCount());
                TradeService.getInstance().decreaseItemStock(npcId, itemTemplateId, (int)tradeItem.getCount());
            }

            
            if (count != 0) {
                log.warn(String.format("CHECKPOINT: itemservice couldnt add all items on buy: %d %d %d %d", player
                        .getObjectId(), tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount(), count));
                inventory.decreaseKinah(tradeListPrice);
                return false;
            }
        }
        inventory.decreaseKinah(tradeListPrice);
        PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(kinahItem));
        PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(addedItems));
        // TODO message
        return true;
    }
    
   
    public int getCountItemSoldToPlayer(int npcId, int playerId, int itemTemplateId) {
        int soldCount = npcStocks.getCountSoldToPlayer(playerId, npcId, itemTemplateId);
        return soldCount;
    }

    //  restock only npc concerned (ether / flux) and with different schedules.
    public void restockNpc() {
        DAOManager.getDAO(NpcStocksDAO.class).restockNpcs();
        npcStocks.restockNpcs();
    }

    public int getItemStock(int npcId, int itemTemplateId, int limit) {
        int soldCount = npcStocks.getItemCountSold(npcId, itemTemplateId);
        return limit - soldCount;
    }

    public void decreaseItemStock(int npcId, int itemTemplateId, int count) {
        npcStocks.increaseSoldCount(npcId, itemTemplateId, count);
    }

    public void increaseItemSoldToPlayer(int npcId, int playerId, int itemTemplateId, int count) {
        npcStocks.increaseSoldCountToPlayer(playerId, npcId, itemTemplateId, count);
    }

    public List<Map<String, Integer>> getNpcStocks() {
        return npcStocks.getAll();
    }


    /**
     * Probably later merge with regular buy
     *
     * @param player
     * @param tradeList
     * @return true or false
     */
    public static boolean performBuyFromAbyssShop(Player player, TradeList tradeList) {

        if (!validateBuyItems(tradeList, player)) {
            PacketSendUtility.sendMessage(player, "Some items are not allowed to be selled from this npc");
            if (CustomConfig.ARTMONEY_HACK)
            PunishmentService.setIsInPrison(player, true, CustomConfig.ARTMONEY_HACKBUY_TIME);
            return false;
        }

        Storage inventory = player.getInventory();
        int freeSlots = inventory.getLimit() - inventory.getAllItems().size() + 1;
        AbyssRank rank = player.getAbyssRank();

        // 1. check required items and ap
        if (!tradeList.calculateAbyssBuyListPrice(player))
            return false;
        /**
         * @author AionEngine, Flay
         *
         */
         if(tradeList.getRequiredAp() < 0)
         {
          log.warn("[AUDIT] Player: " + player.getName() + " posible client hack. tradeList.getRequiredAp() < 0");
          return false;
         }

        // 2. check free slots, need to check retail behaviour
        if (freeSlots < tradeList.size())
            return false; // TODO message

        List<Item> addedItems = new ArrayList<Item>();
        for (TradeItem tradeItem : tradeList.getTradeItems()) {
            long count = ItemService.addItem(player, tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount());
            if (count != 0) {
                log.warn(String.format("CHECKPOINT: itemservice couldnt add all items on buy: %d %d %d %d", player
                        .getObjectId(), tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount(), count));
                player.getCommonData().setAp(rank.getAp() - tradeList.getRequiredAp());
                return false;
            }
        }
        player.getCommonData().setAp(rank.getAp() - tradeList.getRequiredAp());
        Map<Integer, Integer> requiredItems = tradeList.getRequiredItems();
        for (Integer itemId : requiredItems.keySet()) {
            player.getInventory().removeFromBagByItemId(itemId, requiredItems.get(itemId));
        }

        PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK(rank));
        PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(addedItems));
        // TODO message
        return true;
    }

    /**
     * @param player
     * @param tradeList
     */
    public static boolean performBuyFromSpecialShop(Player player, TradeList tradeList)
    {
        if (!validateBuyItems(tradeList, player)) {
            PacketSendUtility.sendMessage(player, "Some items are not allowed to be selled from this npc");
            if (CustomConfig.ARTMONEY_HACK)
            PunishmentService.setIsInPrison(player, true, CustomConfig.ARTMONEY_HACKBUY_TIME);
            return false;
        }

        if (!tradeList.calculateSpecialBuyListPrice(player))
            return false;

        Storage inventory = player.getInventory();
        int freeSlots = inventory.getLimit() - inventory.getAllItems().size() + 1;

        if (freeSlots < tradeList.size())
            return false;

        List<Item> addedItems = new ArrayList<Item>();

        for (TradeItem tradeItem : tradeList.getTradeItems()) {
            long count = ItemService.addItem(player, tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount());
            if (count != 0) {
                log.warn(String.format("CHECKPOINT: itemservice couldnt add all items on buy: %d %d %d %d", player
                    .getObjectId(), tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount(), count));
                return false;
            }
        }

        Map<Integer, Integer> requiredItems = tradeList.getRequiredItems();
        for (Integer itemId : requiredItems.keySet()) {
            player.getInventory().removeFromBagByItemId(itemId, requiredItems.get(itemId));
        }

        PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(addedItems));

        return true;
    }

    /**
     * @param tradeList
     */
    private static boolean validateBuyItems(TradeList tradeList, Player player) {
        Npc npc = (Npc) World.getInstance().findAionObject(tradeList.getSellerObjId());
        TradeListTemplate tradeListTemplate = tradeListData.getTradeListTemplate(npc.getObjectTemplate()
                .getTemplateId());

        Set<Integer> allowedItems = new HashSet<Integer>();
        for (TradeTab tradeTab : tradeListTemplate.getTradeTablist()) {
            GoodsList goodsList = goodsListData.getGoodsListById(tradeTab.getId());
            if (goodsList != null && goodsList.getItemIdList() != null)
                allowedItems.addAll(goodsList.getItemIdList());
        }

        for (TradeItem tradeItem : tradeList.getTradeItems()) {
            if(tradeItem.getCount() < 1)
            {
                log.warn("[AUDIT] Player: " + player.getName() + " posible client hack. Trade count < 1");

                return false;
            }

            if((long)tradeItem.getItemTemplate().getMaxStackCount() < tradeItem.getCount())
            {
            log.warn("[AUDIT] Player: " + player.getName() + " posible client hack. item count > MaxStackCount");

                return false;
            }
            if (!allowedItems.contains(tradeItem.getItemId()))
                return false;
        }
        return true;
    }

    /**
     * @param player
     * @param tradeList
     * @return true or false
     */
    public static boolean performSellToShop(Player player, TradeList tradeList) {
        Storage inventory = player.getInventory();

        boolean result = true;
        long kinahReward = 0;
        for (TradeItem tradeItem : tradeList.getTradeItems()) {
            Item item = inventory.getItemByObjId(tradeItem.getItemId());
            if (item == null) {
                result = false;
                continue;
            }

            if (item.getItemTemplate() == null) {
                result = false;
                continue;
            }

            tradeItem.setItemTemplate(item.getItemTemplate());

            // 2) don't allow to sell non-sellable items
            if (!item.getItemTemplate().isSellable()) {
                log.warn("[AUDIT] Selling exploit, tried to sell unsellable item: " + player.getName());
                result = false;
                continue;
            }

            if (item.getItemCount() - tradeItem.getCount() == 0) {
                inventory.removeFromBag(item, true); // need to be here to avoid exploit by sending packet with many
                // items with same unique ids
                kinahReward += item.getItemTemplate().getPrice() * item.getItemCount();

                // TODO check retail packet here
                PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(item.getObjectId()));
            } else if (item.getItemCount() - tradeItem.getCount() > 0) {
                if (inventory.decreaseItemCount(item, tradeItem.getCount()) >= 0) {
                    // TODO check retail packet here
                    kinahReward += item.getItemTemplate().getPrice() * tradeItem.getCount();
                    PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(item));
                } else {
                    result = false;
                    continue;
                }
            } else {
                result = false;
                continue;
            }
        }

        Item kinahItem = inventory.getKinahItem();
        kinahReward = player.getPrices().getKinahForSell(kinahReward, player.getCommonData().getRace());
        inventory.increaseKinah(kinahReward);
        PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(kinahItem));

        return result;
    }

    /**
     * @return the tradeListData
     */
    public static TradeListData getTradeListData() {
        return tradeListData;
    }
    
    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final TradeService instance = new TradeService();
    }
   
    public static final TradeService getInstance() {
    	return SingletonHolder.instance;
    }

    
}
