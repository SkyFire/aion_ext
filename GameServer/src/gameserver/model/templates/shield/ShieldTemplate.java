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

package gameserver.model.templates.shield;

import gameserver.model.Race;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author xavier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Shield")
public class ShieldTemplate {
    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "race")
    protected Race race;

    @XmlAttribute(name = "x")
    protected float x;

    @XmlAttribute(name = "y")
    protected float y;

    @XmlAttribute(name = "z")
    protected float z;

    @XmlAttribute(name = "radius")
    protected float radius;

    @XmlAttribute(name = "skill")
    protected int skill;

    @XmlAttribute(name = "map")
    protected int map;

    public String getName() {
        return name;
    }

    public Race getRace() {
        return race;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getRadius() {
        return radius;
    }

    public int getSkill() {
        return skill;
    }

    public int getMap() {
        return map;
    }
}
