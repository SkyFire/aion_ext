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
package gameserver.model.templates.walker;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author KKnD
 */
@XmlRootElement(name = "routes")
@XmlAccessorType(XmlAccessType.FIELD)
public class RouteData {
    @XmlElement(name = "routestep")
    private List<RouteStep> stepids;

    public List<RouteStep> getRouteSteps() {
        return stepids;
    }

    public RouteStep getRouteStep(int value) {
        for (RouteStep t : stepids) {
            if (t != null && t.getRouteStep() == value) {
                return t;
            }
        }
        return null;
    }
}
