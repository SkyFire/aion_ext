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
package gameserver.model.templates.siege;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author zdead
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SiegeGuards")
public class SiegeGuards {

    @XmlElement(name = "siege_guard")
    protected List<SiegeGuardTemplate> siegeGuards;

    @XmlElement(name = "peace_guard")
    protected List<SiegeGuardTemplate> peaceGuards;

    @XmlElement(name = "rf_guard")
    protected List<SiegeGuardTemplate> reinforcementsGuards;

    /**
     * @return the siegeGuards
     */
    public List<SiegeGuardTemplate> getSiegeGuards() {
        return siegeGuards;
    }

    /**
     * @return the peaceGuards
     */
    public List<SiegeGuardTemplate> getPeaceGuards() {
        return peaceGuards;
    }

    /**
     * @return the reinforcementsGuards
     */
    public List<SiegeGuardTemplate> getReinforcementsGuards() {
        return reinforcementsGuards;
    }

}
