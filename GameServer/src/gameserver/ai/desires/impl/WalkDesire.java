/*
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
package gameserver.ai.desires.impl;

import com.aionemu.commons.utils.Rnd;
import gameserver.ai.AI;
import gameserver.ai.desires.AbstractDesire;
import gameserver.ai.desires.MoveDesire;
import gameserver.configs.main.NpcMovementConfig;
import gameserver.dataholders.DataManager;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.templates.walker.RouteData;
import gameserver.model.templates.walker.RouteStep;
import gameserver.model.templates.walker.WalkerTemplate;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;

/**
 * @author KKnD
 */
public class WalkDesire extends AbstractDesire implements MoveDesire {
    private Npc owner;
    private RouteData route;
    private boolean isWalkingToNextPoint = false;
    private int targetPosition;
    private long nextMoveTime;
    private boolean isRandomWalk = false;

    private RouteStep randomPoint = null;
    private float walkArea = 10;
    private float halfWalkArea = 5;
    private float minRandomDistance = 2;

    public WalkDesire(Npc npc, int power) {
        super(power);
        owner = npc;

        WalkerTemplate template = DataManager.WALKER_DATA.getWalkerTemplate(owner.getSpawn().getWalkerId());
        isRandomWalk = owner.getSpawn().hasRandomWalk();
        if (template != null) {
            route = template.getRouteData();

            owner.getMoveController().setSpeed(owner.getObjectTemplate().getStatsTemplate().getWalkSpeed());
            owner.getMoveController().setWalking(true);
            PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.WALK));
        } else if (isRandomWalk && NpcMovementConfig.ACTIVE_NPC_MOVEMENT) {
            walkArea = Math.max(5, owner.getSpawn().getRandomWalkNr()); // The walk area is at least 5 meter.
            halfWalkArea = walkArea / 2f;
            minRandomDistance = Math.min(walkArea / 5f, 2); // The stop distance is between 1 and 2 meter.

            route = null;
            randomPoint = new RouteStep(owner.getX(), owner.getY(), owner.getZ());

            owner.getMoveController().setSpeed(owner.getObjectTemplate().getStatsTemplate().getWalkSpeed());
            owner.getMoveController().setWalking(true);
            owner.getMoveController().setDistance(minRandomDistance);
            PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.WALK));
        }
    }

    @Override
    public boolean handleDesire(AI<?> ai) {
        if (owner == null)
            return false;

        if (route == null && !isRandomWalk)
            return false;

        if (isWalkingToNextPoint())
            checkArrivedToPoint();

        walkToLocation();
        return true;
    }

    /**
     * Check owner is in a route point
     */
    private void checkArrivedToPoint() {
        RouteStep step = randomPoint;
        if (route != null) {
            step = route.getRouteSteps().get(targetPosition);
        }

        float x = step.getX();
        float y = step.getY();
        float z = step.getZ();

        double dist = MathUtil.getDistance(owner, x, y, z);
        float minDist = (route == null ? minRandomDistance : 2);
        if (dist <= minDist) {
            setWalkingToNextPoint(false);
            getNextTime();
        }
    }

    /**
     * set next route point if not set and time is ready
     */
    private void walkToLocation() {
        if (!isWalkingToNextPoint() && nextMoveTime <= System.currentTimeMillis()) {
            setNextPosition();
            setWalkingToNextPoint(true);

            RouteStep step = randomPoint;
            if (route != null) {
                step = route.getRouteSteps().get(targetPosition);
            }

            float x = step.getX();
            float y = step.getY();
            float z = step.getZ();
            owner.getMoveController().setNewDirection(x, y, z);
            owner.getMoveController().schedule();
        }
    }

    private boolean isWalkingToNextPoint() {
        return isWalkingToNextPoint;
    }

    private void setWalkingToNextPoint(boolean value) {
        isWalkingToNextPoint = value;
    }

    private void setNextPosition() {
        if (route == null) {
            getNextRandomPoint();
            return;
        }

        if (isRandomWalk) {
            targetPosition = Rnd.get(0, route.getRouteSteps().size() - 1);
        } else {
            if (targetPosition < (route.getRouteSteps().size() - 1))
                targetPosition++;
            else
                targetPosition = 0;
        }
    }

    private void getNextTime() {
        int nextDelay;
        if (route == null) {
            nextDelay = Rnd.get(NpcMovementConfig.MINIMIMUM_DELAY, NpcMovementConfig.MAXIMUM_DELAY);
        } else {
            nextDelay = isRandomWalk ? Rnd.get(5, 60) : route.getRouteSteps().get(targetPosition).getRestTime();
        }
        nextMoveTime = System.currentTimeMillis() + nextDelay * 1000;
    }

    private void getNextRandomPoint() {
        float x = owner.getSpawn().getX() - halfWalkArea + Rnd.get() * walkArea;
        float y = owner.getSpawn().getY() - halfWalkArea + Rnd.get() * walkArea;
        randomPoint = new RouteStep(x, y, owner.getSpawn().getZ());
    }

    @Override
    public int getExecutionInterval() {
        return 2;
    }

    @Override
    public void onClear() {
        owner.getMoveController().stop();
    }
}
