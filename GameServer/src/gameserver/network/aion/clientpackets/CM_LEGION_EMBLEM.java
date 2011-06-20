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

import gameserver.model.legion.Legion;
import gameserver.model.legion.LegionEmblem;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_LEGION_EMBLEM;
import gameserver.services.LegionService;

/**
 * @author Simple
 */
public class CM_LEGION_EMBLEM extends AionClientPacket {

    private int legionId;

    public CM_LEGION_EMBLEM(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        legionId = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Legion legion = LegionService.getInstance().getLegion(legionId);
        if (legion != null) {
            LegionEmblem legionEmblem = legion.getLegionEmblem();
            sendPacket(new SM_LEGION_EMBLEM(legionId, legionEmblem.getEmblemVer(), legionEmblem.getColor_r(), legionEmblem.getColor_g(), legionEmblem.getColor_b(), legion.getLegionName(), legionEmblem.getIsCustom()));
        }
    }
}
