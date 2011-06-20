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
 
package gameserver.dataholders; 
 
import java.util.List; 
 
import javax.xml.bind.annotation.XmlAccessType; 
import javax.xml.bind.annotation.XmlAccessorType; 
import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlType; 
 
import gameserver.model.templates.compressed_items.CompressedItem; 
 
/** 
 * @author Mr. Poke 
 * 
 */ 
@XmlAccessorType(XmlAccessType.FIELD) 
@XmlType(name = "CompressedItems", propOrder = 
{"compressedItem" }) 
public class CompressedItemData 
{ 
        @XmlElement(name = "compressed_item") 
        protected List<CompressedItem>  compressedItem; 
 
        /** 
         * @return Returns the compressedItem. 
         */ 
        public List<CompressedItem> getCompressedItem() 
        { 
                return compressedItem; 
        } 
         
        public int size() 
        { 
                return compressedItem.size(); 
        } 
} 
