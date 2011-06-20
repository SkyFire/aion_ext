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
 * @param type of skill(active, passive, etc)
 */
public enum SkillUsageType {
    ACTIVE,
    PASSIVE,
    TOGGLE, // 5 occurances
    MAINTAIN //1 occurance
}
