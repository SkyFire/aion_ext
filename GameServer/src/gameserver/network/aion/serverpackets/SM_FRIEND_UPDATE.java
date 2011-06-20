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

import gameserver.model.gameobjects.player.Friend;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;

/**
 * Sent to update a player's status in a friendlist
 *
 * @author Ben
 */
public class SM_FRIEND_UPDATE extends AionServerPacket {
    private int friendObjId;

    private static Logger log = Logger.getLogger(SM_FRIEND_UPDATE.class);

    public SM_FRIEND_UPDATE(int friendObjId) {
        this.friendObjId = friendObjId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        Friend f = con.getActivePlayer().getFriendList().getFriend(friendObjId);
        if (f == null)
            log.debug("Attempted to update friend list status of " + friendObjId + " for " + con.getActivePlayer().getName() + " - object ID not found on friend list");
        else {
            writeS(buf, f.getName());
            writeD(buf, f.getLevel());
            writeD(buf, f.getPlayerClass().getClassId());
            writeC(buf, f.isOnline() ? 1 : 0); // Online status - No idea why this and f.getStatus are used
            writeD(buf, f.getMapId());
            writeD(buf, f.getLastOnlineTime()); // Date friend was last online as a Unix timestamp.
            writeS(buf, f.getNote());
            writeC(buf, f.getStatus().getIntValue());
        }
    }
}
