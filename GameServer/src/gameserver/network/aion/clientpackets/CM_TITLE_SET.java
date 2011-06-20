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
import gameserver.model.gameobjects.player.Title;
import gameserver.model.gameobjects.stats.listeners.TitleChangeListener;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_TITLE_INFO;
import gameserver.utils.PacketSendUtility;

/**
 * @author Nemiroff
 *         Date: 01.12.2009
 */
public class CM_TITLE_SET extends AionClientPacket {
    /**
     * Title id
     */
    private int titleId;

    /**
     * Constructs new instance of <tt>CM_TITLE_SET </tt> packet
     *
     * @param opcode
     */
    public CM_TITLE_SET(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        titleId = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        boolean isValidTitle = false;

        if (titleId != -1) {
            //check title exploit
            for (Title title : player.getTitleList().getTitles()) {
                if (title.getTemplate().getTitleId() == titleId) {
                    isValidTitle = true;
                    break;
                }
            }

            if (!isValidTitle)
                return;
        }

        sendPacket(new SM_TITLE_INFO(titleId));
        PacketSendUtility.broadcastPacket(player, (new SM_TITLE_INFO(player, titleId)));

        if (player.getCommonData().getTitleId() > 0)
            if (player.getGameStats() != null)
                TitleChangeListener.onTitleChange(player.getGameStats(), player.getCommonData().getTitleId(), false);

        player.getCommonData().setTitleId(titleId);
        if (player.getGameStats() != null) {
            TitleChangeListener.onTitleChange(player.getGameStats(), titleId, true);
		}
	}
}
