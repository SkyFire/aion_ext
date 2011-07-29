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
package com.aionengine.chatserver.model.channel;

import com.aionengine.chatserver.model.ChannelType;
import com.aionengine.chatserver.model.PlayerClass;
import com.aionengine.chatserver.model.Race;

/**
 * @author ATracer
 */
public class JobChannel extends RaceChannel
{
	private PlayerClass	playerClass;

	/**
	 * 
	 * @param playerClass
	 * @param race
	 */
	public JobChannel(PlayerClass playerClass, Race race)
	{
		super(ChannelType.JOB, race);
		this.playerClass = playerClass;
		frenchAlias = "@\u0001job_";
		switch(this.playerClass)
		{
			case ASSASSIN: frenchAlias += "Assassin"; break;
			case CHANTER: frenchAlias += "A\u00e8de"; break;
			case CLERIC: frenchAlias += "Clerc"; break;
			case GLADIATOR: frenchAlias += "Gladiateur[f:\"Gladiatrice\"]"; break;
			case RANGER: frenchAlias += "R\u00f4deur[f:\"R\u00f4deuse\"]"; break;
			case SORCERER: frenchAlias += "Sorcier[f:\"Sorci\u00e8re\"]"; break;
			case SPIRIT_MASTER: frenchAlias += "Spiritualiste"; break;
			case TEMPLAR: frenchAlias += "Templier[f:\"Templi\u00e8re\"]"; break;
		}
		frenchAlias += "\u0001";
		frenchAlias += GameServerService.GAMESERVER_ID;
		frenchAlias += ".";
		if(race == Race.ASMODIANS)
			frenchAlias += "1";
		else
			frenchAlias += "0";
		frenchAlias += ".AION.KOR";
	}
	
	public byte[] getFrenchAlias()
	{
		return frenchAlias.getBytes(Charset.forName("UTF-16le"));
	}

	/**
	 * @return the playerClass
	 */
	public PlayerClass getPlayerClass()
	{
		return playerClass;
	}
}
