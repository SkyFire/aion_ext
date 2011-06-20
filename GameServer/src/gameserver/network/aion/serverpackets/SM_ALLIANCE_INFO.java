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

import gameserver.model.alliance.PlayerAlliance;
import gameserver.model.alliance.PlayerAllianceMember;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author Sarynth (Thx Rhys2002 for Packets)
 */
public class SM_ALLIANCE_INFO extends AionServerPacket {
    private PlayerAlliance alliance;

    public SM_ALLIANCE_INFO(PlayerAlliance alliance) {
        this.alliance = alliance;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeH(buf, 4);
        writeD(buf, alliance.getObjectId());
        writeD(buf, alliance.getCaptainObjectId());

        List<Integer> ids = alliance.getViceCaptainObjectIds();
        writeD(buf, ids.size() > 0 ? ids.get(0) : 0); // viceLeader1
        writeD(buf, ids.size() > 1 ? ids.get(1) : 0); // viceLeader2
        writeD(buf, ids.size() > 2 ? ids.get(2) : 0); // viceLeader3
        writeD(buf, ids.size() > 3 ? ids.get(3) : 0); // viceLeader4

        writeD(buf, 0); //loot rule type - 0 freeforall, 1 roundrobin, 2 leader
        writeD(buf, 0); //autoDistribution - 0 or 1
        writeD(buf, 0); //this.common_item_above); - 0 normal 2 roll 3 bid
        writeD(buf, 0); //this.superior_item_above); - 0 normal 2 roll 3 bid
        writeD(buf, 0); //this.heroic_item_above); - 0 normal 2 roll 3 bid
        writeD(buf, 0); //this.fabled_item_above); - 0 normal 2 roll 3 bid
        writeD(buf, 0); //this.ethernal_item_above); - 0 normal 2 roll 3 bid
        writeD(buf, 0); //this.over_ethernal); - 0 normal 2 roll 3 bid
        writeD(buf, 0); //this.over_over_ethernal); - 0 normal 2 roll 3 bid
        writeC(buf, 0); //unk

        writeD(buf, 0); //unk
        
        writeD(buf, 0); // allianceGroupNumber 1
        writeD(buf, 1000); // allianceId 1
        writeD(buf, 1); // allianceGroupNumber 2
        writeD(buf, 1001); // allianceId 1
        writeD(buf, 2); // allianceGroupNumber 3
        writeD(buf, 1002); // allianceId 1
        writeD(buf, 3); // allianceGroupNumber 4
        writeD(buf, 1003); // allianceId 1

        //writeD(buf, 0); //unk
        //writeH(buf, 0); //unk
        
        writeD(buf, 0); //unk
        writeD(buf, 0); //unk
        writeC(buf, 0); //unk
    }
}
