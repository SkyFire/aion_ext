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

package gameserver.model.flyring;

import gameserver.ai.npcai.DummyAi;
import gameserver.controllers.FlyRingController;
import gameserver.model.gameobjects.Creature;
import gameserver.model.templates.flyring.FlyRingTemplate;
import gameserver.model.utils3d.Plane3D;
import gameserver.model.utils3d.Point3D;
import gameserver.utils.idfactory.IDFactory;
import gameserver.world.FlyRingKnownList;
import gameserver.world.World;

/**
 * @author xavier
 */
public class FlyRing extends Creature {
    private FlyRingTemplate template = null;
    private String name = null;
    private Plane3D plane = null;
    private Point3D center = null;
    private Point3D p1 = null;
    private Point3D p2 = null;

    public FlyRing(FlyRingTemplate template) {
        super(IDFactory.getInstance().nextId(), new FlyRingController(), null, null,
                World.getInstance().createPosition(template.getMap(), template.getCenter().getX(),
                        template.getCenter().getY(), template.getCenter().getZ(), (byte) 0));

        ((FlyRingController) getController()).setOwner(this);
        this.template = template;
        this.name = (template.getName() == null) ? "FLY_RING" : template.getName();
        this.center = new Point3D(template.getCenter().getX(), template.getCenter().getY(), template.getCenter().getZ());
        this.p1 = new Point3D(template.getP1().getX(), template.getP1().getY(), template.getP1().getZ());
        this.p2 = new Point3D(template.getP2().getX(), template.getP2().getY(), template.getP2().getZ());
        this.plane = new Plane3D(center, p1, p2);
        setKnownlist(new FlyRingKnownList(this));
    }

    public Plane3D getPlane() {
        return plane;
    }

    public FlyRingTemplate getTemplate() {
        return template;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte getLevel() {
        return 0;
    }

    @Override
    public void initializeAi() {
        ai = new DummyAi();
        ai.setOwner(this);
    }

    public void spawn() {
        World w = World.getInstance();
        w.storeObject(this);
        w.spawn(this);
    }
}
