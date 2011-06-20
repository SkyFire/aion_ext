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

import gameserver.model.account.Account;
import gameserver.model.account.PlayerAccountData;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_RESTORE_CHARACTER;
import gameserver.services.PlayerService;

/**
 * In this packets aion client is requesting cancellation of character deleting.
 *
 * @author -Nemesiss-
 */
public class CM_RESTORE_CHARACTER extends AionClientPacket {
    /**
     * PlayOk2 - we dont care...
     */
    @SuppressWarnings("unused")
    private int playOk2;
    /**
     * ObjectId of character that deletion should be canceled
     */
    private int chaOid;

    /**
     * Constructs new instance of <tt>CM_RESTORE_CHARACTER </tt> packet
     *
     * @param opcode
     */
    public CM_RESTORE_CHARACTER(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        playOk2 = readD();
        chaOid = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Account account = getConnection().getAccount();
        PlayerAccountData pad = account.getPlayerAccountData(chaOid);

        boolean success = pad != null && PlayerService.cancelPlayerDeletion(pad);
        sendPacket(new SM_RESTORE_CHARACTER(chaOid, success));
    }
}
