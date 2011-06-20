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

package gameserver.world;

import gameserver.model.gameobjects.VisibleObject;
import gameserver.world.MapRegion;

public class NpcKnownList extends KnownList {
    /**
     * @param owner
     */
    public NpcKnownList(VisibleObject owner) {
        super(owner);
    }

    /**
     * Do KnownList update.
     */
    public void doUpdate() {
        MapRegion mapRegion = owner.getActiveRegion();
        if (mapRegion == null)
            return;

        if (mapRegion.isMapRegionActive())
            super.doUpdate();
        else
            clear();
    }
}