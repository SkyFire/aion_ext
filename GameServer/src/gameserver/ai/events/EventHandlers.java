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
package gameserver.ai.events;

import gameserver.ai.events.handler.*;

/**
 * @author ATracer
 */
public enum EventHandlers {
    ATTACKED_EH(new AttackedEventHandler()),
    TIREDATTACKING_EH(new TiredAttackingEventHandler()),
    MOST_HATED_CHANGED_EH(new TiredAttackingEventHandler()),
    SEEPLAYER_EH(new SeePlayerEventHandler()),
    NOTSEEPLAYER_EH(new NotSeePlayerEventHandler()),
    SEECREATURE_EH(new SeeCreatureEventHandler()),
    NOTSEECREATURE_EH(new NotSeeCreatureEventHandler()),
    RESPAWNED_EH(new RespawnedEventHandler()),
    BACKHOME_EH(new BackHomeEventHandler()),
    TALK_EH(new TalkEventHandler()),
    RESTOREDHEALTH_EH(new RestoredHealthEventHandler()),
    NOTHINGTODO_EH(new NothingTodoEventHandler()),
    DESPAWN_EH(new DespawnEventHandler()),
    DAYTIMECHANGE_EH(new DayTimeChangeEventHandler()),
    DIED_EH(new DiedEventHandler());

    private EventHandler eventHandler;

    private EventHandlers(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public EventHandler getHandler() {
        return eventHandler;
    }
}
