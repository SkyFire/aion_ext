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

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author ATracer, modified by Antivirus
 */
@XmlRootElement(name = "itemset")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemSetTemplate {
    @XmlElement(required = true)
    protected List<ItemPart> itempart;
    @XmlElement(required = true)
    protected List<PartBonus> partbonus;
    protected FullBonus fullbonus;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    protected int id;

    /*
    * Final setting
    */

    void afterUnmarshal(Unmarshaller u, Object parent) {
        if (fullbonus != null) {
            // Set number of items to apply the full bonus
            fullbonus.setNumberOfItems(itempart.size());
        }
    }

    /**
     * @return the itempart
     */
    public List<ItemPart> getItempart() {
        return itempart;
    }

    /**
     * @return the partbonus
     */
    public List<PartBonus> getPartbonus() {
        return partbonus;
    }

    /**
     * @return the fullbonus
     */
    public FullBonus getFullbonus() {
        return fullbonus;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
}
