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
package gameserver.network.loginserver.clientpackets;

import com.aionemu.commons.utils.ExitCode;
import gameserver.network.loginserver.LoginServer;
import gameserver.network.loginserver.LoginServerConnection.State;
import gameserver.network.loginserver.LsClientPacket;
import gameserver.network.loginserver.serverpackets.SM_ACCOUNT_LIST;
import gameserver.network.loginserver.serverpackets.SM_GS_AUTH;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

/**
 * This packet is response for SM_GS_AUTH its notify Gameserver if registration was ok or what was wrong.
 *
 * @author -Nemesiss-
 */
public class CM_GS_AUTH_RESPONSE extends LsClientPacket {
    /**
     * Logger for this class.
     */
    protected static final Logger log = Logger.getLogger(CM_GS_AUTH_RESPONSE.class);

    /**
     * Response: 0=Authed,1=NotAuthed,2=AlreadyRegistered
     */
    private int response;

    /**
     * Constructs new instance of <tt>CM_GS_AUTH_RESPONSE </tt> packet.
     *
     * @param opcode
     */
    public CM_GS_AUTH_RESPONSE(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        response = readC();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        /**
         * Authed
         */
        if (response == 0) {
            getConnection().setState(State.AUTHED);
            sendPacket(new SM_ACCOUNT_LIST(LoginServer.getInstance().getLoggedInAccounts()));
        }

        /**
         * NotAuthed
         */
        else if (response == 1) {
            log.fatal("GameServer is not authenticated at LoginServer side, shutting down!");
            System.exit(ExitCode.CODE_ERROR);
        }
        /**
         * AlreadyRegistered
         */
        else if (response == 2) {
            log.info("GameServer is already registered at LoginServer side! trying again...");
            /**
             * try again after 10s
             */
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    CM_GS_AUTH_RESPONSE.this.getConnection().sendPacket(new SM_GS_AUTH());
                }

            }, 10000);
		}
	}
}
