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
 * @author Simple
 */
public class SM_LOGIN_QUEUE extends AionServerPacket {
    private int waitingPosition; // What is the player's position in line
    private int waitingTime; // Per waiting position in seconds
    private int waitingCount; // How many are waiting in line

    private SM_LOGIN_QUEUE() {
        this.waitingPosition = 5;
        this.waitingTime = 60;
        this.waitingCount = 50;
    }

    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, waitingPosition);
        writeD(buf, waitingTime);
        writeD(buf, waitingCount);
    }
}
