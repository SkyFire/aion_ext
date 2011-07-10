/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.configs.main;

import org.openaion.commons.configuration.Property;

public class EnchantsConfig
{
	/**
	 * ManaStone Rates
	 */
	@Property(key = "gameserver.manastone.percent", defaultValue = "57")
	public static int		MSPERCENT;
	@Property(key = "gameserver.manastone.percent1", defaultValue = "43")
	public static int		MSPERCENT1;
	@Property(key = "gameserver.manastone.percent2", defaultValue = "33")
	public static int		MSPERCENT2;
	@Property(key = "gameserver.manastone.percent3", defaultValue = "25")
	public static int		MSPERCENT3;
	@Property(key = "gameserver.manastone.percent4", defaultValue = "19")
	public static int		MSPERCENT4;
	@Property(key = "gameserver.manastone.percent5", defaultValue = "2")
	public static int		MSPERCENT5;
	
	/**
	 * Supplement Additional Success Rates
	 */
	@Property(key = "gameserver.supplement.lesser", defaultValue = "10")
	public static int		LSSUP;
	@Property(key = "gameserver.supplement.regular", defaultValue = "15")
	public static int		RGSUP;
	@Property(key = "gameserver.supplement.greater", defaultValue = "20")
	public static int		GRSUP;
}
