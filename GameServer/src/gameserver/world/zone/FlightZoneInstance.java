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

package gameserver.world.zone;

import gameserver.model.templates.zone.Point2D;
import gameserver.model.templates.zone.ZoneTemplate;

/**
 * @author ATracer
 */
public class FlightZoneInstance {
    private int corners;
    private float xCoordinates[];
    private float yCoordinates[];

    private ZoneTemplate template;

    public FlightZoneInstance(ZoneTemplate template) {
        this.template = template;
        this.corners = template.getPoints().getPoint().size();
        xCoordinates = new float[corners];
        yCoordinates = new float[corners];
        for (int i = 0; i < corners; i++) {
            Point2D point = template.getPoints().getPoint().get(i);
            xCoordinates[i] = point.getX();
            yCoordinates[i] = point.getY();
        }
    }

    /**
     * @return the corners
     */
    public int getCorners() {
        return corners;
    }

    /**
     * @return the xCoordinates
     */
    public float[] getxCoordinates() {
        return xCoordinates;
    }

    /**
     * @return the yCoordinates
     */
    public float[] getyCoordinates() {
        return yCoordinates;
    }

    /**
     * @return the template
     */
    public ZoneTemplate getTemplate() {
        return template;
    }

    /**
     * Top z coordinate
     *
     * @return
     */
    public float getTop() {
        return template.getPoints().getTop();
    }

    /**
     * Bottom z coordinate
     *
     * @return
     */
    public float getBottom() {
        return template.getPoints().getBottom();
	}

}
