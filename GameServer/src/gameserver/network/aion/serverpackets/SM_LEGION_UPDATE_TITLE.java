/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author sweetkr
 */
public class SM_LEGION_UPDATE_TITLE extends AionServerPacket {
    private int objectId;
    private int legionId;
    private String legionName;
    private int rank;

    public SM_LEGION_UPDATE_TITLE(int objectId, int legionId, String legionName, int rank) {
        this.objectId = objectId;
        this.legionId = legionId;
        this.legionName = legionName;
        this.rank = rank;
    }

    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, objectId);
        writeD(buf, legionId);
        writeS(buf, legionName);
        writeC(buf, rank); // 0: commander(?), 1: centurion, 2: soldier
    }
}
