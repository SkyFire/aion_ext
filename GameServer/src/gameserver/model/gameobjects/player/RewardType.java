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
package gameserver.model.gameobjects.player;

import gameserver.model.gameobjects.stats.StatEnum;

/**
 * @author antness
 */
public enum RewardType
{
	HUNTING
	{
		@Override
		public long calcReward(Player player, long reward)
		{
			float statRate = player.getGameStats().getCurrentStat(StatEnum.BOOST_HUNTING_XP_RATE) / 100f;
			return (long) (reward * player.getRates().getXpRate() * statRate);
		}
	},
	GROUP_HUNTING
	{
		@Override
		public long calcReward(Player player, long reward)
		{
			float statRate = player.getGameStats().getCurrentStat(StatEnum.BOOST_GROUP_HUNTING_XP_RATE) / 100f;
			return (long) (reward * player.getRates().getGroupXpRate() * statRate);
		}
	},
	QUEST
	{
		@Override
		public long calcReward(Player player, long reward)
		{
			float statRate = player.getGameStats().getCurrentStat(StatEnum.BOOST_QUEST_XP_RATE) / 100f;
			return (long) (reward * player.getRates().getQuestXpRate() * statRate);
		}
	},
	CRAFTING
	{
		@Override
		public long calcReward(Player player, long reward)
		{
			float statRate = player.getGameStats().getCurrentStat(StatEnum.BOOST_CRAFTING_XP_RATE) / 100f;
			return (long) (reward * player.getRates().getCraftingXPRate() * statRate);
		}
	},
	GATHERING
	{
		@Override
		public long calcReward(Player player, long reward)
		{
			float statRate = player.getGameStats().getCurrentStat(StatEnum.BOOST_GATHERING_XP_RATE) / 100f;
			return (long) (reward * player.getRates().getGatheringXPRate() * statRate);
		}
	};

	public abstract long calcReward(Player player, long reward);
}
