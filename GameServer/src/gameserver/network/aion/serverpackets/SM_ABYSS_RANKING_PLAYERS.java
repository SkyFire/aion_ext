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

package gameserver.network.aion.serverpackets;

import gameserver.model.AbyssRankingResult;
import gameserver.model.Race;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.AbyssRankingService;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * @author Rhys2002, zdead, LokiReborn
 */
public class SM_ABYSS_RANKING_PLAYERS extends AionServerPacket {

    private ArrayList<AbyssRankingResult> data;
    private int race;

    public SM_ABYSS_RANKING_PLAYERS(ArrayList<AbyssRankingResult> data, Race race) {
        this.data = data;
        this.race = race.getRaceId();
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
		if (data == null)
			return;
        writeD(buf, race);// 0:Elyos 1:Asmo
        writeD(buf, Math.round(AbyssRankingService.getInstance().getTimeOfUpdate() / 1000));//TODO Date
        writeD(buf, 0x01);
        writeD(buf, 0x01);// 0:Nothing 1:Update Table
        writeH(buf, data.size());// list size

        for (AbyssRankingResult rs : data) {
            writeD(buf, rs.getTopRanking());// Current Rank
            writeD(buf, rs.getPlayerRank());// AbyssRank
            writeD(buf, rs.getOldRanking());// Old Rank, TODO: build history table and schedule hourly refresh
            writeD(buf, rs.getPlayerId()); // PlayerID
            writeD(buf, race);
            writeD(buf, rs.getPlayerClass().getClassId());// Class Id
            writeC(buf, 0); // Sex ? 0=male / 1=female
            writeC(buf, 0); // Unk
            writeH(buf, 0); // Unk
            writeD(buf, rs.getPlayerAP());// Abyss Points
            writeD(buf, 0); // Unk
            writeH(buf, rs.getPlayerLevel());

            writeS(buf, rs.getPlayerName());// Player Name

            for (int size = 0; size < (30 - rs.getPlayerName().length() * 2); size++) {
                writeC(buf, 0x00);
            }
            writeH(buf, 0x00);
            writeD(buf, 0x00);
            writeD(buf, 0x00);
            if (rs.getLegionName() == null) {
                writeS(buf, "");// Legion Name
                for (int size = 0; size < 62; size++) {
                    writeC(buf, 0x00);
                }
            } else {
                writeS(buf, rs.getLegionName());// Legion Name
                for (int size = 0; size < (62 - rs.getLegionName().length() * 2); size++) {
                    writeC(buf, 0x00);
                }
            }
            writeH(buf, 0x00);
        }

        data = null;

    }
}
