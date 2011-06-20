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
package gameserver.skillengine.change;

import gameserver.model.gameobjects.stats.StatEnum;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Change")
public class Change {
    @XmlAttribute(required = true)
    private StatEnum stat;
    @XmlAttribute(required = true)
    private Func func;
    @XmlAttribute(required = true)
    private int value;
    @XmlAttribute
    private int delta;
    @XmlAttribute
    private boolean unchecked;

    /**
     * @return the unchecked
     */
    public boolean isUnchecked() {
        return unchecked;
    }

    /**
     * @return the stat
     */
    public StatEnum getStat() {
        return stat;
    }

    /**
     * @return the func
     */
    public Func getFunc() {
        return func;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @return the delta
     */
    public int getDelta() {
        return delta;
    }
}
