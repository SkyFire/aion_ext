/**
 * This file is part of Aion X Emu <aionxemu.com>
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.taskmanager.tasks;

import gameserver.model.alliance.PlayerAllianceEvent;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.GroupEvent;
import gameserver.services.AllianceService;
import gameserver.taskmanager.AbstractIterativePeriodicTaskManager;

/**
 * @author Sarynth
 *         <p/>
 *         Supports PlayerGroup and PlayerAlliance movement updating.
 */
public final class GroupUpdater extends AbstractIterativePeriodicTaskManager<Player> {
    private static final class SingletonHolder {
        private static final GroupUpdater INSTANCE = new GroupUpdater();
    }

    public static GroupUpdater getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public GroupUpdater() {
        super(2000);
    }

    @Override
    protected void callTask(Player player) {
        if (player.isInGroup())
            player.getPlayerGroup().updateGroupUIToEvent(player, GroupEvent.MOVEMENT);

        if (player.isInAlliance())
            AllianceService.getInstance().updateAllianceUIToEvent(player, PlayerAllianceEvent.MOVEMENT);

        // Remove task from list. It will be re-added if player moves again.
        this.stopTask(player);
    }

    @Override
    protected String getCalledMethodName() {
        return "groupAllianceUpdate()";
    }

}
