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

/**
 * @author dragoon112
 */
public class CM_REMOVE_ALTERED_STATE extends AionClientPacket {

    private int skillid;

    /**
     * @param opcode
     */
    public CM_REMOVE_ALTERED_STATE(int opcode) {
        super(opcode);
    }

    /* (non-Javadoc)
      * @see com.aionemu.commons.network.packet.BaseClientPacket#readImpl()
      */

    @Override
    protected void readImpl() {
        skillid = readH();

    }

    /* (non-Javadoc)
      * @see com.aionemu.commons.network.packet.BaseClientPacket#runImpl()
      */

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        player.getEffectController().removeEffect(skillid);
    }

}
