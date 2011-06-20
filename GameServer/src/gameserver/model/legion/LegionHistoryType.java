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
package gameserver.model.legion;

/**
 * @author Simple
 */
public enum LegionHistoryType {
    CREATE(0), // No parameters
    JOIN(1), // Parameter: name
    KICK(2), // Parameter: name
    LEVEL_UP(3), // Parameter: legion level
    APPOINTED(4), // Parameter: legion level
    EMBLEM_REGISTER(5), // No parameters
    EMBLEM_MODIFIED(6); // No parameters

    private byte historyType;

    private LegionHistoryType(int historyType) {
        this.historyType = (byte) historyType;
    }

    /**
     * Returns client-side id for this
     *
     * @return byte
     */
    public byte getHistoryId() {
        return this.historyType;
	}
}
