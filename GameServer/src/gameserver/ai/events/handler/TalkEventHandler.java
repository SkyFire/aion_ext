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
package gameserver.ai.events.handler;

import gameserver.ai.AI;
import gameserver.ai.events.Event;
import gameserver.ai.state.AIState;
import gameserver.model.gameobjects.Npc;
import gameserver.utils.ThreadPoolManager;

/**
 * @author ATracer
 */
public class TalkEventHandler implements EventHandler {
    @Override
    public Event getEvent() {
        return Event.TALK;
    }

    @Override
    public void handleEvent(Event event, AI<?> ai) {
        final Npc owner = (Npc) ai.getOwner();

        if (owner.hasWalkRoutes()) {
            owner.getMoveController().setCanWalk(false);
            owner.getController().stopMoving();

            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    owner.getMoveController().setCanWalk(true);
                }
            }, 60000);
        }
        ai.setAiState(AIState.TALKING);
    }
}
