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
import gameserver.model.gameobjects.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr. Poke
 */
public class StaticObjectKnownList extends NpcKnownList {
    /**
     * @param owner
     */
    public StaticObjectKnownList(VisibleObject owner) {
        super(owner);
    }

    /**
     * Find objects that are in visibility range.
     */
    @Override
    protected void findVisibleObjects() {
        if (owner == null || !owner.isSpawned())
            return;

        final List<VisibleObject> objectsToAdd = new ArrayList<VisibleObject>();

        for (MapRegion r : owner.getActiveRegion().getNeighbours()) {
            r.doOnAllPlayers(new Executor<Player>() {
                @Override
                public boolean run(Player newObject) {
                    if (newObject == owner || newObject == null)
                        return true;

                    if (!checkObjectInRange(owner, newObject))
                        return true;

                    objectsToAdd.add(newObject);
                    return true;
                }
            }, true);
        }

        for (VisibleObject object : objectsToAdd) {
            object.getKnownList().storeObject(owner);
            owner.getController().see(object);
        }

        objectsToAdd.clear();
    }
}
