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

import gameserver.model.templates.petskill.PetSkillTemplate;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author ATracer
 */
@XmlRootElement(name = "pet_skill_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class PetSkillData {
    @XmlElement(name = "pet_skill")
    private List<PetSkillTemplate> petSkills;

    /**
     * A map containing all npc skill templates
     */
    private TIntObjectHashMap<TIntIntHashMap> petSkillData = new TIntObjectHashMap<TIntIntHashMap>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (PetSkillTemplate petSkill : petSkills) {
            TIntIntHashMap orderSkillMap = petSkillData.get(petSkill.getOrderSkill());
            if (orderSkillMap == null) {
                orderSkillMap = new TIntIntHashMap();
                petSkillData.put(petSkill.getOrderSkill(), orderSkillMap);
            }

            orderSkillMap.put(petSkill.getPetId(), petSkill.getSkillId());
        }
    }

    public int size() {
        return petSkillData.size();
    }

    public int getPetOrderSkill(int orderSkill, int petNpcId) {
        return petSkillData.get(orderSkill).get(petNpcId);
	}
}
