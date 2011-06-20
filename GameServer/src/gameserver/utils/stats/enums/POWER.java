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
public enum POWER {
    WARRIOR(110),
    GLADIATOR(115),
    TEMPLAR(115),
    SCOUT(100),
    ASSASSIN(110),
    RANGER(90),
    MAGE(90),
    SORCERER(90),
    SPIRIT_MASTER(90),
    PRIEST(95),
    CLERIC(105),
    CHANTER(110);

    private int value;

    private POWER(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
