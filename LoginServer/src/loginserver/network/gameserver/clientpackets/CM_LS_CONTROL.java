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

package loginserver.network.gameserver.clientpackets;

import com.aionemu.commons.database.dao.DAOManager;
import loginserver.dao.AccountDAO;
import loginserver.model.Account;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.serverpackets.SM_LS_CONTROL_RESPONSE;

import java.nio.ByteBuffer;

/**
 * @author Aionchs-Wylovech
 */
public class CM_LS_CONTROL extends GsClientPacket {
    private String accountName;

    private int param;

    private int type;

    private String playerName;

    private String adminName;

    private boolean result;

    public CM_LS_CONTROL(ByteBuffer buf, GsConnection client) {
        super(buf, client, 0x05);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {

        type = readC();
        adminName = readS();
        accountName = readS();
        playerName = readS();
        param = readC();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {

        Account account = DAOManager.getDAO(AccountDAO.class).getAccount(accountName);
        switch (type) {
            case 1:
                account.setAccessLevel((byte) param);
                break;
            case 2:
                account.setMembership((byte) param);
                break;
        }
        result = DAOManager.getDAO(AccountDAO.class).updateAccount(account);
        sendPacket(new SM_LS_CONTROL_RESPONSE(type, result, playerName, account.getId(), param, adminName));
    }
}        
