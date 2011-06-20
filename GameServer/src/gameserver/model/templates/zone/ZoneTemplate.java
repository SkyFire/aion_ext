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
package gameserver.model.templates.zone;

import gameserver.world.zone.ZoneName;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Zone")
public class ZoneTemplate {
    @XmlElement(required = true)
    protected Points points;
    protected List<ZoneName> link;
    @XmlAttribute
    protected int priority;
    @XmlAttribute(name = "fly")
    protected boolean flightAllowed;
    @XmlAttribute(name = "breath")
    protected boolean breath;
    @XmlAttribute
    protected ZoneName name;
    @XmlAttribute
    protected int mapid;

    /**
     * Gets the value of the points property.
     */
    public Points getPoints() {
        return points;
    }

    /**
     * Gets the value of the link property.
     */
    public List<ZoneName> getLink() {
        if (link == null) {
            link = new ArrayList<ZoneName>();
        }
        return this.link;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @return the flightAllowed
     */
    public boolean isFlightAllowed() {
        return flightAllowed;
    }

    /**
     * Gets the value of the name property.
     */
    public ZoneName getName() {
        return name;
    }

    /**
     * Gets the value of the mapid property.
     */
    public int getMapid() {
        return mapid;
    }

    /**
     * @return the breath
     */
    public boolean isBreath() {
        return breath;
	}
}
