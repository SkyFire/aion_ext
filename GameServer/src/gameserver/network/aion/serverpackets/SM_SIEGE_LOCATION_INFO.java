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

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.configs.main.SiegeConfig;
import gameserver.dao.LegionDAO;
import gameserver.model.legion.LegionEmblem;
import gameserver.model.siege.SiegeLocation;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.SiegeService;
import javolution.util.FastMap;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @author Sarynth - packets from rhys2002.
 */

// SM_SIEGE_LOCATION_INFO
public class SM_SIEGE_LOCATION_INFO extends AionServerPacket
{
	/**
	 * infoType 0 - reset 1 - change
	 */
	private int							infoType;

	private Map<Integer, SiegeLocation>	locations;

	public SM_SIEGE_LOCATION_INFO()
	{
		this.infoType = 0; // Reset
		locations = SiegeService.getInstance().getSiegeLocations();
	}

	/**
	 * @param loc
	 */
	public SM_SIEGE_LOCATION_INFO(SiegeLocation loc)
	{
		this.infoType = 1; // Update
		locations = new FastMap<Integer, SiegeLocation>();
		locations.put(loc.getLocationId(), loc);
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(SiegeConfig.SIEGE_ENABLED == false)
		{
			// Siege is Disabled
			writeC(buf, 0);
			writeH(buf, 0);
			return;
		}

		writeC(buf, infoType);
		writeH(buf, locations.size());
		for(SiegeLocation sLoc : locations.values())
		{
			writeD(buf, sLoc.getLocationId()); // Artifact ID

			int legionId = sLoc.getLegionId();
			writeD(buf, legionId); // Legion ID

			LegionEmblem emblem = DAOManager.getDAO(LegionDAO.class).loadLegionEmblem(legionId);
			int emblemId = emblem.getEmblemVer();
			writeD(buf, emblemId);

			writeC(buf, 0xFF); //sets transparency 0xFF is default; but 0x00 is much better looking ;o)
	        writeC(buf, emblem.getColor_r());
	        writeC(buf, emblem.getColor_g());
	        writeC(buf, emblem.getColor_b());

			writeC(buf, sLoc.getRace().getRaceId());

			// is vulnerable (0 - no, 2 - yes)
			writeC(buf, sLoc.isVulnerable() ? 2 : 0);

			// faction can teleport (0 - no, 1 - yes)
			writeC(buf, sLoc.isCanTeleport() ? 1 : 0);

			// Next State (0 - invulnerable, 1 - vulnerable)
			writeC(buf, sLoc.getNextState());

			writeD(buf, 0); // unk
			writeD(buf, 0); // unk 1.9 only
		}
	}
}
