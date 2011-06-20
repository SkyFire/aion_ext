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
package gameserver.model.templates.siegelocation;

import gameserver.model.siege.SiegeType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sarynth
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "siegelocation")
public class SiegeLocationTemplate {
    @XmlAttribute(name = "id")
    protected int id;
    @XmlAttribute(name = "type")
    protected SiegeType type;
    @XmlAttribute(name = "world")
    protected int world;

    /**
     * @return the location id
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return the location type
     */
    public SiegeType getType() {
        return this.type;
    }

    /**
     * @return the world id
     */
    public int getWorldId() {
        return this.world;
    }
}
