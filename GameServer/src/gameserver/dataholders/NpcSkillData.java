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

import gameserver.model.templates.npcskill.NpcSkillList;
import gnu.trove.TIntObjectHashMap;
import org.apache.log4j.Logger;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author ATracer
 */
@XmlRootElement(name = "npc_skill_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class NpcSkillData {
    @XmlElement(name = "npcskills")
    private List<NpcSkillList> npcSkills;

    /**
     * A map containing all npc skill templates
     */
    private TIntObjectHashMap<NpcSkillList> npcSkillData = new TIntObjectHashMap<NpcSkillList>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (NpcSkillList npcSkill : npcSkills) {
            npcSkillData.put(npcSkill.getNpcId(), npcSkill);

            if (npcSkill.getNpcSkills() == null)
                Logger.getLogger(NpcSkillData.class).error("NO SKILL");
        }

    }

    public int size() {
        return npcSkillData.size();
    }

    public NpcSkillList getNpcSkillList(int id) {
        return npcSkillData.get(id);
    }
}
