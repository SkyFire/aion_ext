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

import gameserver.model.legion.LegionMemberEx;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * @author Simple
 */
public class SM_LEGION_MEMBERLIST extends AionServerPacket {
    private static final int OFFLINE = 0x00;
    private static final int ONLINE = 0x01;
    private ArrayList<LegionMemberEx> legionMembers;

    /**
     * This constructor will handle legion member info when a List of members is given
     *
     * @param ArrayList <LegionMemberEx> legionMembers
     */
    public SM_LEGION_MEMBERLIST(ArrayList<LegionMemberEx> legionMembers) {
        this.legionMembers = legionMembers;
    }

    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, 0x01);
        writeH(buf, (65536 - legionMembers.size()));
        for (LegionMemberEx legionMember : legionMembers) {
            writeD(buf, legionMember.getObjectId());
            writeS(buf, legionMember.getName());
            writeC(buf, legionMember.getPlayerClass().getClassId());
            writeD(buf, legionMember.getLevel());
            writeC(buf, legionMember.getRank().getRankId());
            writeD(buf, legionMember.getWorldId());
            writeC(buf, legionMember.isOnline() ? ONLINE : OFFLINE);
            writeS(buf, legionMember.getSelfIntro());
            writeS(buf, legionMember.getNickname());
            writeD(buf, legionMember.getLastOnline());
		}
	}
}
