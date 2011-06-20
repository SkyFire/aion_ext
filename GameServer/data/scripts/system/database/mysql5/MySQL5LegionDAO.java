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
import com.aionemu.commons.database.ParamReadStH;
import gameserver.dao.LegionDAO;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.StorageType;
import gameserver.model.legion.*;
import gameserver.services.RentalService;

import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Class that that is responsible for loading/storing {@link gameserver.model.legion.Legion} object from
 * MySQL 5.
 *
 * @author Simple
 */
public class MySQL5LegionDAO extends LegionDAO {
    /**
     * Logger
     */
    private static final Logger log = Logger.getLogger(MySQL5LegionDAO.class);

    /**
     * Legion Queries
     */
    private static final String INSERT_LEGION_QUERY = "INSERT INTO legions(id, `name`) VALUES (?, ?)";
    private static final String SELECT_LEGION_QUERY1 = "SELECT * FROM legions WHERE id=?";
    private static final String SELECT_LEGION_QUERY2 = "SELECT * FROM legions WHERE name=?";
    private static final String DELETE_LEGION_QUERY = "DELETE FROM legions WHERE id = ?";
    private static final String UPDATE_LEGION_QUERY = "UPDATE legions SET name=?, level=?, contribution_points=?, legionar_permission2=?, centurion_permission1=?, centurion_permission2=?, disband_time=? WHERE id=?";

    /**
     * Legion Ranking Queries *
     */
    private static final String SELECT_LEGIONRANKING_QUERY = "SELECT id, contribution_points FROM legions ORDER BY contribution_points DESC;";

    /**
     * Announcement Queries *
     */
    private static final String INSERT_ANNOUNCEMENT_QUERY = "INSERT INTO legion_announcement_list(`legion_id`, `announcement`, `date`) VALUES (?, ?, ?)";
    private static final String SELECT_ANNOUNCEMENTLIST_QUERY = "SELECT * FROM legion_announcement_list WHERE legion_id=? ORDER BY date ASC LIMIT 0,7;";
    private static final String DELETE_ANNOUNCEMENT_QUERY = "DELETE FROM legion_announcement_list WHERE legion_id = ? AND date = ?";

    /**
     * Emblem Queries *
     */
    private static final String INSERT_EMBLEM_QUERY = "INSERT INTO legion_emblems(legion_id, emblem_ver, color_r, color_g, color_b, custom, emblem_data) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_EMBLEM_QUERY = "UPDATE legion_emblems SET emblem_ver=?, color_r=?, color_g=?, color_b=?, custom=?, emblem_data=? WHERE legion_id=?";
    private static final String SELECT_EMBLEM_QUERY = "SELECT * FROM legion_emblems WHERE legion_id=?";

    /**
     * Storage Queries *
     */
    private static final String SELECT_STORAGE_QUERY = "SELECT `itemUniqueId`, `itemId`, `itemCount`, `itemColor`, `isEquiped`, `slot`, `enchant`, `itemCreator`, `itemSkin`, `fusionedItem`, `optionalSocket`, `optionalFusionSocket`, `expireTime` FROM `inventory` WHERE `itemOwner`=? AND `itemLocation`=? AND `isEquiped`=?";

