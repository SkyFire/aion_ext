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
package gameserver.model.templates.teleport;

import javax.xml.bind.annotation.*;

/**
 * @author orz
 */
@XmlRootElement(name = "teleporter_template")
@XmlAccessorType(XmlAccessType.NONE)
public class TeleporterTemplate {
    @XmlAttribute(name = "npc_id", required = true)
    private int npcId;

    @XmlAttribute(name = "name", required = true)
    private String name = "";

    @XmlAttribute(name = "teleportId", required = true)
    private int teleportId = 0;

    @XmlAttribute(name = "type", required = true)
    private TeleportType type;

    @XmlElement(name = "locations")
    private TeleLocIdData teleLocIdData;

    /**
     * @return the npcId
     */
    public int getNpcId() {
        return npcId;
    }

    /**
     * @return the name of npc
     */
    public String getName() {
        return name;
    }

    /**
     * @return the teleportId
     */
    public int getTeleportId() {
        return teleportId;
    }

    /**
     * @return the type
     */
    public TeleportType getType() {
        return type;
    }

    /**
     * @return the teleLocIdData
     */
    public TeleLocIdData getTeleLocIdData() {
        return teleLocIdData;
    }
}
