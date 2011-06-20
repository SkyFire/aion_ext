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
 * @author Jego
 */
public class NpcMovementConfig {
    /**
     * NPC movement activator
     */
    @Property(key = "gameserver.npc.movement.active", defaultValue = "true")
    public static boolean ACTIVE_NPC_MOVEMENT;

    /**
     * Minimum movement delay
     */
    @Property(key = "gameserver.npc.movement.delay.minimum", defaultValue = "3")
    public static int MINIMIMUM_DELAY;

    /**
     * Maximum movement delay
     */
    @Property(key = "gameserver.npc.movement.delay.maximum", defaultValue = "15")
    public static int MAXIMUM_DELAY;
}
