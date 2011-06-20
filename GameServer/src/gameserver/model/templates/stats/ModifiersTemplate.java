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
package gameserver.model.templates.stats;

import gameserver.model.gameobjects.stats.modifiers.*;

import javax.xml.bind.annotation.*;
import java.util.TreeSet;

/**
 * @author xavier
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "modifiers")
public class ModifiersTemplate {
    @XmlElements({
            @XmlElement(name = "sub", type = SubModifier.class),
            @XmlElement(name = "add", type = AddModifier.class),
            @XmlElement(name = "rate", type = RateModifier.class),
            @XmlElement(name = "set", type = SetModifier.class),
            @XmlElement(name = "mean", type = MeanModifier.class)
    })
    private TreeSet<StatModifier> modifiers;

    public TreeSet<StatModifier> getModifiers() {
        return modifiers;
    }
}
