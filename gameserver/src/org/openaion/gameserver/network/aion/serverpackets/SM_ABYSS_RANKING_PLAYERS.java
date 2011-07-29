/*
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.openaion.gameserver.model.AbyssRankingResult;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.services.AbyssRankingService;


/**
 * @author Rhys2002, Sylar, LokiReborn
 */
public class SM_ABYSS_RANKING_PLAYERS extends AionServerPacket
{
	
	private ArrayList<AbyssRankingResult> 	data;
	private int 							race;
	
	public SM_ABYSS_RANKING_PLAYERS(ArrayList<AbyssRankingResult> data, Race race)
	{
		this.data = data;
		this.race = race.getRaceId();
	}

	@Override	
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, race);// 0:Elyos 1:Asmo
		writeD(buf, Math.round(AbyssRankingService.getInstance().getTimeOfUpdate() / 1000));//TODO Date
		writeD(buf, 0x01);
		writeD(buf, 0x01);// 0:Nothing 1:Update Table
		writeH(buf, data.size());// list size
		
		for (AbyssRankingResult rs : data)
		{
			writeD(buf, rs.getTopRanking());// Current Rank
			writeD(buf, rs.getPlayerRank());// AbyssRank
			writeD(buf, rs.getOldRanking());// Old Rank, TODO: build history table and schedule hourly refresh
			writeD(buf, rs.getPlayerId()); // PlayerID
			writeD(buf, race);
			writeD(buf, rs.getPlayerClass().getClassId());// Class Id
			writeD(buf, 0); // Sex ? 0=male / 1=female
			writeD(buf, rs.getPlayerAP());// Abyss Points
			writeD(buf, 0); // Unk
			writeH(buf, rs.getPlayerLevel());
			
			writeS(buf, rs.getPlayerName());// Player Name

		    writeB(buf, new byte[52 - (rs.getPlayerName().length() * 2 + 2)]);

			if(rs.getLegionName() == null)
			{
			writeB(buf, new byte[80]);
			}
			else
			{
			writeS(buf, rs.getLegionName());// Legion Name
			writeB(buf, new byte[78 - (rs.getLegionName().length() * 2)]);
			}
			writeH(buf, 0x00);
		}
		
		data = null;
		
	}
}
