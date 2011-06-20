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
import gameserver.model.legion.LegionHistory;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_LEGION_TABS;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

import java.util.Collection;

/**
 * @author Simple
 */
public class CM_LEGION_TABS extends AionClientPacket {
    private static final Logger log = Logger.getLogger(CM_LEGION_TABS.class);

    /**
     * exOpcode and the rest
     */
    private int page;
    private int tab;

    /**
     * Constructs new instance of CM_LEGION packet
     *
     * @param opcode
     */
    public CM_LEGION_TABS(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        page = readD();
        tab = readC();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();
        Collection<LegionHistory> history = activePlayer.getLegion().getLegionHistory();

        /**
         * Max page is 3 for legion history
         */
        if (page > 3)
            return;

        /**
         * If history size is less than page*8 return
         */
        if (history.size() < page * 8)
            return;

        switch (tab) {
            /**
             * History Tab
             */
            case 0:
                log.debug("Requested History Tab Page: " + page);
                if (!history.isEmpty())
                    PacketSendUtility.sendPacket(activePlayer, new SM_LEGION_TABS(history, page));
                break;
            /**
             * Reward Tab
             */
            case 1:
                log.debug("Requested Reward Tab Page: " + page);
                break;
		}
	}
}
