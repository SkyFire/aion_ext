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
package gameserver.network.aion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Vyaslav, Ares/Kaipo
 */
public abstract class AionServerPacketSeq {
    private ArrayList<AionServerPacket> packetSeq;

    /**
     * Constructs new server packet
     */
    protected AionServerPacketSeq() {
        packetSeq = new ArrayList<AionServerPacket>();
    }

    public void addPacket(AionServerPacket packet) {
        packetSeq.add(packet);
    }

    public final Collection<AionServerPacket> getPacketSeq() {
        return Collections.unmodifiableList(packetSeq);
    }

}
