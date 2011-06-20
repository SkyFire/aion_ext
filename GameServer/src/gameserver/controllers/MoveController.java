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
package gameserver.controllers;

import gameserver.ai.desires.impl.MoveToHomeDesire;
import gameserver.controllers.movement.MovementType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.siege.FortressGeneral;
import gameserver.network.aion.serverpackets.SM_MOVE;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import org.apache.log4j.Logger;

import java.util.concurrent.Future;

/**
 * @author ATracer
 */
public class MoveController {

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(MoveController.class);

    private Future<?> moveTask;
    private Creature owner;
    private boolean directionChanged = true;

    private VisibleObject target;

    private float targetX;
    private float targetY;
    private float targetZ;

    private float ownerX;
    private float ownerY;
    private float ownerZ;

    private float x2;
    private float y2;
    private float z2;
    private byte h2;

    private boolean isFollowTarget;
    private boolean isStopped = false;

    private float speed = 0;
    private float distance = 2;

    private double distanceToTarget;
    private boolean walking;
    private boolean canWalk = true;

    /**
     * @param owner
     */
    public MoveController(Creature owner) {
        this.owner = owner;
    }

    /**
     * @param isFollowTarget the isFollowTarget to set
     */
    public void setFollowTarget(boolean isFollowTarget) {
        this.isFollowTarget = isFollowTarget;
    }

    /**
     * @param directionChanged the directionChanged to set
     */
    public void setDirectionChanged(boolean directionChanged) {
        this.directionChanged = directionChanged;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * @return The speed.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(float distance) {
        this.distance = distance;
    }

    /**
     * @return the walking
     */
    public boolean isWalking() {
        return walking;
    }

    /**
     * @param walking the walking to set
     */
    public void setWalking(boolean walking) {
        this.walking = walking;
    }

    /**
     * @return creature is able to walk
     */
    public boolean canWalk() {
        return canWalk;
    }

    /**
     * @param canWalk if creature is able to walk
     */
    public void setCanWalk(boolean canWalk) {
        this.canWalk = canWalk;
    }

    public void setNewDirection(float x, float y, float z) {
        if (x != targetX || y != targetY || z != targetZ)
            directionChanged = true;
        this.targetX = x;
        this.targetY = y;
        this.targetZ = z;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public float getTargetZ() {
        return targetZ;
    }

    public boolean isScheduled() {
        return moveTask != null && !moveTask.isCancelled();
    }

    public void schedule() {
        if (isScheduled())
            return;

        if (speed == 0)
            speed = owner.getGameStats().getCurrentStat(StatEnum.SPEED) / 1000;

        moveTask = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable() {

            @Override
            public void run() {
                move();
            }
        }, 0, 500);
    }

    private void move() {
        /**
         * Demo npc skills - prevent movement while casting
         */
        if (!owner.canPerformMove() || owner.isCasting() || !canWalk) {
            if (!isStopped) {
                isStopped = true;
                owner.getController().stopMoving();
            }
            return;
        }

        target = owner.getTarget();

        if (isFollowTarget && target != null) {
            setNewDirection(target.getX(), target.getY(), target.getZ());
        }

        ownerX = owner.getX();
        ownerY = owner.getY();
        ownerZ = owner.getZ();

        distanceToTarget = MathUtil.getDistance(ownerX, ownerY, ownerZ, targetX, targetY, targetZ);

        // 2 hacks for FortressGenerals
        if (owner instanceof FortressGeneral) {
            this.distance = 3.5f;

            if (MathUtil.getDistance(owner, owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ()) > 150) {
                owner.getAi().clearDesires();
                owner.getAi().addDesire(new MoveToHomeDesire((Npc) owner, 100));
                owner.getAi().schedule();
                return;
            }
        }

        if (distanceToTarget > this.distance) {
            isStopped = false;

            if(distanceToTarget > (speed * 0.5)) {
            	x2 = (float) (((targetX - ownerX) / distanceToTarget) * speed * 0.5);
            	y2 = (float) (((targetY - ownerY) / distanceToTarget) * speed * 0.5);
            	z2 = (float) (((targetZ - ownerZ) / distanceToTarget) * speed * 0.5);
            }
            else {
            	x2 = (float) (targetX - ownerX);
            	y2 = (float) (targetY - ownerY);
            	z2 = (float) (targetZ - ownerZ);
            }

            h2 = (byte) (Math.toDegrees(Math.atan2(y2, x2)) / 3);

            if (directionChanged) {
                PacketSendUtility.broadcastPacket(owner, new SM_MOVE(owner.getObjectId(), ownerX, ownerY, ownerZ,
                        (float) (x2 / 0.5), (float) (y2 / 0.5), 0, h2, MovementType.MOVEMENT_START_KEYBOARD));
                directionChanged = false;
                World.getInstance().updatePosition(owner, ownerX + x2, ownerY + y2, ownerZ + z2, h2, true);
            } else {
                World.getInstance().updatePosition(owner, ownerX + x2, ownerY + y2, ownerZ + z2, h2, false);
            }

        } else {
            if (!isStopped) {
                isStopped = true;
                owner.getController().stopMoving();
                long lastAttackDiff = System.currentTimeMillis() - owner.getLastAttack();
                if(lastAttackDiff > 1500) {
                	owner.getController().attackTarget((Creature) owner.getTarget());
                	owner.setLastAttack(System.currentTimeMillis());
                }
            }
        }
    }

    public double getDistanceToTarget() {
        if (isFollowTarget) {
            VisibleObject target = owner.getTarget();
            if (target != null)
                return MathUtil.getDistance(owner.getX(), owner.getY(), owner.getZ(),
                        target.getX(), target.getY(), target.getZ());

        }
        return MathUtil.getDistance(owner.getX(), owner.getY(), owner.getZ(), targetX, targetY, targetZ);
    }

    public void stop() {
        this.walking = false;

        if (moveTask != null) {
            if (!moveTask.isCancelled())
                moveTask.cancel(true);
            moveTask = null;
        }
    }
}
