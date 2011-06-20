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
package gameserver.model.templates.spawn;

import gameserver.utils.gametime.DayTime;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlType(name = "SpawnTime")
@XmlEnum
public enum SpawnTime {
    DAY,
    NIGHT;

    /**
     * @param dayTime
     * @return true or false
     */
    public boolean isAllowedDuring(DayTime dayTime) {
        switch (this) {
            case DAY:
                return dayTime == DayTime.AFTERNOON || dayTime == DayTime.MORNING
                        || dayTime == DayTime.EVENING;
            case NIGHT:
                return dayTime == DayTime.NIGHT;
        }
        return true;
    }
}
