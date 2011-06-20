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
package gameserver.model.templates.itemset;

import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.templates.stats.ModifiersTemplate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.TreeSet;

/**
 * @author ATracer
 */
@XmlRootElement(name = "FullBonus")
@XmlAccessorType(XmlAccessType.FIELD)
public class FullBonus {
    @XmlElement(name = "modifiers", required = false)
    protected ModifiersTemplate modifiers;

    private int totalnumberofitems;

    public TreeSet<StatModifier> getModifiers() {
        return modifiers != null ? modifiers.getModifiers() : null;
    }

    /**
     * @return Value of the number of items in the set
     */
    public int getCount() {
        return totalnumberofitems;
    }

    /**
     * Sets number of items in the set (when this bonus applies)
     *
     * @param number
     */
    public void setNumberOfItems(int number) {
        this.totalnumberofitems = number;
    }
}
