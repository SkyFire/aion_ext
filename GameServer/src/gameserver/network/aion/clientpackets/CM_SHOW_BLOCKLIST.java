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

import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_BLOCK_LIST;

/**
 * Send when the client requests the blocklist
 *
 * @author Ben
 */
public class CM_SHOW_BLOCKLIST extends AionClientPacket {

    public CM_SHOW_BLOCKLIST(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        if (getConnection().getActivePlayer() != null && getConnection().getActivePlayer().getBlockList() != null) {
            sendPacket(new SM_BLOCK_LIST(getConnection().getActivePlayer().getBlockList()));
        }
    }
}
