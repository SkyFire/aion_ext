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

import gameserver.ai.AI;
import gameserver.ai.desires.AbstractDesire;
import gameserver.ai.desires.MoveDesire;
import gameserver.ai.state.AIState;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.utils.MathUtil;

/**
 * @author Pinguin, ATracer
 */
public class MoveToTargetDesire extends AbstractDesire implements MoveDesire {
    private Npc owner;
    private Creature target;

    /**
     * @param crt
     * @param desirePower
     */
    public MoveToTargetDesire(Npc owner, Creature target, int desirePower) {
        super(desirePower);
        this.owner = owner;
        this.target = target;
    }

    @Override
    public boolean handleDesire(AI<?> ai) {
        if (owner == null || owner.getLifeStats().isAlreadyDead())
            return false;
        if (target == null || target.getLifeStats().isAlreadyDead())
            return false;

        owner.getMoveController().setFollowTarget(true);

        if (!owner.getMoveController().isScheduled())
            owner.getMoveController().schedule();

        double distance = owner.getMoveController().getDistanceToTarget();

        /** MoveToHome if Npc is too far away from home
         and not only if target is too far away **/
        if (owner.getSpawn() != null) {
            double dist = MathUtil.getDistance(owner.getX(), owner.getY(), owner.getZ(), owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ());
            if (dist > 100 && !owner.hasWalkRoutes()) {
                owner.getLifeStats().increaseHp(TYPE.NATURAL_HP, owner.getLifeStats().getMaxHp());
                ai.setAiState(AIState.MOVINGTOHOME);
                return false;
            }
        }

        if (distance > 125)
            return false;

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MoveToTargetDesire))
            return false;

        MoveToTargetDesire that = (MoveToTargetDesire) o;
        return target.equals(that.target);
    }

    /**
     * @return the target
     */
    public Creature getTarget() {
        return target;
    }

    @Override
    public int getExecutionInterval() {
        return 1;
    }

    @Override
    public void onClear() {
        owner.getMoveController().stop();
    }
}