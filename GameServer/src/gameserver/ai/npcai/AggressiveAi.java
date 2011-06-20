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
package gameserver.ai.npcai;

import gameserver.ai.events.EventHandlers;
import gameserver.ai.state.StateHandlers;

/**
 * @author ATracer
 */
public class AggressiveAi extends MonsterAi {
    public AggressiveAi() {
        super();
        /**
         * Event handlers
         */
        this.addEventHandler(EventHandlers.SEEPLAYER_EH.getHandler());
        this.addEventHandler(EventHandlers.NOTSEEPLAYER_EH.getHandler());

        /**
         * State handlers
         */
        this.addStateHandler(StateHandlers.ACTIVE_AGGRO_SH.getHandler());
    }
}
