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
import gameserver.services.AllianceService;
import gameserver.services.GroupService;

/**
 * @author Sweetkr
 * @author Simple
 */
public class CM_SHOW_BRAND extends AionClientPacket {

    private int brandId;
    private int targetObjectId;

    /**
     * @param opcode
     */
    public CM_SHOW_BRAND(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
    	@SuppressWarnings("unused")
		int unk1 = readD(); //unknown always 1
        brandId = readD();
        targetObjectId = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();

        if (player == null)
            return;

        if (player.isInGroup())
            GroupService.getInstance().showBrand(player.getPlayerGroup(), brandId, targetObjectId);
        if (player.isInAlliance())
            AllianceService.getInstance().showBrand(player.getPlayerAlliance(), brandId, targetObjectId);
    }
}
