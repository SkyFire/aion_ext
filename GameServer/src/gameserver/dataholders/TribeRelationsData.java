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

import gameserver.model.templates.tribe.*;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ATracer
 */
@XmlRootElement(name = "tribe_relations")
@XmlAccessorType(XmlAccessType.FIELD)
public class TribeRelationsData {
    @XmlElement(name = "tribe", required = true)
    protected List<Tribe> tribeList;

    protected Map<String, Tribe> tribeNameMap = new HashMap<String, Tribe>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (Tribe tribe : tribeList) {
            tribeNameMap.put(tribe.getName(), tribe);
        }
        tribeList = null;
    }

    /**
     * @return tribeNameMap.size()
     */
    public int size() {
        return tribeNameMap.size();
    }

    /**
     * @param tribeName
     * @return
     */
    public boolean hasAggressiveRelations(String tribeName) {
        Tribe tribe = tribeNameMap.get(tribeName);
        if (tribe == null)
            return false;
        AggroRelations aggroRelations = tribe.getAggroRelations();
        return aggroRelations != null && !aggroRelations.getTo().isEmpty();
    }

    /**
     * @param tribeName
     * @return
     */
    public boolean hasHostileRelations(String tribeName) {
        Tribe tribe = tribeNameMap.get(tribeName);
        if (tribe == null)
            return false;
        HostileRelations hostileRelations = tribe.getHostileRelations();
        return hostileRelations != null && !hostileRelations.getTo().isEmpty();
    }

    /**
     * @param tribeName1
     * @param tribeName2
     * @return
     */
    public boolean isAggressiveRelation(String tribeName1, String tribeName2) {
        Tribe tribe1 = tribeNameMap.get(tribeName1);
        if (tribe1 == null)
            return false;
        AggroRelations aggroRelations = tribe1.getAggroRelations();
        if (aggroRelations == null)
            return false;

        return aggroRelations.getTo().contains(tribeName2);
    }

    /**
     * @param tribeName1
     * @param tribeName2
     * @return
     */
    public boolean isSupportRelation(String tribeName1, String tribeName2) {
        Tribe tribe1 = tribeNameMap.get(tribeName1);
        if (tribe1 == null)
            return false;
        SupportRelations supportRelations = tribe1.getSupportRelations();
        if (supportRelations == null)
            return false;
        return supportRelations.getTo().contains(tribeName2);
    }

    /**
     * @param tribeName1
     * @param tribeName2
     * @return
     */
    public boolean isFriendlyRelation(String tribeName1, String tribeName2) {
        Tribe tribe1 = tribeNameMap.get(tribeName1);
        if (tribe1 == null)
            return false;
        FriendlyRelations friendlyRelations = tribe1.getFriendlyRelations();
        if (friendlyRelations == null)
            return false;
        return friendlyRelations.getTo().contains(tribeName2);
    }

    /**
     * @param tribeName1
     * @param tribeName2
     * @return
     */
    public boolean isNeutralRelation(String tribeName1, String tribeName2) {
        Tribe tribe1 = tribeNameMap.get(tribeName1);
        if (tribe1 == null)
            return false;
        NeutralRelations neutralRelations = tribe1.getNeutralRelations();
        if (neutralRelations == null)
            return false;
        return neutralRelations.getTo().contains(tribeName2);
    }

    /**
     * @param tribeName1
     * @param tribeName2
     * @return
     */
    public boolean isHostileRelation(String tribeName1, String tribeName2) {
        Tribe tribe1 = tribeNameMap.get(tribeName1);
        if (tribe1 == null)
            return false;
        HostileRelations hostileRelations = tribe1.getHostileRelations();
        if (hostileRelations == null)
            return false;
        return hostileRelations.getTo().contains(tribeName2);
    }

    /**
     * @param tribeName
     * @return
     */
    public boolean isGuardDark(String tribeName) {
        Tribe tribe = tribeNameMap.get(tribeName);
        if (tribe == null)
            return false;

        if (Tribe.GUARD_DARK.equals(tribe.getName()))
            return true;

        String baseTribe = tribe.getBase();
        if (baseTribe != null)
            return isGuardDark(baseTribe);

        return false;
    }

    /**
     * @param tribeName
     * @return
     */
    public boolean isGuardLight(String tribeName) {
        Tribe tribe = tribeNameMap.get(tribeName);
        if (tribe == null)
            return false;

        if (Tribe.GUARD_LIGHT.equals(tribe.getName()))
            return true;

        String baseTribe = tribe.getBase();
		if(baseTribe != null)
			return isGuardLight(baseTribe);
		
		return false;
	}
}
