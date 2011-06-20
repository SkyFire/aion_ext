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
package gameserver.ai.state;

import gameserver.ai.state.handler.*;

/**
 * @author ATracer
 */
public enum StateHandlers {
    /**
     * AIState.MOVINGTOHOME
     */
    MOVINGTOHOME_SH(new MovingToHomeStateHandler()),
    /**
     * AIState.NONE
     */
    NONE_MONSTER_SH(new NoneNpcStateHandler()),
    /**
     * AIState.ATTACKING
     */
    ATTACKING_SH(new AttackingStateHandler()),
    /**
     * AIState.THINKING
     */
    THINKING_SH(new ThinkingStateHandler()),
    /**
     * AIState.ACTIVE
     */
    ACTIVE_NPC_SH(new ActiveNpcStateHandler()),
    ACTIVE_AGGRO_SH(new ActiveAggroStateHandler()),
    /**
     * AIState.RESTING
     */
    RESTING_SH(new RestingStateHandler()),
    /**
     * AIState.TALKING
     */
    TALKING_SH(new TalkingStateHandler());

    private StateHandler stateHandler;

    private StateHandlers(StateHandler stateHandler) {
        this.stateHandler = stateHandler;
    }

    public StateHandler getHandler() {
        return stateHandler;
    }
}
