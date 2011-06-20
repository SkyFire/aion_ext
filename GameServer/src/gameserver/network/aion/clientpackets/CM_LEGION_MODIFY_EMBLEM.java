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
import gameserver.services.LegionService;

/**
 * @author Simple
 */
public class CM_LEGION_MODIFY_EMBLEM extends AionClientPacket {

    /**
     * Emblem related information *
     */
    private int legionId;
    private int emblemVer;
    private int red;
    private int green;
    private int blue;

    /**
     * @param opcode
     */
    public CM_LEGION_MODIFY_EMBLEM(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        legionId = readD();
        emblemVer = readH();
        readC(); // 0xFF is default; sets the transparency
        red = readC();
        green = readC();
        blue = readC();
    }

    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();

        if (activePlayer.isLegionMember())
            LegionService.getInstance().storeLegionEmblem(activePlayer, legionId, emblemVer, red, green, blue, false);
    }
}
