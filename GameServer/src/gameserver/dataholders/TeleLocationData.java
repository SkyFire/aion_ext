/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is pryvate software: you can redistribute it and/or modify
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
package gameserver.dataholders;

import gameserver.model.templates.teleport.TelelocationTemplate;
import gnu.trove.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author orz
 */
@XmlRootElement(name = "teleport_location")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeleLocationData {
    @XmlElement(name = "teleloc_template")
    private List<TelelocationTemplate> tlist;

    /**
     * A map containing all teleport location templates
     */
    private TIntObjectHashMap<TelelocationTemplate> loctlistData = new TIntObjectHashMap<TelelocationTemplate>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (TelelocationTemplate loc : tlist) {
            loctlistData.put(loc.getLocId(), loc);
        }
    }

    public int size() {
        return loctlistData.size();
    }


    public TelelocationTemplate getTelelocationTemplate(int id) {
        return loctlistData.get(id);
    }

}
