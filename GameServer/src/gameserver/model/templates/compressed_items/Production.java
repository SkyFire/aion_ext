/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package gameserver.model.templates.compressed_items;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Mr. Poke, Jefe
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Production")
public class Production
{
    @XmlAttribute(required = true)
    protected int	chance;
    @XmlAttribute(name = "item_id", required = true)
    protected int	itemId;
    @XmlAttribute(required = false)
    protected int	count;
    @XmlAttribute(required = false)
    protected int	min;
    @XmlAttribute(required = false)
    protected int	max;

    /**
     * Gets the value of the chance property.
     * 
     */
    public int getChance() {
        return chance;
    }

    /**
     * Gets the value of the itemId property.
     * 
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Gets the value of the count property.
     * 
     */
    public int getCount() {
        return count;
    }
    
    /**
    * Gets the value of the Min property.
    * 
    */
    public int getMin() {
        return min;
    }  
    
    /**
    * Gets the value of the Max property.
    * 
    */
    public int getMax() {
        return max;
    }
}
