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

package gameserver.network.aion.serverpackets;


import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.TradeListTemplate;
import gameserver.model.templates.TradeListTemplate.TradeTab;
import gameserver.model.templates.goods.GoodsList;
import gameserver.services.TradeService;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;


/**
 * @author alexa026
 *         modified by ATracer, Sarynth
 */
public class SM_TRADELIST extends AionServerPacket {

    private int targetObjectId;
    private int npcTemplateId;
    private TradeListTemplate tlist;
    private int buyPriceModifier;
    private Player player;
    
    public SM_TRADELIST(Npc npc, TradeListTemplate tlist, int buyPriceModifier, Player player) {
        this.targetObjectId = npc.getObjectId();
        this.npcTemplateId = npc.getNpcId();
        this.tlist = tlist;
        this.buyPriceModifier = buyPriceModifier;
        this.player = player;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        List<GoodsList.Item> limitedItems = new ArrayList<GoodsList.Item>();
        if ((tlist != null) && (tlist.getNpcId() != 0) && (tlist.getCount() != 0)) {
            writeD(buf, targetObjectId);
            //writeC(buf, tlist.isAbyss() ? 2 : 1); //abyss or normal
            writeC(buf, tlist.getCategory() + 1);
            writeD(buf, buyPriceModifier); // Vendor Buy Price Modifier
            writeH(buf, tlist.getCount());
            for (TradeTab tradeTabl : tlist.getTradeTablist()) {
                writeD(buf, tradeTabl.getId());
                GoodsList goodsList = DataManager.GOODSLIST_DATA.getGoodsListById(tradeTabl.getId());
                if(goodsList == null || goodsList.getItemList() == null)
                    continue;
                for (GoodsList.Item item : goodsList.getItemList()) {
                    if (item.isLimited()) {
                        limitedItems.add(item);
                    }
                }
            }
            
            if(!limitedItems.isEmpty()) {
                writeH(buf, limitedItems.size());
                for (GoodsList.Item item : limitedItems) {
                    writeD(buf, item.getId());
                    writeH(buf, TradeService.getInstance().getCountItemSoldToPlayer(npcTemplateId, player.getCommonData().getPlayerObjId(), item.getId())); //amount sold to player
                    writeH(buf, TradeService.getInstance().getItemStock(npcTemplateId, item.getId(), item.getSellLimit())); //amount left
                }
            }
        } else if (tlist == null) {
            Logger.getLogger(SM_TRADELIST.class).warn("Empty TradeListTemplate for NpcId: " + npcTemplateId);
            writeD(buf, targetObjectId);
            writeC(buf, 1);
            writeD(buf, buyPriceModifier);
            writeH(buf, 0);
        }
    }
}
