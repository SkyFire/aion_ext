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
import gameserver.model.gameobjects.player.ToyPet;

import java.util.List;

/**
 * @author xitanium
 */
public abstract class PlayerPetsDAO implements DAO {
    @Override
    public final String getClassName() {
        return PlayerPetsDAO.class.getName();
    }

    public abstract void insertPlayerPet(Player player, int petId, int decoration, String name);

    public abstract void renamePet(Player player, int petId, String name);

    public abstract void removePlayerPet(Player player, int petId);

    public abstract List<ToyPet> getPlayerPets(int playerId);

    public abstract ToyPet getPlayerPet(int playerId, int petId);

}
