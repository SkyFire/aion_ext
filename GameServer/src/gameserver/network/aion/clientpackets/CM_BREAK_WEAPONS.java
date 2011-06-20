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

import gameserver.network.aion.AionClientPacket;
import gameserver.services.ArmsfusionService;

/**
 * @author zdead
 */
public class CM_BREAK_WEAPONS extends AionClientPacket {

    public CM_BREAK_WEAPONS(int opcode) {
        super(opcode);
    }

    private int weaponToBreakUniqueId;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        readD();
        weaponToBreakUniqueId = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        ArmsfusionService.breakWeapons(getConnection().getActivePlayer(), weaponToBreakUniqueId);
    }
}
