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
public enum ACCURACY {
    WARRIOR(100),
    GLADIATOR(100),
    TEMPLAR(100),
    SCOUT(110),
    ASSASSIN(110),
    RANGER(100),
    MAGE(95),
    SORCERER(100),
    SPIRIT_MASTER(100),
    PRIEST(100),
    CLERIC(100),
    CHANTER(90);

    private int value;

    private ACCURACY(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
