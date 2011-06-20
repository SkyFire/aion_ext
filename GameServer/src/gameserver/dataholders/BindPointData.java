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

import gameserver.model.templates.BindPointTemplate;
import gnu.trove.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author avol
 */
@XmlRootElement(name = "bind_points")
@XmlAccessorType(XmlAccessType.FIELD)
public class BindPointData {
    private static Logger log = Logger.getLogger(BindPointData.class);

    @XmlElement(name = "bind_point")
    private List<BindPointTemplate> bplist;

    /**
     * A map containing all bind point location templates
     */
    private TIntObjectHashMap<BindPointTemplate> bindplistData = new TIntObjectHashMap<BindPointTemplate>();
    private TIntObjectHashMap<BindPointTemplate> bindplistData2 = new TIntObjectHashMap<BindPointTemplate>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (BindPointTemplate bind : bplist) {
            bindplistData.put(bind.getNpcId(), bind);
            bindplistData2.put(bind.getBindId(), bind);
        }
    }

    public int size() {
        return bindplistData.size();
    }

    public BindPointTemplate getBindPointTemplate(int npcId) {
        BindPointTemplate bpt = bindplistData.get(npcId);
        if (bpt == null) {
            log.error("BindPointTemplate is missing for npcId: " + npcId);            
            return null;
        }
        return bpt;
    }

    public BindPointTemplate getBindPointTemplate2(int bindPointId) {
        BindPointTemplate bpt = bindplistData2.get(bindPointId);
        if (bpt == null) {
            log.error("BindPointTemplate is missing for bindPointId: " + bindPointId);            
            return null;
        }
        return bpt;
    }
}
