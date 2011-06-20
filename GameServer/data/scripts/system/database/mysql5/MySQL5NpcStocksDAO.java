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
import com.aionemu.commons.database.ReadStH;
import com.aionemu.commons.database.IUStH;
import gameserver.dao.NpcStocksDAO;
import gameserver.model.NpcStocks;

import javolution.util.FastMap;
import java.util.Map;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author xitanium
 */
public class MySQL5NpcStocksDAO extends NpcStocksDAO {

    @Override
    public NpcStocks getStocks()
    {
        final NpcStocks npcStocks = new NpcStocks();
        DB.select("SELECT * FROM npc_stocks", new ReadStH() {
            @Override
            public void handleRead(ResultSet arg0) throws SQLException {
                while (arg0.next()) {
                    int playerId    = arg0.getInt("player_id");
                    int npcId       = arg0.getInt("npc_id");
                    int itemTplId   = arg0.getInt("item_id");
                    int count       = arg0.getInt("quantity");
                    long lastSaleDate= arg0.getTimestamp("last_sale").getTime();
                    npcStocks.addStock(playerId, npcId, itemTplId, count, lastSaleDate);
                }
            }
        });
        return npcStocks;
    }
    
    @Override
    public void storeNpcStock(final Map<String, Integer> npcStock) {
        DB.insertUpdate("REPLACE INTO npc_stocks (npc_id, player_id, item_id, count, last_sale)" +
        " VALUES(?,?,?,?,?)" , new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
                Timestamp timestamp =  new Timestamp((long)npcStock.get("lastSaleDate"));
                ps.setInt(1, npcStock.get("npcId"));
                ps.setInt(2, npcStock.get("playerId"));
                ps.setInt(3, npcStock.get("itemTemplateId"));
                ps.setInt(4, npcStock.get("count"));
                ps.setTimestamp(5, timestamp);
                ps.execute();
            }
        });
    }
    
    @Override
    public void restockNpcs() {
        DB.insertUpdate("UPDATE npc_stocks SET count = 0 where player_id = 0");
    }

    @Override
    public boolean supports(String databaseName, int majorVersion, int minorVersion) {
        return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
    }

}
