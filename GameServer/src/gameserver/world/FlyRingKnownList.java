/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
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
package gameserver.world;

import gameserver.model.flyring.FlyRing;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.utils.MathUtil;

/**
 * @author xavier
 */
public class FlyRingKnownList extends KnownList {
    public FlyRingKnownList(VisibleObject owner) {
        super(owner);
    }

    @Override
    protected boolean checkObjectInRange(VisibleObject owner, VisibleObject newObject) {
        FlyRing fr = (FlyRing) owner;
        if (MathUtil.isIn3dRange(owner, newObject, fr.getTemplate().getRadius() * 2)) {
            return true;
        }
        return false;
    }
}
