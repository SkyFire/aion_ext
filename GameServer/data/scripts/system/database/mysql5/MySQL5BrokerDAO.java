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

package mysql5;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ReadStH;
import com.aionemu.commons.database.dao.DAOManager;
import gameserver.dao.BrokerDAO;
import gameserver.dao.InventoryDAO;
import gameserver.model.broker.BrokerRace;
import gameserver.model.gameobjects.BrokerItem;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.PersistentState;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MySQL5BrokerDAO extends BrokerDAO {
    private static final Logger log = Logger.getLogger(MySQL5BrokerDAO.class);

    @Override
    public List<BrokerItem> loadBroker() {
        final List<BrokerItem> brokerItems = new ArrayList<BrokerItem>();

        final List<Item> items = getBrokerItems();

        DB.select("SELECT * FROM broker", new ReadStH() {

            @Override
            public void handleRead(ResultSet rset) throws SQLException {
                while (rset.next()) {
                    int itemPointer = rset.getInt("itemPointer");
                    int itemId = rset.getInt("itemId");
                    long itemCount = rset.getLong("itemCount");
                    String seller = rset.getString("seller");
                    int sellerId = rset.getInt("sellerId");
                    long price = rset.getLong("price");
                    BrokerRace itemBrokerRace = BrokerRace.valueOf(rset.getString("brokerRace"));
                    Timestamp expireTime = rset.getTimestamp("expireTime");
                    Timestamp settleTime = rset.getTimestamp("settleTime");
                    int sold = rset.getInt("isSold");
                    int settled = rset.getInt("isSettled");

                    boolean isSold = sold == 1;
                    boolean isSettled = settled == 1;

                    Item item = null;
                    if (!isSold)
                        for (Item brItem : items) {
                            if (itemPointer == brItem.getObjectId()) {
                                item = brItem;
                                break;
                            }
                        }

                    brokerItems.add(new BrokerItem(item, itemId, itemPointer, itemCount, price, seller, sellerId,
                            itemBrokerRace, isSold, isSettled, expireTime, settleTime));
                }
            }
        });

        return brokerItems;
    }

    private List<Item> getBrokerItems() {
        final List<Item> brokerItems = new ArrayList<Item>();

        DB.select("SELECT * FROM inventory WHERE `itemLocation` = 126", new ReadStH() {

            @Override
            public void handleRead(ResultSet rset) throws SQLException {
                while (rset.next()) {
                    int itemUniqueId = rset.getInt("itemUniqueId");
                    int itemId = rset.getInt("itemId");
                    long itemCount = rset.getLong("itemCount");
                    int itemColor = rset.getInt("itemColor");
                    int slot = rset.getInt("slot");
                    int location = rset.getInt("itemLocation");
                    int enchant = rset.getInt("enchant");
                    int itemSkin = rset.getInt("itemSkin");
                    int fusionedItem = rset.getInt("fusionedItem");
                    int optionalSocket = rset.getInt("optionalSocket");
                    int optionalFusionSocket = rset.getInt("optionalFusionSocket");
                    Timestamp expireTime = rset.getTimestamp("expireTime");
                    String itemCreator = rset.getString("itemCreator");
                    brokerItems.add(new Item(itemUniqueId, itemId, itemCount,
                        itemColor, itemCreator, false, false, slot, location,
                        enchant, itemSkin, fusionedItem, optionalSocket,
                        optionalFusionSocket, expireTime));
                }
            }
        });

        return brokerItems;
    }

    @Override
    public boolean store(BrokerItem item) {
        boolean result = false;

        if (item == null) {
            log.warn("Null broker item on save");
            return result;
        }

        switch (item.getPersistentState()) {
            case NEW:
                result = insertBrokerItem(item);
                if (item.getItem() != null)
                    DAOManager.getDAO(InventoryDAO.class).store(item.getItem(), item.getSellerId());
                break;

            case DELETED:
                result = deleteBrokerItem(item);
                break;

            case UPDATE_REQUIRED:
                result = updateBrokerItem(item);
                break;
        }

        if (result)
            item.setPersistentState(PersistentState.UPDATED);

        return result;
    }

    private boolean insertBrokerItem(final BrokerItem item) {
        boolean result = DB
                .insertUpdate(
                        "INSERT INTO `broker` (`itemPointer`, `itemId`, `itemCount`,`seller`, `price`, `brokerRace`, `expireTime`, `sellerId`, `isSold`, `isSettled`) VALUES (?,?,?,?,?,?,?,?,?,?)",
                        new IUStH() {
                            @Override
                            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                                stmt.setInt(1, item.getItemUniqueId());
                                stmt.setInt(2, item.getItemId());
                                stmt.setLong(3, item.getItemCount());
                                stmt.setString(4, item.getSeller());
                                stmt.setLong(5, item.getPrice());
                                stmt.setString(6, String.valueOf(item.getItemBrokerRace()));
                                stmt.setTimestamp(7, item.getExpireTime());
                                stmt.setInt(8, item.getSellerId());
                                stmt.setBoolean(9, item.isSold());
                                stmt.setBoolean(10, item.isSettled());

                                stmt.execute();
                            }
                        });

        return result;
    }

    private boolean deleteBrokerItem(final BrokerItem item) {
        boolean result = DB.insertUpdate(
                "DELETE FROM `broker` WHERE `itemPointer` = ? AND `sellerId` = ? AND `expireTime` = ?", new IUStH() {
                    @Override
                    public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                        stmt.setInt(1, item.getItemUniqueId());
                        stmt.setInt(2, item.getSellerId());
                        stmt.setTimestamp(3, item.getExpireTime());

                        stmt.execute();
                    }
                });

        return result;
    }

    private boolean updateBrokerItem(final BrokerItem item) {
        boolean result = DB
                .insertUpdate(
                        "UPDATE broker SET `isSold` = ?, `isSettled` = 1, `settleTime` = ? WHERE `itemPointer` = ? AND `expireTime` = ? AND `sellerId` = ? AND `isSettled` = 0",
                        new IUStH() {
                            @Override
                            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                                stmt.setBoolean(1, item.isSold());
                                stmt.setTimestamp(2, item.getSettleTime());
                                stmt.setInt(3, item.getItemUniqueId());
                                stmt.setTimestamp(4, item.getExpireTime());
                                stmt.setInt(5, item.getSellerId());

                                stmt.execute();
                            }
                        });

        return result;
    }

    @Override
    public int[] getUsedIDs() {
        PreparedStatement statement = DB.prepareStatement("SELECT id FROM players", ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        try {
            ResultSet rs = statement.executeQuery();
            rs.last();
            int count = rs.getRow();
            rs.beforeFirst();
            int[] ids = new int[count];
            for (int i = 0; i < count; i++) {
                rs.next();
                ids[i] = rs.getInt("id");
            }
            return ids;
        }
        catch (SQLException e) {
            log.error("Can't get list of id's from players table", e);
        }
        finally {
            DB.close(statement);
        }

        return new int[0];
    }

    public boolean supports(String s, int i, int i1) {
        return MySQL5DAOUtils.supports(s, i, i1);
    }
}
