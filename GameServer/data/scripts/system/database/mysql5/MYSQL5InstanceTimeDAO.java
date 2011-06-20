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
import com.aionemu.commons.database.ParamReadStH;
import gameserver.dao.InstanceTimeDAO;
import gameserver.model.gameobjects.player.Player;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * AccountTime DAO implementation for MySQL5
 *
 * @author Arkshadow
 */
public class MYSQL5InstanceTimeDAO extends InstanceTimeDAO {
    private static final Logger log = Logger.getLogger(MYSQL5InstanceTimeDAO.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, Long> getTimes(final Player player) {
        final Map<Integer, Long> result = new HashMap<Integer, Long>();
        DB.select("SELECT * FROM `instance_time` WHERE `playerId` = ?", new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement ps) throws SQLException {
                ps.setInt(1, player.getObjectId());
            }

            @Override
            public void handleRead(ResultSet resultSet) throws SQLException {
                while (resultSet.next())
                    result.put(resultSet.getInt("instanceId"), resultSet.getLong("CheckIn"));
            }
        });
        return result;
    }

    @Override
    public boolean exists(final Player player, final int instanceId) {
        final Map<Integer, Long> result = new HashMap<Integer, Long>();
        DB.select("SELECT * FROM `instance_time` WHERE `playerId` = ? AND `InstanceId` = ?", new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement ps) throws SQLException {
                ps.setInt(1, player.getObjectId());
                ps.setInt(2, instanceId);
            }

            @Override
            public void handleRead(ResultSet resultSet) throws SQLException {
                while (resultSet.next())
                    result.put(resultSet.getInt("instanceId"), resultSet.getLong("CheckIn"));
            }
        });
        return !result.isEmpty();
    }

    @Override
    public boolean updateEntry(final int instanceId, final Player player, final int cd) {
        Connection con = null;
        boolean result = true;
        String query = "UPDATE `instance_time` SET `CheckIn` = ? WHERE `playerId` = ? AND `instanceId` = ?";
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            long time = Calendar.getInstance().getTimeInMillis();
            time += cd * 60 * 1000;
            stmt.setLong(1, time);
            stmt.setInt(2, player.getObjectId());
            stmt.setInt(3, instanceId);
            stmt.execute();
            stmt.close();
        }
        catch (Exception e) {
            log.error("No entry found for player " + player.getName() + " instanceId #" + String.valueOf(instanceId), e);
            result = false;
        }
        finally {
            DatabaseFactory.close(con);
        }
        return result;
    }

    @Override
    public boolean createEntry(final int instanceId, final Player player) {
        String query = "INSERT INTO `instance_time`(`playerId`, `instanceId`, `CheckIn`) VALUES (?, ?, ?)";
        Connection con = null;
        boolean result = true;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            long time = Calendar.getInstance().getTimeInMillis();
            stmt.setInt(1, player.getObjectId());
            stmt.setInt(2, instanceId);
            stmt.setLong(3, time);
            stmt.execute();
            stmt.close();
        }
        catch (Exception e) {
            log.error("Entry already exist for player " + player.getName() + " instanceId #" + String.valueOf(instanceId), e);
            result = false;
        }
        finally {
            DatabaseFactory.close(con);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(String s, int i, int i1) {
        return MySQL5DAOUtils.supports(s, i, i1);
    }
}
