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
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author KaiPo
 */
public class SM_STARTED_QUEST_LIST extends AionServerPacket {

    private SortedMap<Integer, QuestState> completeQuestList = new TreeMap<Integer, QuestState>();
    private List<QuestState> startedQuestList = new ArrayList<QuestState>();

    public SM_STARTED_QUEST_LIST(Player player) {
        for (QuestState qs : player.getQuestStateList().getAllQuestState()) {
            if (qs.getStatus() == QuestStatus.COMPLETE)
                completeQuestList.put(qs.getQuestId(), qs);
            else if (qs.getStatus() != QuestStatus.NONE)
                startedQuestList.add(qs);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
			
		writeH(buf, 0x01); // 2.1 
		writeH(buf, (-1*startedQuestList.size()) & 0xFFFF);
        // writeC(buf, startedQuestList.size());
        for (QuestState qs : startedQuestList) // quest list size ( max is 25 )
        {
            writeH(buf, qs.getQuestId());
            writeH(buf, 0);
			writeC(buf, qs.getStatus().value());
            writeD(buf, qs.getQuestVars().getQuestVars());
            writeC(buf, 0);

        }
        		
    }

}