    /**
     * History Queries *
     */
    private static final String INSERT_HISTORY_QUERY = "INSERT INTO legion_history(`legion_id`, `date`, `history_type`, `name`) VALUES (?, ?, ?, ?)";
    private static final String SELECT_HISTORY_QUERY = "SELECT * FROM `legion_history` WHERE legion_id=? ORDER BY date ASC;";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNameUsed(final String name) {
        PreparedStatement s = DB.prepareStatement("SELECT count(id) as cnt FROM legions WHERE ? = legions.name");
        try {
            s.setString(1, name);
            ResultSet rs = s.executeQuery();
            rs.next();
            return rs.getInt("cnt") > 0;
        }
        catch (SQLException e) {
            log.error("Can't check if name " + name + ", is used, returning possitive result", e);
            return true;
        }
        finally {
            DB.close(s);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveNewLegion(final Legion legion) {
        boolean success = DB.insertUpdate(INSERT_LEGION_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
                log.debug("[DAO: MySQL5LegionDAO] saving new legion: " + legion.getLegionId() + " "
                        + legion.getLegionName());

                preparedStatement.setInt(1, legion.getLegionId());
                preparedStatement.setString(2, legion.getLegionName());
                preparedStatement.execute();
            }
        });
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeLegion(final Legion legion) {
        DB.insertUpdate(UPDATE_LEGION_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                log.debug("[DAO: MySQL5LegionDAO] storing player " + legion.getLegionId() + " "
                        + legion.getLegionName());

                stmt.setString(1, legion.getLegionName());
                stmt.setInt(2, legion.getLegionLevel());
                stmt.setInt(3, legion.getContributionPoints());
                stmt.setInt(4, legion.getLegionarPermission2());
                stmt.setInt(5, legion.getCenturionPermission1());
                stmt.setInt(6, legion.getCenturionPermission2());
                stmt.setInt(7, legion.getDisbandTime());
                stmt.setInt(8, legion.getLegionId());
                stmt.execute();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Legion loadLegion(final String legionName) {
        final Legion legion = new Legion();

        boolean success = DB.select(SELECT_LEGION_QUERY2, new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement stmt) throws SQLException {
                stmt.setString(1, legionName);
            }

            @Override
            public void handleRead(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    legion.setLegionName(legionName);
                    legion.setLegionId(resultSet.getInt("id"));
                    legion.setLegionLevel(resultSet.getInt("level"));
                    legion.addContributionPoints(resultSet.getInt("contribution_points"));

                    legion.setLegionPermissions(resultSet.getInt("legionar_permission2"), resultSet
                            .getInt("centurion_permission1"), resultSet.getInt("centurion_permission2"));

                    legion.setDisbandTime(resultSet.getInt("disband_time"));
                }
            }
        });

        log.debug("[MySQL5LegionDAO] Loaded " + legion.getLegionId() + " legion.");

        return (success && legion.getLegionId() != 0) ? legion : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Legion loadLegion(final int legionId) {
        final Legion legion = new Legion();

        boolean success = DB.select(SELECT_LEGION_QUERY1, new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, legionId);
            }

            @Override
            public void handleRead(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    legion.setLegionId(legionId);
                    legion.setLegionName(resultSet.getString("name"));
                    legion.setLegionLevel(resultSet.getInt("level"));
                    legion.addContributionPoints(resultSet.getInt("contribution_points"));

                    legion.setLegionPermissions(resultSet.getInt("legionar_permission2"), resultSet
                            .getInt("centurion_permission1"), resultSet.getInt("centurion_permission2"));

                    legion.setDisbandTime(resultSet.getInt("disband_time"));
                }
            }
        });

        log.debug("[MySQL5LegionDAO] Loaded " + legion.getLegionId() + " legion.");

        return (success && legion.getLegionName() != "") ? legion : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteLegion(int legionId) {
        PreparedStatement statement = DB.prepareStatement(DELETE_LEGION_QUERY);
        try {
            statement.setInt(1, legionId);
        }
        catch (SQLException e) {
            log.error("Some crap, can't set int parameter to PreparedStatement", e);
        }
        DB.executeUpdateAndClose(statement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getUsedIDs() {
        PreparedStatement statement = DB.prepareStatement("SELECT id FROM legions", ResultSet.TYPE_SCROLL_INSENSITIVE,
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
            log.error("Can't get list of id's from legions table", e);
        }
        finally {
            DB.close(statement);
        }

        return new int[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(String s, int i, int i1) {
        return MySQL5DAOUtils.supports(s, i, i1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeMap<Timestamp, String> loadAnnouncementList(final int legionId) {
        final TreeMap<Timestamp, String> announcementList = new TreeMap<Timestamp, String>();

        boolean success = DB.select(SELECT_ANNOUNCEMENTLIST_QUERY, new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, legionId);
            }

            @Override
            public void handleRead(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    String message = resultSet.getString("announcement");
                    Timestamp date = resultSet.getTimestamp("date");

                    announcementList.put(date, message);
                }
            }
        });

        log.debug("[MySQL5LegionDAO] Loaded announcementList " + legionId + " legion.");

        return success ? announcementList : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveNewAnnouncement(final int legionId, final Timestamp currentTime, final String message) {
        if (!isLegionIdUsed(legionId))
            return false;

        boolean success = DB.insertUpdate(INSERT_ANNOUNCEMENT_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
                log.debug("[DAO: MySQL5LegionDAO] saving new announcement.");

                preparedStatement.setInt(1, legionId);
                preparedStatement.setString(2, message);
                preparedStatement.setTimestamp(3, currentTime);
                preparedStatement.execute();
            }
        });
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAnnouncement(int legionId, Timestamp unixTime) {
        PreparedStatement statement = DB.prepareStatement(DELETE_ANNOUNCEMENT_QUERY);
        try {
            statement.setInt(1, legionId);
            statement.setTimestamp(2, unixTime);
        }
        catch (SQLException e) {
            log.error("Some crap, can't set int parameter to PreparedStatement", e);
        }
        DB.executeUpdateAndClose(statement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeLegionEmblem(final int legionId, final LegionEmblem legionEmblem) {
        switch (legionEmblem.getPersistentState()) {
            case UPDATE_REQUIRED:
                updateLegionEmblem(legionId, legionEmblem);
                break;
            case NEW:
                createLegionEmblem(legionId, legionEmblem);
                break;
        }
        legionEmblem.setPersistentState(PersistentState.UPDATED);
    }

    /**
     * @param legionId
     * @param legionEmblem
     * @return
     */
    private void createLegionEmblem(final int legionId, final LegionEmblem legionEmblem) {
        DB.insertUpdate(INSERT_EMBLEM_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, legionId);
                preparedStatement.setInt(2, legionEmblem.getEmblemVer());
                preparedStatement.setInt(3, legionEmblem.getColor_r());
                preparedStatement.setInt(4, legionEmblem.getColor_g());
                preparedStatement.setInt(5, legionEmblem.getColor_b());
                preparedStatement.setBoolean(6, legionEmblem.getIsCustom());
                preparedStatement.setBytes(7, legionEmblem.getCustomEmblemData());
                preparedStatement.execute();
            }
        });
    }

    /**
     * @param legionId
     * @param legionEmblem
     */
    private void updateLegionEmblem(final int legionId, final LegionEmblem legionEmblem) {
        DB.insertUpdate(UPDATE_EMBLEM_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, legionEmblem.getEmblemVer());
                stmt.setInt(2, legionEmblem.getColor_r());
                stmt.setInt(3, legionEmblem.getColor_g());
                stmt.setInt(4, legionEmblem.getColor_b());
                stmt.setBoolean(5, legionEmblem.getIsCustom());
                stmt.setBytes(6, legionEmblem.getCustomEmblemData());
                stmt.setInt(7, legionId);
                stmt.execute();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LegionEmblem loadLegionEmblem(final int legionId) {
        final LegionEmblem legionEmblem = new LegionEmblem();

        DB.select(SELECT_EMBLEM_QUERY, new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, legionId);
            }

            @Override
            public void handleRead(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    legionEmblem.setEmblem(resultSet.getInt("emblem_ver"), resultSet.getInt("color_r"), resultSet
                            .getInt("color_g"), resultSet.getInt("color_b"), resultSet.getBoolean("custom"), resultSet.getBytes("emblem_data"));
                }
            }
        });
        legionEmblem.setPersistentState(PersistentState.UPDATED);

        return legionEmblem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LegionWarehouse loadLegionStorage(Legion legion) {
        final LegionWarehouse inventory = new LegionWarehouse(legion);
        final int legionId = legion.getLegionId();
        final int storage = StorageType.LEGION_WAREHOUSE.getId();
        final int equipped = 0;

        DB.select(SELECT_STORAGE_QUERY, new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, legionId);
                stmt.setInt(2, storage);
                stmt.setInt(3, equipped);
            }

            @Override
            public void handleRead(ResultSet rset) throws SQLException {
                while (rset.next()) {
                    int itemUniqueId = rset.getInt("itemUniqueId");
                    int itemId = rset.getInt("itemId");
                    int itemCount = rset.getInt("itemCount");
                    int itemColor = rset.getInt("itemColor");
                    int isEquiped = rset.getInt("isEquiped");
                    int slot = rset.getInt("slot");
                    int enchant = rset.getInt("enchant");
                    int itemSkin = rset.getInt("itemSkin");
                    int fusionedItem = rset.getInt("fusionedItem");
                    int optionalSocket = rset.getInt("optionalSocket");
                    int optionalFusionSocket = rset.getInt("optionalFusionSocket");
                    Timestamp expireTime = rset.getTimestamp("expireTime");
                    String itemCreator = rset.getString("itemCreator");
                    Item item = new Item(itemUniqueId, itemId, itemCount,
                        itemColor, itemCreator, (isEquiped == 1), false, slot,
                        storage, enchant, itemSkin, fusionedItem,
                        optionalSocket, optionalFusionSocket, expireTime);
                    item.setPersistentState(PersistentState.UPDATED);
                    inventory.onLoadHandler(item);
                }
            }
        });
        return inventory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<Integer, Integer> loadLegionRanking() {
        final HashMap<Integer, Integer> legionRanking = new HashMap<Integer, Integer>();

        DB.select(SELECT_LEGIONRANKING_QUERY, new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement stmt) throws SQLException {
            }

            @Override
            public void handleRead(ResultSet resultSet) throws SQLException {
                int i = 1;
                while (resultSet.next()) {
                    if (resultSet.getInt("contribution_points") > 0) {
                        legionRanking.put(resultSet.getInt("id"), i);
                        i++;
                    } else
                        legionRanking.put(resultSet.getInt("id"), 0);
                }
            }
        });

        return legionRanking;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadLegionHistory(final Legion legion) {

        final Collection<LegionHistory> history = legion.getLegionHistory();

        DB.select(SELECT_HISTORY_QUERY, new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, legion.getLegionId());
            }

            @Override
            public void handleRead(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    history.add(new LegionHistory(LegionHistoryType.valueOf(resultSet.getString("history_type")),
                            resultSet.getString("name"), resultSet.getTimestamp("date")));
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveNewLegionHistory(final int legionId, final LegionHistory legionHistory) {
        boolean success = DB.insertUpdate(INSERT_HISTORY_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, legionId);
                preparedStatement.setTimestamp(2, legionHistory.getTime());
                preparedStatement.setString(3, legionHistory.getLegionHistoryType().toString());
                preparedStatement.setString(4, legionHistory.getName());
                preparedStatement.execute();
            }
        });
        return success;
    }

    /**
     * {@inheritDoc}
     */
    private boolean isLegionIdUsed(final int legionId) {
        PreparedStatement s = DB.prepareStatement("SELECT id FROM legions WHERE id=?");
        try {
            s.setInt(1, legionId);
            ResultSet rs = s.executeQuery();
            rs.next();
            return rs.getInt("id") > 0;
        }
        catch (SQLException e) {
            log.error("Can't check if id " + legionId + ", is used. ", e);
            return false;
        }
        finally {
            DB.close(s);
        }
    }
}
