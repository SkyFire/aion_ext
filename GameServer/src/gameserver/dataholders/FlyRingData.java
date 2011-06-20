/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
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

import gameserver.model.templates.flyring.FlyRingTemplate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author xavier
 */
@XmlRootElement(name = "fly_rings")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlyRingData {
    @XmlElement(name = "fly_ring")
    private List<FlyRingTemplate> flyRingTemplates;

    public int size() {
        if (flyRingTemplates == null) {
            flyRingTemplates = new ArrayList<FlyRingTemplate>();
            return 0;
        }
        return flyRingTemplates.size();
    }

    public List<FlyRingTemplate> getFlyRingTemplates() {
        if (flyRingTemplates == null) {
            return new ArrayList<FlyRingTemplate>();
        }
        return flyRingTemplates;
    }

    public void addAll(Collection<FlyRingTemplate> templates) {
        if (flyRingTemplates == null) {
            flyRingTemplates = new ArrayList<FlyRingTemplate>();
        }
        flyRingTemplates.addAll(templates);
    }
}