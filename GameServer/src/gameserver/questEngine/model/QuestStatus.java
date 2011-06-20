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
package gameserver.questEngine.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * @author MrPoke
 */

@XmlEnum
public enum QuestStatus {
    NONE(0),
    START(3),
    REWARD(4),
    COMPLETE(5),
    LOCKED(6);

    private int id;

    private QuestStatus(int id) {
        this.id = id;
    }

    public int value() {
        return id;
    }
}
