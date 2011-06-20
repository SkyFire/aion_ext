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
package gameserver.model.templates;

import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.templates.stats.ModifiersTemplate;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.TreeSet;

/**
 * @author xavier
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "title_templates")
public class TitleTemplate {
    @XmlAttribute(name = "id", required = true)
    @XmlID
    private String id;

    @XmlElement(name = "modifiers", required = false)
    protected ModifiersTemplate modifiers;

    @XmlAttribute(name = "race", required = true)
    private int race;

    private int titleId;

    public int getTitleId() {
        return titleId;
    }

    public int getRace() {
        return race;
    }

    public TreeSet<StatModifier> getModifiers() {
        if (modifiers != null) {
            return modifiers.getModifiers();
        } else {
            return null;
        }
    }

    void afterUnmarshal(Unmarshaller u, Object parent) {
        this.titleId = Integer.parseInt(id);
    }
}
