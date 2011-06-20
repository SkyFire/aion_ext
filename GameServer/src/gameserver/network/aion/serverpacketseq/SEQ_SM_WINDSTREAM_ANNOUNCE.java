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
package gameserver.network.aion.serverpacketseq;

import gameserver.network.aion.AionServerPacketSeq;
import gameserver.network.aion.serverpackets.SM_WINDSTREAM_ANNOUNCE;

/**
 * This class is holding sequance of Info packets for Windstream
 * 
 * @author Vyaslav, Ares/Kaipo
 *
 */
public class SEQ_SM_WINDSTREAM_ANNOUNCE extends AionServerPacketSeq
{
	private boolean ok; 
	public SEQ_SM_WINDSTREAM_ANNOUNCE(int worldId)
	{
		super();
		ok=true;
		switch (worldId){
			case 210050000:
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 100));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 101));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 102));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 110));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 120));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 130));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 150));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 200));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 201));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 77));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 78));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 80));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 81));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 89));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 90));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 92));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 93));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 94));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 95));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 146));
			break;
			case 220070000:
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 1));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 31));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 2));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 3));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 4));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 79));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 82));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 83));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 84));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 91));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 147));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 148));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 149));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 150));
				addPacket(new SM_WINDSTREAM_ANNOUNCE(worldId, 151));
			break;
			default: ok=false; break;
		}
	}
	
	public boolean getStatus(){
		return ok;
	}

}
