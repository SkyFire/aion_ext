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
import gameserver.dao.NpcShoutsDAO;
import gameserver.model.NpcShout;
import javolution.util.FastMap;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author xitanium
 */
public class MySQL5NpcShoutsDAO extends NpcShoutsDAO {

    @Override
    public FastMap<Integer, NpcShout> getShouts() {
        final FastMap<Integer, NpcShout> shouts = new FastMap<Integer, NpcShout>();
        DB.select("SELECT npc_id, message_id, _interval FROM npc_shouts", new ReadStH() {

            @Override
            public void handleRead(ResultSet arg0) throws SQLException {
                // TODO Auto-generated method stub
                while (arg0.next()) {
                    int npcId = arg0.getInt("npc_id");
                    int messageId = arg0.getInt("message_id");
                    int interval = arg0.getInt("_interval");
                    shouts.put(npcId, new NpcShout(npcId, messageId, interval));
                }
            }
        });
        return shouts;
    }

    @Override
    public boolean supports(String databaseName, int majorVersion, int minorVersion) {
        return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
    }

}
