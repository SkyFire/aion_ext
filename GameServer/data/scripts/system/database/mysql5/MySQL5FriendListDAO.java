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
import com.aionemu.commons.database.dao.DAOManager;
import gameserver.dao.FriendListDAO;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.player.Friend;
import gameserver.model.gameobjects.player.FriendList;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ben
 */
public class MySQL5FriendListDAO extends FriendListDAO {
    private static final Logger log = Logger.getLogger(MySQL5FriendListDAO.class);

    public static final String LOAD_QUERY = "SELECT * FROM `friends` WHERE `player`=?";

    public static final String ADD_QUERY = "INSERT INTO `friends` (`player`,`friend`) VALUES (?, ?)";

    public static final String DEL_QUERY = "DELETE FROM friends WHERE player = ? AND friend = ?";

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.dao.FriendListDAO#load(com.aionemu.gameserver.model.gameobjects.player.Player)
      */

    @Override
    public FriendList load(final Player player) {
        final List<Friend> friends = new ArrayList<Friend>();
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(LOAD_QUERY);
            stmt.setInt(1, player.getObjectId());
            ResultSet rset = stmt.executeQuery();
            PlayerDAO dao = DAOManager.getDAO(PlayerDAO.class);
            while (rset.next()) {
                int objId = rset.getInt("friend");

                PlayerCommonData pcd = dao.loadPlayerCommonData(objId);
                if (pcd != null) {
                    Friend friend = new Friend(pcd);
                    friends.add(friend);
                }
            }
        }
        catch (Exception e) {
            log.fatal("Could not restore QuestStateList data for player: " + player.getObjectId() + " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
        }

        return new FriendList(player, friends);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addFriends(final Player player, final Player friend) {
        return DB.insertUpdate(ADD_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
                ps.setInt(1, player.getObjectId());
                ps.setInt(2, friend.getObjectId());
                ps.addBatch();

                ps.setInt(1, friend.getObjectId());
                ps.setInt(2, player.getObjectId());
                ps.addBatch();

                ps.executeBatch();
            }
        });

    }

    @Override
    public boolean delFriends(final int playerOid, final int friendOid) {
        return DB.insertUpdate(DEL_QUERY, new IUStH() {

            @Override
            public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
                ps.setInt(1, playerOid);
                ps.setInt(2, friendOid);
                ps.addBatch();

                ps.setInt(1, friendOid);
                ps.setInt(2, playerOid);
                ps.addBatch();

                ps.executeBatch();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(String s, int i, int i1) {
        return MySQL5DAOUtils.supports(s, i, i1);
    }
}
