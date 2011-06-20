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

import gameserver.model.legion.Legion;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Map;

/**
 * @author Simple
 */
public class SM_LEGION_INFO extends AionServerPacket {
    /**
     * Legion information *
     */
    private Legion legion;

    /**
     * This constructor will handle legion info
     *
     * @param legion
     */
    public SM_LEGION_INFO(Legion legion) {
        this.legion = legion;
    }

    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        writeS(buf, legion.getLegionName());
        writeC(buf, legion.getLegionLevel());
        writeD(buf, legion.getLegionRank());
        writeC(buf, legion.getCenturionPermission1());
        writeC(buf, legion.getCenturionPermission2());
        writeC(buf, legion.getLegionarPermission1());
        writeC(buf, legion.getLegionarPermission2());
        writeD(buf, legion.getContributionPoints());
        writeD(buf, 0x00); // unk
        writeD(buf, 0x00); // unk
        writeD(buf, 0x00); // unk

        /** Get Announcements List From DB By Legion **/
        Map<Timestamp, String> announcementList = legion.getAnnouncementList().descendingMap();

        /** Show max 7 announcements **/
        int i = 0;
        for (Timestamp unixTime : announcementList.keySet()) {
            writeS(buf, announcementList.get(unixTime));
            writeD(buf, (int) (unixTime.getTime() / 1000));
            i++;
            if (i >= 7)
                break;
        }

        if (legion.getLegionEmblem().getCustomEmblemData() == null)
            writeH(buf, 105);
        else
            writeH(buf, 108);
	}
}
