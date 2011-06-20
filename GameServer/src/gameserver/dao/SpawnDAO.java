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
import gameserver.model.templates.spawn.SpawnTemplate;

import java.util.Map;

/**
 * @author xavier
 */
public abstract class SpawnDAO implements DAO {
    public enum SpawnType {
        SPAWNED(1),
        DESPAWNED(2),
        REMOVED(3),
        ALL(0);

        private int type;

        private SpawnType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    @Override
    public String getClassName() {
        return SpawnDAO.class.getName();
    }

    public abstract int addSpawn(int npcId, int adminObjectId, String group, boolean noRespawn, int mapId, float x, float y, float z, byte h, int objectId, int staticid);

    public abstract boolean unSpawnGroup(int adminObjectId, String group);

    public abstract boolean isSpawned(int adminObjectId, String group);

    public abstract int isInDB(int npcId, float x, float y);

    public abstract boolean updateHeading(int spawnId, int heading);

    public abstract boolean setSpawned(int spawnId, int objectId, boolean isSpawned);

    public abstract boolean setGroupSpawned(int adminObjectId, String group, boolean isSpawned);

    public abstract Map<Integer, SpawnTemplate> listSpawns(int adminObjectId, String group, SpawnType type);

    public abstract Map<String, Integer> listSpawnGroups(int adminObjectId);

    public abstract Map<Integer, SpawnTemplate> getAllSpawns();

    public abstract boolean deleteSpawn(int spawnId);

    public abstract boolean deleteSpawnGroup(int adminObjectId, String groupName);

    public abstract int getSpawnObjectId(int spawnId, boolean isSpawned);

    public abstract SpawnTemplate getSpawnTemplate(int spawnId);
}
