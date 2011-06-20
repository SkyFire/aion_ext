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
package gameserver.skillengine.properties;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Properties")
public class Properties {
    @XmlElements({
            @XmlElement(name = "firsttarget", type = FirstTargetProperty.class),
            @XmlElement(name = "targetrange", type = TargetRangeProperty.class),
            @XmlElement(name = "addweaponrange", type = AddWeaponRangeProperty.class),
            @XmlElement(name = "targetrelation", type = TargetRelationProperty.class),
            @XmlElement(name = "firsttargetrange", type = FirstTargetRangeProperty.class)
    })
    protected List<Property> properties;

    /**
     * Gets the value of the getProperties property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the getProperties property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperties().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link FirstTargetProperty }
     * {@link TargetRangeProperty }
     * {@link AddWeaponRangeProperty }
     * {@link TargetRelationProperty }
     * {@link FirstTargetRangeProperty }
     */
    public List<Property> getProperties() {
        if (properties == null) {
            properties = new ArrayList<Property>();
        }

        return this.properties;
    }

}
