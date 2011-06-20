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
package gameserver.model.templates.npcskill;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author AionChs Master
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "npcskills")
public class NpcSkillList {
    @XmlAttribute(name = "npcid")
    protected int npcId;
    @XmlAttribute(name = "skill_count")
    protected int count;
    @XmlElement(name = "npcskill")
    protected List<NpcSkillTemplate> npcSkills;

    /**
     * @return the npcId
     */
    public int getNpcId() {
        return npcId;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @return the npcSkills
     */
    public List<NpcSkillTemplate> getNpcSkills() {
        return npcSkills;
    }
}