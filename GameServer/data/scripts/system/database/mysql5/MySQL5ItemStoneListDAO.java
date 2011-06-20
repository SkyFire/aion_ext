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
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.IUStH;
import gameserver.dao.ItemStoneListDAO;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.items.FusionStone;
import gameserver.model.items.GodStone;
import gameserver.model.items.ItemStone.ItemStoneType;
import gameserver.model.items.ManaStone;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author ATracer
 */
public class MySQL5ItemStoneListDAO extends ItemStoneListDAO {
    private static final Logger log = Logger.getLogger(MySQL5ItemStoneListDAO.class);

    public static final String INSERT_QUERY = "INSERT INTO `item_stones` (`itemUniqueId`, `itemId`, `slot`, `category`) VALUES (?,?,?, ?)";
    public static final String UPDATE_QUERY = "UPDATE `item_stones` SET `itemId`=? where `itemUniqueId`=? AND `category`=?";
    public static final String DELETE_QUERY = "DELETE FROM `item_stones` WHERE `itemUniqueId`=? AND slot=? AND category=?";
    public static final String SELECT_QUERY = "SELECT `itemId`, `slot`, `category` FROM `item_stones` WHERE `itemUniqueId`=?";


    @Override
    public void load(final List<Item> items) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
            for (Item item : items) {
                if (item.getItemTemplate().isArmor() || item.getItemTemplate().isWeapon()) {
                    stmt.setInt(1, item.getObjectId());
                    ResultSet rset = stmt.executeQuery();
                    while (rset.next()) {
                        int itemId = rset.getInt("itemId");
                        int slot = rset.getInt("slot");
                        int stoneType = rset.getInt("category");
                        if (stoneType == 0)
                            item.getItemStones().add(new ManaStone(item.getObjectId(), itemId, slot, PersistentState.UPDATED));
                        else if (stoneType == 1)
                            item.setGoodStone(new GodStone(item.getObjectId(), itemId, PersistentState.UPDATED));
                        else
                            item.getFusionStones().add(new FusionStone(item.getObjectId(), itemId, slot, PersistentState.UPDATED));
                    }
                    rset.close();
                }
            }
            stmt.close();
        }
        catch (Exception e) {
            log.fatal("Could not restore ItemStoneList data from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
        }
    }

    @Override
    public void save(Player player) {
        List<Item> allPlayerItems = player.getAllItems();

        for (Item item : allPlayerItems) {
            if (item.hasManaStones())
                storeManaStone(item.getItemStones());

            if (item.hasFusionStones())
                storeFusionStone(item.getFusionStones());

            GodStone godStone = item.getGodStone();
            store(godStone);
        }
    }

    @Override
    public void storeManaStone(final Set<ManaStone> manaStones) {
        if (manaStones == null)
            return;

        Iterator<ManaStone> iterator = manaStones.iterator();
        while (iterator.hasNext()) {
            ManaStone manaStone = iterator.next();
            switch (manaStone.getPersistentState()) {
                case NEW:
                    addItemStone(manaStone.getItemObjId(), manaStone.getItemId(),
                            manaStone.getSlot());
                    break;
                case UPDATE_REQUIRED:
                    updateItemStone(manaStone.getItemObjId(), manaStone.getItemId());
                    break;
                case DELETED:
                    deleteItemStone(manaStone.getItemObjId(), manaStone.getSlot());
                    break;

            }
            manaStone.setPersistentState(PersistentState.UPDATED);
        }
    }

    @Override
    public void storeFusionStone(final Set<FusionStone> manaStones) {
        if (manaStones == null)
            return;

        Iterator<FusionStone> iterator = manaStones.iterator();
        while (iterator.hasNext()) {
            FusionStone manaStone = iterator.next();
            switch (manaStone.getPersistentState()) {
                case NEW:
                    addFusionStone(manaStone.getItemObjId(), manaStone.getItemId(),
                            manaStone.getSlot());
                    break;
                case DELETED:
                    deleteFusionStone(manaStone.getItemObjId(), manaStone.getSlot());
                    break;

            }
            manaStone.setPersistentState(PersistentState.UPDATED);
        }
    }

    /**
     * @param godstone
     */
    @Override
    public void store(GodStone godstone) {
        if (godstone == null)
            return;

        switch (godstone.getPersistentState()) {
            case NEW:
                addGodStone(godstone.getItemObjId(), godstone.getItemId(),
                        godstone.getSlot());
                break;
            case UPDATE_REQUIRED:
                updateGodStone(godstone.getItemObjId(), godstone.getItemId());
                break;
            case DELETED:
                deleteGodStone(godstone.getItemObjId(), godstone.getSlot());
                break;
        }
        godstone.setPersistentState(PersistentState.UPDATED);
    }

    /**
     * Adds new item stone to item
     *
     * @param itemObjId
     * @param itemId
     * @param statEnum
     * @param statValue
     * @param slot
     */
    private void addItemStone(final int itemObjId, final int itemId, final int slot) {
        DB.insertUpdate(INSERT_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, itemObjId);
                stmt.setInt(2, itemId);
                stmt.setInt(3, slot);
                stmt.setInt(4, ItemStoneType.MANASTONE.ordinal());
                stmt.execute();
            }
        });
    }

    /**
     * @param itemObjId
     * @param itemId
     */
    private void updateItemStone(final int itemObjId, final int itemId) {
        DB.insertUpdate(UPDATE_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, itemId);
                stmt.setInt(2, itemObjId);
                stmt.setInt(3, ItemStoneType.MANASTONE.ordinal());
                stmt.execute();
            }
        });
    }

    /**
     * Adds new fusion item stone to item
     *
     * @param itemObjId
     * @param itemId
     * @param statEnum
     * @param statValue
     * @param slot
     */
    private void addFusionStone(final int itemObjId, final int itemId, final int slot) {
        DB.insertUpdate(INSERT_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, itemObjId);
                stmt.setInt(2, itemId);
                stmt.setInt(3, slot);
                stmt.setInt(4, ItemStoneType.FUSIONSTONE.ordinal());
                stmt.execute();
            }
        });
    }

    /**
     * @param itemObjId
     * @param itemId
     * @param slot
     */
    private void addGodStone(final int itemObjId, final int itemId, final int slot) {
        DB.insertUpdate(INSERT_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, itemObjId);
                stmt.setInt(2, itemId);
                stmt.setInt(3, slot);
                stmt.setInt(4, ItemStoneType.GODSTONE.ordinal());
                stmt.execute();
            }
        });
    }

    /**
     * @param itemObjId
     * @param itemId
     */
    private void updateGodStone(final int itemObjId, final int itemId) {
        DB.insertUpdate(UPDATE_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, itemId);
                stmt.setInt(2, itemObjId);
                stmt.setInt(3, ItemStoneType.GODSTONE.ordinal());
                stmt.execute();
            }
        });
    }

    /**
     * Deleted item stone from selected item
     *
     * @param itemObjId
     * @param slot
     */
    private void deleteItemStone(final int itemObjId, final int slot) {
        DB.insertUpdate(DELETE_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, itemObjId);
                stmt.setInt(2, slot);
                stmt.setInt(3, ItemStoneType.MANASTONE.ordinal());
                stmt.execute();
            }
        });
    }

    /**
     * Deleted item stone from selected item
     *
     * @param itemObjId
     * @param slot
     */
    private void deleteFusionStone(final int itemObjId, final int slot) {
        DB.insertUpdate(DELETE_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, itemObjId);
                stmt.setInt(2, slot);
                stmt.setInt(3, ItemStoneType.FUSIONSTONE.ordinal());
                stmt.execute();
            }
        });
    }

    /**
     * @param itemObjId
     * @param slot
     */
    private void deleteGodStone(final int itemObjId, final int slot) {
        DB.insertUpdate(DELETE_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, itemObjId);
                stmt.setInt(2, slot);
                stmt.setInt(3, ItemStoneType.GODSTONE.ordinal());
                stmt.execute();
            }
        });
    }

    @Override
    public boolean supports(String s, int i, int i1) {
        return MySQL5DAOUtils.supports(s, i, i1);
    }

}
