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
import gameserver.services.LegionService;

/**
 * @author LokiReborn
 */
public class CM_LEGION_EMBLEM_SEND extends AionClientPacket {

    private int legionId;

    public CM_LEGION_EMBLEM_SEND(int opcode) {
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
            if (legionEmblem.getCustomEmblemData() == null) return;
            LegionService.getInstance().sendCustomLegionPacket(getConnection().getActivePlayer(), legionEmblem, legionId, legion.getLegionName());
        }
    }
}
