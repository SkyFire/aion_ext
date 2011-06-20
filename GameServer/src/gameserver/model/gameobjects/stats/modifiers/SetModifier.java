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

import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.gameobjects.stats.StatModifierPriority;

/**
 * @author xavier
 */
public class SetModifier extends SimpleModifier {
    @Override
    public int apply(int baseStat, int currentStat) {
        return value;
    }

    @Override
    public StatModifierPriority getPriority() {
        return StatModifierPriority.HIGH;
    }

    public static SetModifier newInstance(StatEnum stat, int value, boolean isBonus) {
        SetModifier m = new SetModifier();
        m.setStat(stat);
        m.setValue(value);
        m.setBonus(isBonus);
        m.nextId();
        return m;
    }
}
