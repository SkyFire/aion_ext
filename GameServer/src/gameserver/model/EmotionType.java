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
package gameserver.model;

/**
 * @author lyahim
 * @author 2.0 Magenik, Ares/Kaipo
 */
public enum EmotionType
{
	UNK(-1),
	SELECT_TARGET(0),
	JUMP(1),
	SIT(2),
	STAND(3),
	CHAIR_SIT(4),
	CHAIR_UP(5),
	START_FLYTELEPORT(6),
	LAND_FLYTELEPORT(7),
	START_WINDSTREAM(8), // 2.0
	BOOST_WINDSTREAM(11), // 2.0
	FLY(13),
	LAND(14),
	DIE(18),
	RESURRECT(19),
	EMOTE(21),
	END_DUEL(22), // What? Duel? It's the end of a emote
	ATTACKMODE(24), // Attack mode, by game
	NEUTRALMODE(25), // Attack mode, by game
	WALK(26),
	RUN(27),
	SWITCH_DOOR(31),
	START_EMOTE(32), // It's not "emote". NPCs?
	OPEN_PRIVATESHOP(33),
	CLOSE_PRIVATESHOP(34),
	START_EMOTE2(35), //It's not "emote". Triggered after Attack Mode of npcs
	POWERSHARD_ON(36),
	POWERSHARD_OFF(37),
	ATTACKMODE2(38), // It's the Attack toggled by player
	NEUTRALMODE2(39), // It's Neutral toggled by player
	START_LOOT(40),
	END_LOOT(41),
	START_QUESTLOOT(42),
	END_QUESTLOOT(43);
	
	private int id;
	
	private EmotionType(int id)
	{
		this.id = id;
	}
	
	public int getTypeId()
	{
		return id;
	}
	
	
	public static EmotionType getEmotionTypeById(int id)
	{
		for(EmotionType emotionType : values())
		{
			if(emotionType.getTypeId() == id)
				return emotionType;
		}
		return UNK;
	}
	
}
