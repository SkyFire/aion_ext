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
package gameserver.network.chatserver.clientpackets;

import gameserver.network.chatserver.CsClientPacket;
import gameserver.services.ChatService;
import org.apache.log4j.Logger;

/**
 * @author ATracer
 */
public class CM_CS_PLAYER_AUTH_RESPONSE extends CsClientPacket {
    protected static final Logger log = Logger.getLogger(CM_CS_PLAYER_AUTH_RESPONSE.class);

    /**
     * Player for which authentication was performed
     */
    private int playerId;
    /**
     * Token will be sent to client
     */
    private byte[] token;

    /**
     * @param opcode
     */
    public CM_CS_PLAYER_AUTH_RESPONSE(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        playerId = readD();
        int tokenLenght = readC();
        token = readB(tokenLenght);
    }

    @Override
    protected void runImpl() {
        ChatService.playerAuthed(playerId, token);
    }
}
