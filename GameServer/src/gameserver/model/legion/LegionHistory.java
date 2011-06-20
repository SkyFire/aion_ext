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

import java.sql.Timestamp;

/**
 * @author Simple
 */
public class LegionHistory {
    private LegionHistoryType legionHistoryType;
    private String name = "";
    private Timestamp time;

    public LegionHistory(LegionHistoryType legionHistoryType, String name, Timestamp time) {
        this.legionHistoryType = legionHistoryType;
        this.name = name;
        this.time = time;
    }

    /**
     * @return the legionHistoryType
     */
    public LegionHistoryType getLegionHistoryType() {
        return legionHistoryType;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the time
     */
    public Timestamp getTime() {
        return time;
    }
}
