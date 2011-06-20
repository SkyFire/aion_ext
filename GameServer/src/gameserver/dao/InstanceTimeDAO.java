/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is pryvate software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import gameserver.model.gameobjects.player.Player;

import java.util.Map;

/**
 * @author Arkshadow
 */
public abstract class InstanceTimeDAO implements DAO {
    @Override
    public String getClassName() {
        return InstanceTimeDAO.class.getName();
    }

    public abstract boolean updateEntry(final int instanceId, final Player player, final int cd);

    public abstract Map<Integer, Long> getTimes(Player player);

    public abstract boolean createEntry(final int instanceId, final Player player);

    public abstract boolean exists(final Player player, final int instanceId);
}
