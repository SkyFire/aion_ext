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

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.dao.InGameShopDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.inGameShop.InGameShop;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gnu.trove.TIntObjectHashMap;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PZIKO333
 */

public class SM_IN_GAME_SHOP_LIST extends AionServerPacket {
    private Player player;
    private int nrList;
    private int salesRanking;
    private TIntObjectHashMap<ArrayList<InGameShop>> allItems = new TIntObjectHashMap<ArrayList<InGameShop>>();

    public SM_IN_GAME_SHOP_LIST(Player player, int nrList, int salesRanking) {
        this.player = player;
        this.nrList = nrList;
        this.salesRanking = salesRanking;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        List<InGameShop> items = null;
        int category = player.getNrCategoryInGameShop();
        if (salesRanking == 1) {
            if (category == 2) {
                items = (DAOManager.getDAO(InGameShopDAO.class)).loadInGameShopItems();
            } else if (category > 2) {
                items = (DAOManager.getDAO(InGameShopDAO.class)).loadInGameShopItemsCat(category);
            }
            int i = 0;
            int r = 9;
            int f = 0;
            for (InGameShop a : items) {
                if (i == r) {
                    r += 9;
                    f++;
                }
                ArrayList<InGameShop> template = allItems.get(f);
                if (template == null) {
                    template = new ArrayList<InGameShop>();
                    allItems.put(f, template);
                }
                template.add(a);
                i++;
            }

            List<InGameShop> inAllItems = allItems.get(nrList);

            writeD(buf, salesRanking);
            writeD(buf, nrList);
            writeD(buf, items.size());
            writeH(buf, inAllItems == null ? 0 : inAllItems.size());
            if (inAllItems != null) {
                for (InGameShop item : inAllItems) {
                    writeD(buf, item.getObjectId());
                }
            }
        } else {
            List<InGameShop> salesRankingItems = (DAOManager.getDAO(InGameShopDAO.class)).loadInGameShopSalesRanking(salesRanking == 1 ? category : -1, salesRanking == 1 ? nrList : -1, salesRanking);

            writeD(buf, salesRanking);
            writeD(buf, nrList);
            writeD(buf, ((DAOManager.getDAO(InGameShopDAO.class)).getMaxList(category) + 1) * 9);
            writeH(buf, salesRankingItems.size());

            for (InGameShop item : salesRankingItems) {
                writeD(buf, item.getObjectId());
            }
        }
    }
}