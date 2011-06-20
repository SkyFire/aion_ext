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
package gameserver.dataholders;

import gameserver.model.templates.siege.SiegeSpawnList;
import gnu.trove.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author zdead
 */
@XmlRootElement(name = "siege_spawns")
@XmlAccessorType(XmlAccessType.FIELD)
public class SiegeSpawnData {
    @XmlElement(name = "siege_spawn")
    private List<SiegeSpawnList> siegeLocationSpawnList;

    /**
     * Map that contains skillId - SkillTemplate key-value pair
     */
    private TIntObjectHashMap<SiegeSpawnList> siegeLists = new TIntObjectHashMap<SiegeSpawnList>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        siegeLists.clear();
        for (SiegeSpawnList list : siegeLocationSpawnList) {
            siegeLists.put(list.getLocationId(), list);
        }
    }

    public int size() {
        return siegeLists.size();
    }

    public SiegeSpawnList getSpawnsForLocation(int locationId) {
        return siegeLists.get(locationId);
    }
}
