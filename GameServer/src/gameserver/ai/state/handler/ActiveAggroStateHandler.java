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
package gameserver.ai.state.handler;

import gameserver.ai.AI;
import gameserver.ai.desires.impl.AggressionDesire;
import gameserver.ai.desires.impl.WalkDesire;
import gameserver.ai.events.Event;
import gameserver.ai.state.AIState;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.world.Executor;

/**
 * @author ATracer
 */
public class ActiveAggroStateHandler extends StateHandler {
    @Override
    public AIState getState() {
        return AIState.ACTIVE;
    }

    /**
     * State ACTIVE
     * AI AggressiveMonsterAi
     * AI GuardAi
     */
    @Override
    public void handleState(AIState state, final AI<?> ai) {
        ai.clearDesires();
        final Npc owner = (Npc) ai.getOwner();

        owner.getKnownList().doOnAllObjects(new Executor<AionObject>() {
            @Override
            public boolean run(AionObject object) {
                if (object instanceof Creature && owner.isAggressiveTo((Creature) object)) {
                    ai.addDesire(new AggressionDesire(owner, AIState.ACTIVE.getPriority()));
                    return false;
                }

                return true;
            }
        }, true);

        if (ai.desireQueueSize() == 0 && owner.hasWalkRoutes()) {
            ai.addDesire(new WalkDesire(owner, AIState.ACTIVE.getPriority()));
        }

        if (ai.desireQueueSize() == 0)
            ai.handleEvent(Event.NOTHING_TODO);
        else
            ai.schedule();
    }
}
