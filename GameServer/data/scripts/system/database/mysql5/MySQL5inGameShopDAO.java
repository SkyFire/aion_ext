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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import gameserver.model.inGameShop.InGameShop;
import gameserver.model.inGameShop.InGameShopCategory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import gameserver.dao.InGameShopDAO;
import java.util.List;
import java.util.ArrayList;

/**
 * @author PZIKO333
 */

public class MySQL5inGameShopDAO extends InGameShopDAO {

    private static final Logger log = Logger.getLogger(MySQL5inGameShopDAO.class);
    public static final String SELECT_QUERY = "SELECT `object_id`, `item_id`, `item_count`, `item_price`, `category`, `list`, `sales_ranking`, `description` FROM `ingameshop`";
    public static final String SELECT_QUERY_CAT = "SELECT `object_id`, `item_id`, `item_count`, `item_price`, `category`, `list`, `sales_ranking`, `description` FROM `ingameshop` WHERE `category`=?";
    public static final String SELECT_IN_GAME_SHOP_QUERY = "SELECT `item_id`, `item_count`, `item_price`, `category`, `list`, `sales_ranking`, `description`  FROM `ingameshop` WHERE `object_id`=?";
    public static final String DELETE_QUERY = "DELETE FROM `ingameshop` WHERE `item_id`=? AND `category`=? AND `list`=?";
    public static final String QUERY = "SELECT `list` FROM `ingameshop` WHERE `category`=?";
    public static final String SELECT_SALES_RANKINK_QUERY = "SELECT `object_id`, `item_Id`, `item_count`, `item_price`, `description` FROM `ingameshop` WHERE `category`=? AND `list`=? AND `sales_ranking`=?";
    public static final String SELECT_CAT_QUERY = "SELECT `id`, `name` FROM `ingameshopcategorys`";
    public static final String INSERT_LOG = "INSERT INTO `ingameshoplog` (`account`, `character`, `receiver`, `item`, `count`, `price`) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String INSERT_ITEM = "INSERT INTO `ingameshop` (`object_id`, `item_id`, `item_count`, `item_price`, `category`, `list`, `sales_ranking`, `description`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public List<InGameShop> loadInGameShopSalesRanking(int category, int list, int salesRanking) {
        final List<InGameShop> inGameShopItems = new ArrayList<InGameShop>();

        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_SALES_RANKINK_QUERY);
            stmt.setInt(1, category);
            stmt.setInt(2, list);
            stmt.setInt(3, salesRanking);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                int objectId = rset.getInt("object_id");
                int itemId = rset.getInt("item_id");
                int itemCount = rset.getInt("item_count");
                int itemPrice = rset.getInt("item_price");
                String description = rset.getString("description");
                InGameShop inGameShop = new InGameShop(objectId, itemId, itemCount, itemPrice, category, list, salesRanking, description);
                inGameShopItems.add(inGameShop);
            }
            rset.close();
            stmt.close();
        } catch (Exception e) {
            log.fatal("Could not restore inGameShop data for category: " + category + "and list" + list + " from DB: " + e.getMessage(), e);
        } finally {
            DatabaseFactory.close(con);
        }
        return inGameShopItems;
    }

    @Override
    public int getMaxList(int category) {
        int list = 0;
        int maxValue = 0;
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(QUERY);
            stmt.setInt(1, category);

            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                list = rset.getInt("list");
                if (list > maxValue) {
                    maxValue = list;
                }
            }
            rset.close();
            stmt.close();
        } catch (Exception e) {
            log.fatal("Could not restore inGameShop data for category: " + " from DB: " + e.getMessage(), e);
        } finally {
            DatabaseFactory.close(con);
        }
        return maxValue;
    }

    @Override
    public List<InGameShop> loadInGameShopItems() {
        final List<InGameShop> inGameShopItems = new ArrayList<InGameShop>();

        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                int objectId = rset.getInt("object_id");
                int itemId = rset.getInt("item_id");
                int itemCount = rset.getInt("item_count");
                int itemPrice = rset.getInt("item_price");
                int category = rset.getInt("category");
                int list = rset.getInt("list");
                int salesRanking = rset.getInt("sales_ranking");
                String description = rset.getString("description");
                InGameShop inGameShop = new InGameShop(objectId, itemId, itemCount, itemPrice, category, list, salesRanking, description);
                if (category > 2) {
                    inGameShopItems.add(inGameShop);
                }
            }
            rset.close();
            stmt.close();
        } catch (Exception e) {
            log.fatal("Could not restore inGameShop data for all from DB: " + e.getMessage(), e);
        } finally {
            DatabaseFactory.close(con);
        }
        return inGameShopItems;
    }

    @Override
    public List<InGameShop> loadInGameShopItemsCat(int category) {
        final List<InGameShop> inGameShopItems = new ArrayList<InGameShop>();

        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_QUERY_CAT);
            stmt.setInt(1, category);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                int objectId = rset.getInt("object_id");
                int itemId = rset.getInt("item_id");
                int itemCount = rset.getInt("item_count");
                int itemPrice = rset.getInt("item_price");
                int list = rset.getInt("list");
                int salesRanking = rset.getInt("sales_ranking");
                String description = rset.getString("description");
                InGameShop inGameShop = new InGameShop(objectId, itemId, itemCount, itemPrice, category, list, salesRanking, description);
                inGameShopItems.add(inGameShop);
            }
            rset.close();
            stmt.close();
        } catch (Exception e) {
            log.fatal("Could not restore inGameShop data for all from DB: " + e.getMessage(), e);
        } finally {
            DatabaseFactory.close(con);
        }
        return inGameShopItems;
    }

    @Override
    public InGameShop loadInGameShopItem(int objectId) {
        InGameShop inGameShopItem = null;

        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_IN_GAME_SHOP_QUERY);
            stmt.setInt(1, objectId);

            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                int itemId = rset.getInt("item_id");
                int itemCount = rset.getInt("item_count");
                int itemPrice = rset.getInt("item_price");
                int category = rset.getInt("category");
                int list = rset.getInt("list");
                int salesRanking = rset.getInt("sales_ranking");
                String description = rset.getString("description");
                inGameShopItem = new InGameShop(objectId, itemId, itemCount, itemPrice, category, list, salesRanking, description);
            }
            rset.close();
            stmt.close();
        } catch (Exception e) {
            log.fatal("Could not restore inGameShop data for player: " + " from DB: " + e.getMessage(), e);
        } finally {
            DatabaseFactory.close(con);
        }
        return inGameShopItem;
    }

    @Override
    public boolean deleteIngameShopItem(int itemId, int category, int list) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
            stmt.setInt(1, itemId);
            stmt.setInt(2, category);
            stmt.setInt(3, list);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            log.error("Error delete ingameshopItem: " + itemId, e);
            return false;
        } finally {
            DatabaseFactory.close(con);
        }
        return true;
    }

    @Override
    public void saveIngameShopItem(int objectId, int itemId, int itemCount, int itemPrice, int category, int list, int salesRanking, String description) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(INSERT_ITEM);

            stmt.setInt(1, objectId);
            stmt.setInt(2, itemId);
            stmt.setInt(3, itemCount);
            stmt.setInt(4, itemPrice);
            stmt.setInt(5, category);
            stmt.setInt(6, list);
            stmt.setInt(7, salesRanking);
            stmt.setString(8, description);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            log.error("Error saving Item: " + objectId, e);
        } finally {
            DatabaseFactory.close(con);
        }
    }

    @Override
    public void saveIngameShopLog(String account, String character, String receiver, int item, int count, long price) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(INSERT_LOG);

            stmt.setString(1, account);
            stmt.setString(2, character);
            stmt.setString(3, receiver);
            stmt.setInt(4, item);
            stmt.setInt(5, count);
            stmt.setInt(6, (int)price);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            log.error("Error saving Log", e);
        } finally {
            DatabaseFactory.close(con);
        }
    }

    @Override
    public int[] getUsedIDs() {
        PreparedStatement statement = DB.prepareStatement("SELECT object_id FROM ingameshop", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        try {
            ResultSet rs = statement.executeQuery();
            rs.last();
            int count = rs.getRow();
            rs.beforeFirst();
            int[] ids = new int[count];
            for (int i = 0; i < count; i++) {
                rs.next();
                ids[i] = rs.getInt("object_id");
            }
            return ids;
        } catch (SQLException e) {
            log.error("Can't get list of id's from ingameshop table", e);
        } finally {
            DB.close(statement);
        }
        return new int[0];
    }

    @Override
    public List<InGameShopCategory> loadInGameShopCategory() {
        final List<InGameShopCategory> InGameShopCategorys = new ArrayList<InGameShopCategory>();

        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_CAT_QUERY);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                int Id = rset.getInt("id");
                String name = rset.getString("name");
                InGameShopCategory inGameShopCategory = new InGameShopCategory(Id, name);
                InGameShopCategorys.add(inGameShopCategory);
            }
            rset.close();
            stmt.close();
        } catch (Exception e) {
            log.fatal("Could not restore inGameShopCategory data for all from DB: " + e.getMessage(), e);
        } finally {
            DatabaseFactory.close(con);
        }
        return InGameShopCategorys;
    }

    @Override
    public boolean supports(String s, int i, int i1) {
        return MySQL5DAOUtils.supports(s, i, i1);
    }
}
