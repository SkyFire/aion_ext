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
import gameserver.model.AbyssRankingResult;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;

import java.util.ArrayList;

/**
 * @author ATracer
 */
public abstract class AbyssRankDAO implements DAO {

    @Override
    public final String getClassName() {
        return AbyssRankDAO.class.getName();
    }

    public abstract void loadAbyssRank(Player player);

    public abstract boolean storeAbyssRank(Player player);

    public abstract void updatePlayerRanking();

    public abstract void updateLegionRanking();

    public abstract ArrayList<AbyssRankingResult> getAbyssRankingPlayers(Race race);

    public abstract ArrayList<AbyssRankingResult> getAbyssRankingLegions(Race race);
}
