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
import gameserver.services.QuestService;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author MrPoke,KaiPo
 */

public class SM_NEARBY_QUESTS extends AionServerPacket {
    private Integer[] questIds;
    private int size;

    public SM_NEARBY_QUESTS(List<Integer> questIds) {
        this.questIds = questIds.toArray(new Integer[questIds.size()]);
        this.size = questIds.size();
    }


    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        if (questIds == null || con.getActivePlayer() == null)
            return;
        int playerLevel = con.getActivePlayer().getLevel();
		writeC(buf, 0x00); // 2.1
		writeH(buf, (-1*size) & 0xFFFF); // 2.1
        for (int id : questIds) {
            writeH(buf, id);
            if (QuestService.checkLevelRequirement(id, playerLevel))
                writeH(buf, 0);
            else
                writeH(buf, 2);
        }
    }
}
