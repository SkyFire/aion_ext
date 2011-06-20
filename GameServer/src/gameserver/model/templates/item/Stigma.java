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
package gameserver.model.templates.item;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Stigma")
public class Stigma {
    @XmlElement(name = "require_skill")
    protected List<RequireSkill> requireSkill;
    @XmlAttribute
    protected int skillid;
    @XmlAttribute
    protected int skilllvl;
    @XmlAttribute
    protected int shard;

    /**
     * @return the skillid
     */
    public int getSkillid() {
        return skillid;
    }

    /**
     * @return the skilllvl
     */
    public int getSkilllvl() {
        return skilllvl;
    }

    /**
     * @return the shard
     */
    public int getShard() {
        return shard;
    }

    public List<RequireSkill> getRequireSkill() {
        if (requireSkill == null) {
            requireSkill = new ArrayList<RequireSkill>();
        }
        return this.requireSkill;
    }
}
