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

import com.aionemu.commons.utils.ExitCode;
import gameserver.network.chatserver.ChatServerConnection.State;
import gameserver.network.chatserver.CsClientPacket;
import gameserver.network.chatserver.serverpackets.SM_CS_AUTH;
import gameserver.services.ChatService;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

/**
 * @author ATracer
 */
public class CM_CS_AUTH_RESPONSE extends CsClientPacket {

    /**
     * Logger for this class.
     */
    protected static final Logger log = Logger.getLogger(CM_CS_AUTH_RESPONSE.class);

    /**
     * Response: 0=Authed,<br>
     * 1=NotAuthed,<br>
     * 2=AlreadyRegistered
     */
    private int response;
    private byte[] ip;
    private int port;

    /**
     * @param opcode
     */
    public CM_CS_AUTH_RESPONSE(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        response = readC();
        ip = readB(4);
        port = readH();
    }

    @Override
    protected void runImpl() {
        switch (response) {
            case 0: // Authed
                log.info("GameServer authed successfully IP : " + (ip[0] & 0xFF) + "." + (ip[1] & 0xFF) + "." + (ip[2] & 0xFF) + "." + (ip[3] & 0xFF) + " Port: " + port);
                getConnection().setState(State.AUTHED);
                ChatService.setIp(ip);
                ChatService.setPort(port);
                break;
            case 1: // NotAuthed
                log.fatal("GameServer is not authenticated at ChatServer side");
                System.exit(ExitCode.CODE_ERROR);
                break;
            case 2: // AlreadyRegistered
                log.info("GameServer is already registered at ChatServer side! trying again...");
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        CM_CS_AUTH_RESPONSE.this.getConnection().sendPacket(new SM_CS_AUTH());
                    }

                }, 10000);
                break;
        }
    }
}
