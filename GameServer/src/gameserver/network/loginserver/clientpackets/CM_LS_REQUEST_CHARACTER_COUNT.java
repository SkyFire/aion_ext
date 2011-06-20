/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.loginserver.clientpackets;

import gameserver.network.loginserver.LsClientPacket;
import gameserver.network.loginserver.serverpackets.SM_LS_CHARACTER_COUNT;
import gameserver.services.AccountService;

/**
 * @author xavier
 *         <p/>
 *         Packet sent by login server to request account characters count
 */
public class CM_LS_REQUEST_CHARACTER_COUNT extends LsClientPacket {
    private int accountId;

    /**
     * @param opcode
     */
    public CM_LS_REQUEST_CHARACTER_COUNT(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        accountId = readD();
    }

    @Override
    protected void runImpl() {
        int characterCount = AccountService.getCharacterCountFor(accountId);
        sendPacket(new SM_LS_CHARACTER_COUNT(accountId, characterCount));
    }
}
