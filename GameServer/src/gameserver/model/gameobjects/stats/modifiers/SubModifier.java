/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
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
package gameserver.model.gameobjects.stats.modifiers;

import gameserver.model.gameobjects.stats.StatModifierPriority;

/**
 * @author xavier
 */
public class SubModifier extends SimpleModifier {
    @Override
    public int apply(int baseStat, int currentStat) {
        if (isBonus()) {
            return Math.round(-1 * value);
        } else {
            return Math.round(baseStat - value);
        }
    }

    @Override
    public StatModifierPriority getPriority() {
        return StatModifierPriority.LOW;
    }
}
