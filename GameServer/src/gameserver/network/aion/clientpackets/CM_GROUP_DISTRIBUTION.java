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
import gameserver.restrictions.RestrictionsManager;
import gameserver.services.GroupService;

/**
 * @author Lyahim
 * @author Simple
 */
public class CM_GROUP_DISTRIBUTION extends AionClientPacket {

    private int amount;

    public CM_GROUP_DISTRIBUTION(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        amount = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        if (amount < 1)
            return;

        Player player = getConnection().getActivePlayer();

        if (!RestrictionsManager.canTrade(player))
            return;

        GroupService.getInstance().groupDistribution(player, amount);
    }
}
