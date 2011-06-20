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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_CUSTOM_SETTINGS;
import gameserver.utils.PacketSendUtility;

/**
 * @author Sweetkr
 */
public class CM_CUSTOM_SETTINGS extends AionClientPacket {
    private int display;
    private int deny;

    public CM_CUSTOM_SETTINGS(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        /**
         * 1 : show legion mantle
         * 2 : priority equipment
         * 4 : show helmet
         */
        display = readH();
        /**
         * 1 : view detail player
         * 2 : trade
         * 4 : party/force
         * 8 : legion
         * 16 : friend
         * 32 : dual(pvp)
         */
        deny = readH();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();
        activePlayer.getPlayerSettings().setDisplay(display);
        activePlayer.getPlayerSettings().setDeny(deny);

        PacketSendUtility.broadcastPacket(activePlayer, new SM_CUSTOM_SETTINGS(activePlayer), true);
    }
}
