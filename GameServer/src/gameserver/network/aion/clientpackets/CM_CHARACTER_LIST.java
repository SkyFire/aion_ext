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
import gameserver.network.aion.serverpackets.SM_CHARACTER_LIST;

/**
 * In this packets aion client is requesting character list.
 *
 * @author -Nemesiss-
 */
public class CM_CHARACTER_LIST extends AionClientPacket {
    /**
     * PlayOk2 - we dont care...
     */
    private int playOk2;

    /**
     * Constructs new instance of <tt>CM_CHARACTER_LIST </tt> packet.
     *
     * @param opcode
     */
    public CM_CHARACTER_LIST(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        playOk2 = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        sendPacket(new SM_CHARACTER_LIST(playOk2));
    }
}
