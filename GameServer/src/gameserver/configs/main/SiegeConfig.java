/**
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

/**
 * @author Sarynth
 */
public class SiegeConfig {
    /**
     * Basic Siege Config
     */

    /**
     * Siege Enabled
     */
    @Property(key = "gameserver.siege.enabled", defaultValue = "true")
    public static boolean SIEGE_ENABLED;

    /**
     * Siege Timer Interval
     */
    @Property(key = "gameserver.siege.interval", defaultValue = "7200")
    public static int SIEGE_TIMER_INTERVAL;

    @Property(key = "gameserver.siege.vulnerable.chance", defaultValue = "50")
    public static int SIEGE_VULNERABLE_CHANCE;

    /**
     * Siege Location Values
     */
    @Property(key = "gameserver.siege.influence.fortress", defaultValue = "10")
    public static int SIEGE_POINTS_FORTRESS;

    @Property(key = "gameserver.siege.influence.artifact", defaultValue = "1")
    public static int SIEGE_POINTS_ARTIFACT;

    /**
     * Reward when general is killed
     */
    @Property(key = "gameserver.siege.apreward.default", defaultValue = "9000")
    public static int SIEGE_AP_REWARD_DEFAULT;

    @Property(key = "gameserver.siege.apreward.divine", defaultValue = "40000")
    public static int SIEGE_AP_REWARD_DIVINE;
}
