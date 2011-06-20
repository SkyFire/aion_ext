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
package gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class GroupConfig {
    /**
     * Group
     */
    @Property(key = "gameserver.playergroup.removetime", defaultValue = "600")
    public static int GROUP_REMOVE_TIME;

    @Property(key = "gameserver.playergroup.maxdistance", defaultValue = "100")
    public static int GROUP_MAX_DISTANCE;

    @Property(key = "gameserver.playergroup.maxlevel.difference", defaultValue = "100")
    public static int GROUP_MAX_LEVEL_DIFFERENCE;

    @Property(key = "gameserver.playergroup.invite.other.race", defaultValue = "false")
    public static boolean GROUP_INVITE_OTHER_RACE;

    /**
     * Alliance
     */
    @Property(key = "gameserver.playeralliance.removetime", defaultValue = "600")
    public static int ALLIANCE_REMOVE_TIME;

    @Property(key = "gameserver.playeralliance.invite.other.race", defaultValue = "false")
    public static boolean ALLIANCE_INVITE_OTHER_RACE;
}
