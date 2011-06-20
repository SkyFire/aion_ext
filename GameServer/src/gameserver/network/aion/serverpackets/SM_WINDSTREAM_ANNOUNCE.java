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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is holding Info packets for Windstream
 *
 * @author Vyaslav, Ares/Kaipo
 */
public class SM_WINDSTREAM_ANNOUNCE extends AionServerPacket {
    private int worldId;
    private int wsSeqId;

    //static map for announce sequance per world

    @SuppressWarnings("serial")
    private static class SeqMap extends HashMap<Integer, HashMap<SeqMap.PacketPartType, HashMap<Integer, Set<Integer>>>> {
        public enum PacketPartType {
            PACKET_SEQ_PREFIX,
            PACKET_SEQ_POSTFIX
        }

        public SeqMap() {
            super();
            //worldid
            put(210050000, new HashMap<PacketPartType, HashMap<Integer, Set<Integer>>>() {{
                //packetPartType
                put(PacketPartType.PACKET_SEQ_PREFIX, new HashMap<Integer, Set<Integer>>() {{
                    //value
                    put(0x01, new HashSet<Integer>(Arrays.asList(
                            //seqs
                            77, 78, 80, 81, 89, 90, 92, 93, 94, 95, 146
                    )));
                }});

                put(PacketPartType.PACKET_SEQ_POSTFIX, new HashMap<Integer, Set<Integer>>() {{
                    put(0x01, new HashSet<Integer>(Arrays.asList(

                            130, 200, 201, 77, 78, 80, 81, 89, 90, 92, 93, 94, 95, 146
                    )));
                }});
            }});
            //worldid
            put(220070000, new HashMap<PacketPartType, HashMap<Integer, Set<Integer>>>() {{
                //packetPartType
                put(PacketPartType.PACKET_SEQ_PREFIX, new HashMap<Integer, Set<Integer>>() {{
                    //value
                    put(0x01, new HashSet<Integer>(Arrays.asList(
                            //seqs
                            79, 82, 83, 84, 91, 147, 148, 149, 150, 151
                    )));
                    //value
                    put(0x02, new HashSet<Integer>(Arrays.asList(
                            //seqs
                            1, 31
                    )));
                }});

                put(PacketPartType.PACKET_SEQ_POSTFIX, new HashMap<Integer, Set<Integer>>() {{
                    put(0x01, new HashSet<Integer>(Arrays.asList(
                            //seqs
                            3, 4, 79, 82, 83, 84, 91, 147, 148, 149, 150, 151
                    )));

                }});
            }});
        }

        private static final SeqMap seqMap = new SeqMap();

        public static int getPacketPart(int worldId, SeqMap.PacketPartType packetPartType, int seqId) {
            int ret = 0;
            HashMap<Integer, Set<Integer>> valMap = seqMap.get(worldId).get(packetPartType);
            for (int val : valMap.keySet())
                if (valMap.get(val).contains(seqId)) ret = val;
            return ret;
        }
    }

    ;

    public SM_WINDSTREAM_ANNOUNCE(int worldId, int wsSeqId) {
        this.worldId = worldId;
        this.wsSeqId = wsSeqId;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, SeqMap.getPacketPart(worldId, SeqMap.PacketPartType.PACKET_SEQ_PREFIX, wsSeqId));
        writeD(buf, worldId);
        writeD(buf, wsSeqId);
        writeC(buf, SeqMap.getPacketPart(worldId, SeqMap.PacketPartType.PACKET_SEQ_POSTFIX, wsSeqId));
    }
}
