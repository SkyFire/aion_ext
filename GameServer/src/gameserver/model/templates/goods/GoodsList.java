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
package gameserver.model.templates.goods;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GoodsList")
public class GoodsList {

    protected List<GoodsList.Item> item;
    @XmlAttribute
    protected int id;

    protected List<Integer> itemIdList;

    void afterUnmarshal(Unmarshaller u, Object parent) {
        itemIdList = new ArrayList<Integer>();

        if (item == null)
            return;

        for (Item it : item) {
            itemIdList.add(it.getId());
        }
        item = null;
    }

    /**
     * Gets the value of the id property.
     */
    public int getId() {
        return id;
    }

    public List<GoodsList.Item> getItemList()
    {
       return item;
    }

    /**
     * @return the itemIdList
     */
    public List<Integer> getItemIdList() {
        return itemIdList;
    }


    /**
     * <p>Java class for anonymous complex type.
     * <p/>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}int" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Item {
        @XmlAttribute(name="buylimit", required=false)
        protected int buylimit;
        
        @XmlAttribute(name="selllimit", required=false)
        protected int selllimit;

        @XmlAttribute
        protected int id;

        /**
         * Gets the value of the id property.
         */
        public int getId() {
            return id;
        }

        public int getSellLimit() {
            return selllimit;
        }

        public int getBuyLimit() {
            return buylimit;
        }

        public boolean isLimited() {
            if (getBuyLimit() > 0 || getSellLimit() > 0)
                return true;
            return false;
        }
    }
}
