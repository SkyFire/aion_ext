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
import gameserver.dao.PlayerPunishmentsDAO;
import gameserver.model.gameobjects.player.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lord_rex
 */
public class MySQL5PlayerPunishmentsDAO extends PlayerPunishmentsDAO {
    public static final String SELECT_QUERY = "SELECT `player_id`, `punishment_status`, `punishment_timer` FROM `player_punishments` WHERE `player_id`=?";
    public static final String UPDATE_QUERY = "UPDATE `player_punishments` SET `punishment_status`=?, `punishment_timer`=? WHERE `player_id`=?";
    public static final String REPLACE_QUERY = "REPLACE INTO `player_punishments` VALUES (?,?,?)";
    public static final String DELETE_QUERY = "DELETE FROM `player_punishments` WHERE `player_id`=?";

    @Override
    public void loadPlayerPunishments(final Player player) {
        DB.select(SELECT_QUERY, new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement ps) throws SQLException {
                ps.setInt(1, player.getObjectId());
            }

            @Override
            public void handleRead(ResultSet rs) throws SQLException {
                while (rs.next()) {
                    player.setPrisonTimer(rs.getLong("punishment_timer"));

                    if (player.isInPrison())
                        player.setPrisonTimer(rs.getLong("punishment_timer"));
                    else
                        player.setPrisonTimer(0);
                }
            }
        });
    }

    @Override
    public void storePlayerPunishments(final Player player) {
        DB.insertUpdate(UPDATE_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
                ps.setInt(1, player.isInPrison() ? 1 : 0);
                ps.setLong(2, player.getPrisonTimer());
                ps.setInt(3, player.getObjectId());
                ps.execute();
            }
        });
    }

    @Override
    public void punishPlayer(final Player player, final int mode) {
        DB.insertUpdate(REPLACE_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
                ps.setInt(1, player.getObjectId());
                ps.setInt(2, mode);
                ps.setLong(3, player.getPrisonTimer());
                ps.execute();
            }
        });
    }

    @Override
    public void unpunishPlayer(final Player player) {
        DB.insertUpdate(DELETE_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
                ps.setInt(1, player.getObjectId());
                ps.execute();
            }
        });
    }

    @Override
    public boolean supports(String s, int i, int i1) {
        return MySQL5DAOUtils.supports(s, i, i1);
    }
}
