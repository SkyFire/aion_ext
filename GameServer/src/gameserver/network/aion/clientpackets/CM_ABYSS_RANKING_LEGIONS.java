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


package gameserver.network.aion.clientpackets;

import gameserver.model.AbyssRankingResult;
import gameserver.model.Race;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_ABYSS_RANKING_LEGIONS;
import gameserver.services.AbyssRankingService;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * In this packets aion client is asking for legions abyss rankings
 *
 * @author zdead
 */
public class CM_ABYSS_RANKING_LEGIONS extends AionClientPacket {

    private Race queriedRace;
    private int raceId;

    private static final Logger log = Logger.getLogger(CM_ABYSS_RANKING_LEGIONS.class);

    public CM_ABYSS_RANKING_LEGIONS(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        raceId = readC();
        switch (raceId) {
            case 0:
                queriedRace = Race.ELYOS;
                break;
            case 1:
                queriedRace = Race.ASMODIANS;
                break;
        }
    }

    /**
     * {@inheritDoc}n
     */
    @Override
    protected void runImpl() {
        // calculate rankings and send packet
        if (queriedRace != null) {
            ArrayList<AbyssRankingResult> results = AbyssRankingService.getInstance().getLegions(queriedRace);
            PacketSendUtility.sendPacket(getConnection().getActivePlayer(), new SM_ABYSS_RANKING_LEGIONS(results, queriedRace));
        } else {
            log.warn("Received invalid raceId: " + raceId);
        }
    }
}
