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
import gameserver.ai.desires.impl.WalkDesire;
import gameserver.ai.events.Event;
import gameserver.ai.state.AIState;
import gameserver.model.gameobjects.Npc;

/**
 * @author ATracer
 */
public class ActiveNpcStateHandler extends StateHandler {
    @Override
    public AIState getState() {
        return AIState.ACTIVE;
    }

    /**
     * State ACTIVE
     * AI NpcAi
     */
    @Override
    public void handleState(AIState state, AI<?> ai) {
        ai.clearDesires();
        Npc owner = (Npc) ai.getOwner();
        if (owner.hasWalkRoutes()) {
            ai.addDesire(new WalkDesire(owner, AIState.ACTIVE.getPriority()));
        }

        if (ai.desireQueueSize() == 0)
            ai.handleEvent(Event.NOTHING_TODO);
        else
            ai.schedule();
    }
}
