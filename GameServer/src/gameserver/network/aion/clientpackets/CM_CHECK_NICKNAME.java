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
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.serverpackets.SM_CREATE_CHARACTER;
import gameserver.network.aion.serverpackets.SM_NICKNAME_CHECK_RESPONSE;
import gameserver.services.PlayerService;

/**
 * In this packets aion client is asking if given nickname is ok/free?.
 *
 * @author -Nemesiss-
 */
public class CM_CHECK_NICKNAME extends AionClientPacket {
    /**
     * nick name that need to be checked
     */
    private String nick;

    /**
     * Constructs new instance of <tt>CM_CHECK_NICKNAME </tt> packet
     *
     * @param opcode
     */
    public CM_CHECK_NICKNAME(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        nick = readS();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        AionConnection client = getConnection();

        if (!PlayerService.isValidName(nick)) {
            client.sendPacket(new SM_NICKNAME_CHECK_RESPONSE(SM_CREATE_CHARACTER.RESPONSE_INVALID_NAME));
        } else if (!PlayerService.isFreeName(nick)) {
            client.sendPacket(new SM_NICKNAME_CHECK_RESPONSE(SM_CREATE_CHARACTER.RESPONSE_NAME_ALREADY_USED));
        } else {
            client.sendPacket(new SM_NICKNAME_CHECK_RESPONSE(SM_CREATE_CHARACTER.RESPONSE_OK));
		}
	}
}
