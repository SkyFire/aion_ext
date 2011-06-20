/*
 * This file is part of Aion X EMU <aionxemu>.
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.skillengine.model.learn;

/**
 * @author ATracer
 * @param all class char to skill
 */
public enum SkillClass {
    /**
     * Positioning in enum should correspond to com.aionemu.gameserver.model.PlayerClass *
     */
    WARRIOR,
    FIGHTER,
    KNIGHT,
    SCOUT,
    ASSASSIN,
    RANGER,
    MAGE,
    WIZARD,
    ELEMENTALLIST,
    CLERIC, //in reality priest
    PRIEST, //in reality cleric
    CHANTER,
    ALL
}
