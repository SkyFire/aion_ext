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
package gameserver.model.templates.gather;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Material")
public class Material {

    @XmlAttribute
    protected String name;
    @XmlAttribute
    protected Integer itemid;
    @XmlAttribute
    protected Integer nameid;
    @XmlAttribute
    protected Integer rate;

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * @return the itemid
     */
    public Integer getItemid() {
        return itemid;
    }

    /**
     * Gets the value of the nameid property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public Integer getNameid() {
        return nameid;
    }

    /**
     * Gets the value of the rate property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public Integer getRate() {
        return rate;
    }
}
