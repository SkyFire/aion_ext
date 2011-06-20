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

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.configs.main.CustomConfig;
import gameserver.dao.PlayerPasskeyDAO;
import gameserver.model.account.CharacterPasskey.ConnectType;
import gameserver.model.account.PlayerAccountData;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.serverpackets.SM_CHARACTER_SELECT;
import gameserver.network.aion.serverpackets.SM_DELETE_CHARACTER;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.PlayerService;

/**
 * In this packets aion client is requesting deletion of character.
 *
 * @author -Nemesiss-
 */
public class CM_DELETE_CHARACTER extends AionClientPacket {
    /**
     * PlayOk2 - we dont care...
     */
    @SuppressWarnings("unused")
    private int playOk2;
    /**
     * ObjectId of character that should be deleted.
     */
    private int chaOid;

    /**
     * Constructs new instance of <tt>CM_DELETE_CHARACTER </tt> packet
     *
     * @param opcode
     */
    public CM_DELETE_CHARACTER(int opcode) {
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
        AionConnection client = getConnection();
        PlayerAccountData playerAccData = client.getAccount().getPlayerAccountData(chaOid);
        if (playerAccData != null && !playerAccData.isLegionMember()) {
            // passkey check
            if (CustomConfig.PASSKEY_ENABLE && !client.getAccount().getCharacterPasskey().isPass()) {
                client.getAccount().getCharacterPasskey().setConnectType(ConnectType.DELETE);
                client.getAccount().getCharacterPasskey().setObjectId(chaOid);
                boolean isExistPasskey = DAOManager.getDAO(PlayerPasskeyDAO.class).existCheckPlayerPasskey(client.getAccount().getId());

                if (!isExistPasskey)
                    client.sendPacket(new SM_CHARACTER_SELECT(0));
                else
                    client.sendPacket(new SM_CHARACTER_SELECT(1));
            } else {
                PlayerService.deletePlayer(playerAccData);
                client.sendPacket(new SM_DELETE_CHARACTER(chaOid, playerAccData.getDeletionTimeInSeconds()));
            }
        } else {
            client.sendPacket(SM_SYSTEM_MESSAGE.STR_DELETE_CHARACTER_IN_LEGION());
		}		
	}
}
