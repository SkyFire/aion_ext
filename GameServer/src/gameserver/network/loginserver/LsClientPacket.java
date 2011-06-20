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
package gameserver.network.loginserver;

import com.aionemu.commons.network.packet.BaseClientPacket;
import org.apache.log4j.Logger;

/**
 * @author -Nemesiss-
 */
public abstract class LsClientPacket extends BaseClientPacket<LoginServerConnection> implements Cloneable {
    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(LsClientPacket.class);

    /**
     * Constructs new client packet with specified opcode. If using this constructor, user must later manually set
     * buffer and connection.
     *
     * @param opcode packet id
     */
    protected LsClientPacket(int opcode) {
        super(opcode);
    }

    /**
     * run runImpl catching and logging Throwable.
     */
    public final void run() {
        try {
            runImpl();
        }
        catch (Throwable e) {
            log.warn("error handling ls (" + getConnection().getIP() + ") message " + this, e);
        }
    }

    /**
     * Send new LsServerPacket to connection that is owner of this packet. This method is equivalent to:
     * getConnection().sendPacket(msg);
     *
     * @param msg
     */
    protected void sendPacket(LsServerPacket msg) {
        getConnection().sendPacket(msg);
    }

    /**
     * Clones this packet object.
     *
     * @return LsClientPacket
     */
    public LsClientPacket clonePacket() {
        try {
            return (LsClientPacket) super.clone();
        }
        catch (CloneNotSupportedException e)
		{
			return null;
		}
	}
}
