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

import gameserver.model.flyring.FlyRing;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.shield.Shield;
import gameserver.utils.MathUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * KnownList.
 *
 * @author -Nemesiss-
 * @modified kosyachok
 */

public class KnownList extends ObjectContainer {
    /**
     * Visibility distance.
     */

    // how far player will see visible object
    private static final float VisibilityDistance = 95;

    // maxZvisibleDistance
    private static final float maxZvisibleDistance = 95;

    /**
     * Owner of this KnownList.
     */
    protected final VisibleObject owner;

    private long lastUpdate;

    /**
     * COnstructor.
     *
     * @param owner
     */
    public KnownList(VisibleObject owner) {
        super();
        this.owner = owner;
    }

    /**
     * Do KnownList update.
     */
    public void doUpdate() {
        if ((System.currentTimeMillis() - lastUpdate) < 1000)
            return;

        findVisibleObjects();

        lastUpdate = System.currentTimeMillis();
    }

    /**
     * Clear known list. Used when object is despawned.
     */
    public void clear() {
        AionObject obj;

        List<VisibleObject> toRemove = new ArrayList<VisibleObject>();

        synchronized (allObjects) {
            for (Iterator<AionObject> i = allObjects.values().iterator(); i.hasNext();) {
                obj = i.next();
                i.remove();
                if (obj instanceof VisibleObject) {
                    toRemove.add((VisibleObject) obj);
                }
            }
        }

        for (VisibleObject vObj : toRemove) {
            vObj.getKnownList().removeObject(owner, false);
        }
    }

    /**
     * Check if object is known
     *
     * @param object
     * @return true if object is known
     */
    public boolean knowns(AionObject object) {
        return allObjects.containsKey(object.getObjectId());
    }

    /**
     * Add VisibleObject to this KnownList.
     *
     * @param object
     */
    @Override
    public void storeObject(AionObject object) {
        if (!(object instanceof VisibleObject)) {
            throw new RuntimeException("Cannot store " + object.getClass().getCanonicalName() + " in " + getClass().getCanonicalName());
        }

        boolean callSee = !knowns(object);

        super.storeObject(object);

        if (callSee) {
            owner.getController().see((VisibleObject) object);
        }
    }

    /**
     * Delete VisibleObject from this KnownList.
     *
     * @param object
     */
    private void removeObject(VisibleObject object, boolean isOutOfRange) {
        boolean callNotSee = knowns(object);

        super.removeObject(object);

        /**
         * object was known.
         */
        if (callNotSee) {
            owner.getController().notSee(object, isOutOfRange);
        }
    }

    @Override
    public void removeObject(AionObject object) {
        if (!(object instanceof VisibleObject)) {
            throw new RuntimeException("Cannot remove " + object.getClass().getCanonicalName() + " in " + getClass().getCanonicalName());
        }

        this.removeObject((VisibleObject) object, true);
    }

    /**
     * Find objects that are in visibility range.
     */
    private class KnownListExecutor extends Executor<AionObject> {
        private List<VisibleObject> objectsToDel;
        private List<VisibleObject> objectsToAdd;
        private VisibleObject owner;

        private KnownListExecutor(VisibleObject owner) {
            this.objectsToAdd = new ArrayList<VisibleObject>();
            this.objectsToDel = new ArrayList<VisibleObject>();
            this.owner = owner;
        }

        private void updateKnownObjects() {
            for (MapRegion r : owner.getActiveRegion().getNeighbours()) {
                if (r != null)
                    r.doOnAllObjects(KnownListExecutor.this, true);
            }

            if (objectsToAdd.size() > 0) {
                for (VisibleObject object : objectsToAdd) {
                    storeObject(object);
                    object.getKnownList().storeObject(owner);
                }

                objectsToAdd.clear();
            }

            if (objectsToDel.size() > 0) {

                for (VisibleObject object : objectsToDel) {
                    removeObject(object);
                    object.getKnownList().removeObject(owner);
                }

                objectsToDel.clear();
            }
        }

        @Override
        public boolean run(AionObject newObject) {
            if (newObject == owner || newObject == null || !(newObject instanceof VisibleObject))
                return true;

            if (!checkObjectInRange(owner, (VisibleObject) newObject)) {
                if (owner.getKnownList().knowns(newObject)) {
                    objectsToDel.add((VisibleObject) newObject);
                }
                return true;
            }

            /**
             * New object is not known.
             */
            if (!knowns(newObject)) {
                objectsToAdd.add((VisibleObject) newObject);
            }
            return true;
        }
    }

    protected void findVisibleObjects() {
        if (owner == null || !owner.isSpawned())
            return;

        KnownListExecutor kle = new KnownListExecutor(owner);
        kle.updateKnownObjects();
    }

    protected boolean checkObjectInRange(VisibleObject owner, VisibleObject newObject) {
        if (newObject instanceof Shield) {
            return newObject.getKnownList().checkObjectInRange(newObject, owner);
        }

        if (newObject instanceof FlyRing) {
            return newObject.getKnownList().checkObjectInRange(newObject, owner);
        }

        //check if Z distance is greater than maxZvisibleDistance
        if (Math.abs(owner.getZ() - newObject.getZ()) > maxZvisibleDistance)
            return false;

        return MathUtil.isInRange(owner, newObject, VisibilityDistance);
    }
}
