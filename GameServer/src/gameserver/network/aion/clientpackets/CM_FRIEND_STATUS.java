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

import gameserver.model.gameobjects.player.FriendList.Status;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;

/**
 * Packet received when a user changes his buddylist status
 *
 * @author Ben
 */
public class CM_FRIEND_STATUS extends AionClientPacket {

    //The users new status
    private int status;

    public CM_FRIEND_STATUS(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        status = readC();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();
        Status statusEnum = Status.getByValue(status);
        if (statusEnum == null)
            statusEnum = Status.ONLINE;
        activePlayer.getFriendList().setStatus(statusEnum);

    }

}
