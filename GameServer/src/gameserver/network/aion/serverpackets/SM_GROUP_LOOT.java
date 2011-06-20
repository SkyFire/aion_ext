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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author Rhys2002
 */
public class SM_GROUP_LOOT extends AionServerPacket {
    private int groupId;
    private int unk1;
    private int unk2;
    private int itemId;
    private int itemIndex;
    private int lootCorpseId;
    private int distributionId;
    private int playerId;
    private int luck;

    /**
     * @param Player Id must be 0 to start the Roll Options
     */
    public SM_GROUP_LOOT(int groupId, int itemId, int itemIndex, int lootCorpseId, int distributionId) {
        this.groupId = groupId;
        this.unk1 = 1;
        this.unk2 = 1;
        this.itemId = itemId;
        this.itemIndex = itemIndex;
        this.lootCorpseId = lootCorpseId;
        this.distributionId = distributionId;
        this.playerId = 0;
        this.luck = 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, groupId);
        writeD(buf, unk1);
        writeD(buf, unk2);
        writeD(buf, itemId);
        writeC(buf, itemIndex);
        writeD(buf, lootCorpseId);
        writeC(buf, distributionId);
        writeD(buf, playerId);
        writeD(buf, luck);
    }
}
