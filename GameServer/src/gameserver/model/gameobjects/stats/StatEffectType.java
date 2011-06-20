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
package gameserver.model.gameobjects.stats;

/**
 * @author xavier
 */
public enum StatEffectType {
    SKILL_EFFECT(1),
    ITEM_EFFECT(2),
    TITLE_EFFECT(3),
    STONE_EFFECT(4),
    ENCHANT_EFFECT(6),
    ITEM_SET_EFFECT(5);

    private int value;

    private StatEffectType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
