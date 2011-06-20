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

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Title;
import gameserver.model.gameobjects.player.TitleList;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author acu77
 */
public class SM_TITLE_INFO extends AionServerPacket {
    private TitleList titleList;
    private int type;       // 0: list, 1: self set, 3: broad set
    private int titleId;
    private int playerObjId;

    /**
     * title list
     *
     * @param player
     */
    public SM_TITLE_INFO(Player player) {
        this.type = 0;
        this.titleList = player.getTitleList();
    }

    /**
     * self title set
     *
     * @param titleId
     */
    public SM_TITLE_INFO(int titleId) {
        this.type = 1;
        this.titleId = titleId;
    }

    /**
     * broad title set
     *
     * @param player
     * @param titleId
     */
    public SM_TITLE_INFO(Player player, int titleId) {
        this.type = 3;
        this.playerObjId = player.getObjectId();
        this.titleId = titleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, type);

        switch (type) {
            case 0: // list
                writeC(buf, 0); // unk
                writeH(buf, titleList.size());
                for (Title title : titleList.getTitles()) {
                    writeD(buf, title.getTemplate().getTitleId());
                    writeD(buf, 0); // unk
                }
                break;
            case 1: // self set
                writeD(buf, titleId);
                break;
            case 3: // broad set
                writeD(buf, playerObjId);
                writeD(buf, titleId);
                break;
        }
    }
}
