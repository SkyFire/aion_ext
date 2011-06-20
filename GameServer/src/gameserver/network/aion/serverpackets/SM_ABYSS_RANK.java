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

import gameserver.model.gameobjects.player.AbyssRank;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.utils.stats.AbyssRankEnum;

import java.nio.ByteBuffer;

/**
 * @author Nemiroff
 *         Date: 25.01.2010
 */
public class SM_ABYSS_RANK extends AionServerPacket {
    private AbyssRank rank;
    private int currentRankId;

    public SM_ABYSS_RANK(AbyssRank rank) {
        this.rank = rank;
        this.currentRankId = rank.getRank().getId();
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeQ(buf, rank.getAp()); //curAP
        writeD(buf, currentRankId); //curRank
        writeD(buf, rank.getTopRanking()); //curRating

        int nextRankId = currentRankId < AbyssRankEnum.values().length ? currentRankId + 1 : currentRankId;
        writeD(buf, 100 * rank.getAp() / AbyssRankEnum.getRankById(nextRankId).getRequired()); //exp %

        writeD(buf, rank.getAllKill()); //allKill
        writeD(buf, rank.getMaxRank()); //maxRank

        writeD(buf, rank.getDailyKill()); //dayKill
        writeQ(buf, rank.getDailyAP()); //dayAP

        writeD(buf, rank.getWeeklyKill()); //weekKill
        writeQ(buf, rank.getWeeklyAP()); //weekAP

        writeD(buf, rank.getLastKill()); //laterKill
        writeQ(buf, rank.getLastAP()); //laterAP

        writeC(buf, 0x00); //unk
    }
}
