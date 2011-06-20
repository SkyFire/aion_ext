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
package gameserver.utils.stats.enums;

/**
 * @author ATracer
 */
public enum SPEED {
    WARRIOR(6),
    GLADIATOR(6),
    TEMPLAR(6),
    SCOUT(6),
    ASSASSIN(6),
    RANGER(6),
    MAGE(6),
    SORCERER(6),
    SPIRIT_MASTER(6),
    PRIEST(6),
    CLERIC(6),
    CHANTER(6);

    private int value;

    private SPEED(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
