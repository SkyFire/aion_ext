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
 * @author zdead, LokiReborn
 */
public class SM_ABYSS_RANKING_LEGIONS extends AionServerPacket {

    private ArrayList<AbyssRankingResult> data;
    private Race race;

    public SM_ABYSS_RANKING_LEGIONS(ArrayList<AbyssRankingResult> data, Race race) {
        this.data = data;
        this.race = race;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        if (race == null)
            return;

        writeD(buf, race.getRaceId());// 0:Elyos 1:Asmo
        writeD(buf, Math.round(AbyssRankingService.getInstance().getTimeOfUpdate() / 1000));// Date
        writeD(buf, 0x01);// 0:Nothing 1:Update Table
        writeD(buf, 0x01);// 0:Nothing 1:Update Table
        if (data == null || data.isEmpty()) {
            writeH(buf, 0);
            return;
        }
        writeH(buf, data.size());// list size
        for (AbyssRankingResult rs : data) {
            writeD(buf, rs.getLegionRank());// Current Rank
            writeD(buf, rs.getLegionOldRank());// Old Rank
            writeD(buf, rs.getLegionId());// Legion Id
            writeD(buf, race.getRaceId());// 0:Elyos 1:Asmo
            writeC(buf, rs.getLegionLevel());// Legion Level
            writeD(buf, rs.getLegionMembers());// Legion Members
            writeQ(buf, rs.getLegionCP());// Contribution Points

            writeS(buf, rs.getLegionName());// Legion Name

            for (int size = 0; size < (62 - rs.getLegionName().length() * 2); size++) {
                writeC(buf, 0x00);
            }
            writeH(buf, 0x00);
        }
        data = null;
    }
}
