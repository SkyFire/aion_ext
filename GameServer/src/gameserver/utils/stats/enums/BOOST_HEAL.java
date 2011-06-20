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
 * @author PZIKO333
 */
public enum BOOST_HEAL {
    WARRIOR(0),
    GLADIATOR(0),
    TEMPLAR(0),
    SCOUT(0),
    ASSASSIN(0),
    RANGER(0),
    MAGE(0),
    SORCERER(0),
    SPIRIT_MASTER(0),
    PRIEST(100),
    CLERIC(100),
    CHANTER(100);

    private int value;

    private BOOST_HEAL(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
