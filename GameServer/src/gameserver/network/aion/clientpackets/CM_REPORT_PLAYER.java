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
import org.apache.log4j.Logger;

/**
 * Received when a player reports another player with /ReportAutoHunting
 *
 * @author Jego
 */
public class CM_REPORT_PLAYER extends AionClientPacket {
    private static final Logger log = Logger.getLogger(CM_REPORT_PLAYER.class);

    private String player;

    /**
     * A player gets reported.
     *
     * @param opcode
     */
    public CM_REPORT_PLAYER(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        readB(1); // unknown byte.
        player = readS(); // the name of the reported person.
    }

    @Override
    protected void runImpl() {
        Player p = getConnection().getActivePlayer();
        log.info("[AUDIT] " + p.getName() + " reports the player: " + player);
    }

}
