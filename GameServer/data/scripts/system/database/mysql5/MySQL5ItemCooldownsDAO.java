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
import gameserver.dao.ItemCooldownsDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.items.ItemCooldown;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author ATracer
 */
public class MySQL5ItemCooldownsDAO extends ItemCooldownsDAO {
    public static final String INSERT_QUERY = "INSERT INTO `item_cooldowns` (`player_id`, `delay_id`, `use_delay`, `reuse_time`) VALUES (?,?,?,?)";
    public static final String DELETE_QUERY = "DELETE FROM `item_cooldowns` WHERE `player_id`=?";
    public static final String SELECT_QUERY = "SELECT `delay_id`, `use_delay`, `reuse_time` FROM `item_cooldowns` WHERE `player_id`=?";


    @Override
    public void loadItemCooldowns(final Player player) {
        DB.select(SELECT_QUERY, new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, player.getObjectId());
            }

            @Override
            public void handleRead(ResultSet rset) throws SQLException {
                while (rset.next()) {
                    int delayId = rset.getInt("delay_id");
                    int useDelay = rset.getInt("use_delay");
                    long reuseTime = rset.getLong("reuse_time");

                    if (reuseTime > System.currentTimeMillis())
                        player.addItemCoolDown(delayId, reuseTime, useDelay);

                }
            }
        });
        player.getEffectController().broadCastEffects();
    }

    @Override
    public void storeItemCooldowns(final Player player) {
        deleteItemCooldowns(player);
        Map<Integer, ItemCooldown> itemCoolDowns = player.getItemCoolDowns();

        if (itemCoolDowns == null)
            return;

        for (Map.Entry<Integer, ItemCooldown> entry : itemCoolDowns.entrySet()) {
            final int delayId = entry.getKey();
            final long reuseTime = entry.getValue().getReuseTime();
            final int useDelay = entry.getValue().getUseDelay();

            if (reuseTime - System.currentTimeMillis() < 30000)
                continue;

            DB.insertUpdate(INSERT_QUERY, new IUStH() {
                @Override
                public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                    stmt.setInt(1, player.getObjectId());
                    stmt.setInt(2, delayId);
                    stmt.setInt(3, useDelay);
                    stmt.setLong(4, reuseTime);
                    stmt.execute();
                }
            });
        }
    }

    private void deleteItemCooldowns(final Player player) {
        DB.insertUpdate(DELETE_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, player.getObjectId());
                stmt.execute();
            }
        });
    }

    @Override
    public boolean supports(String arg0, int arg1, int arg2) {
        return MySQL5DAOUtils.supports(arg0, arg1, arg2);
    }
}
