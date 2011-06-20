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
package gameserver.model.templates.portal;

import gameserver.model.Race;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Portal")
public class PortalTemplate {
    @XmlAttribute(name = "npcid")
    protected int npcId;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "instance")
    protected boolean instance;
    @XmlAttribute(name = "minlevel")
    protected int minLevel;
    @XmlAttribute(name = "maxlevel")
    protected int maxLevel;
    @XmlAttribute(name = "group")
    protected boolean group;
    @XmlAttribute(name = "race")
    protected Race race;
    @XmlElement(name = "entrypoint")
    protected List<EntryPoint> entryPoint;
    @XmlElement(name = "exitpoint")
    protected List<ExitPoint> exitPoint;
    @XmlElement(name = "portalitem")
    protected List<PortalItem> portalItem;
    @XmlAttribute(name = "titleid")
    protected int IdTitle;

    /**
     * @return the npcId
     */
    public int getNpcId() {
        return npcId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the instance
     */
    public boolean isInstance() {
        return instance;
    }

    /**
     * @return the minLevel
     */
    public int getMinLevel() {
        return minLevel;
    }

    /**
     * @return the maxLevel
     */
    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * @return the group
     */
    public boolean isGroup() {
        return group;
    }

    /**
     * @return the race
     */
    public Race getRace() {
        return race;
    }

    /**
     * @return the entryPoint
     */
    public List<EntryPoint> getEntryPoint() {
        return entryPoint;
    }

    /**
     * @return the exitPoint
     */
    public List<ExitPoint> getExitPoint() {
        return exitPoint;
    }

    /**
     * @return the portalItem
     */
    public List<PortalItem> getPortalItem() {
        return portalItem;
    }

    /**
     * @return the Title Id
     */
    public int getIdTitle() {
        return IdTitle;
    }
}
