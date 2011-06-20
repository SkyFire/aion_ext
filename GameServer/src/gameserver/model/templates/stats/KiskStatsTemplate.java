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
package gameserver.model.templates.stats;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Sarynth
 */

@XmlRootElement(name = "kisk_stats")
@XmlAccessorType(XmlAccessType.FIELD)
public class KiskStatsTemplate {
    @XmlAttribute(name = "usemask")
    private int useMask = 4;

    @XmlAttribute(name = "members")
    private int maxMembers = 6;

    @XmlAttribute(name = "resurrects")
    private int maxResurrects = 18;

    public int getUseMask() {
        return useMask;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public int getMaxResurrects() {
        return maxResurrects;
    }
}
