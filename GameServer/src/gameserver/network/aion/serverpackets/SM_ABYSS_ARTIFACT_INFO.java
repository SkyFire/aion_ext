/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.network.aion.serverpackets;

import gameserver.model.siege.SiegeLocation;
import gameserver.model.siege.SiegeType;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import javolution.util.FastList;

import java.nio.ByteBuffer;
import java.util.Collection;

/**
 * @author xitanium
 */
public class SM_ABYSS_ARTIFACT_INFO extends AionServerPacket {

    private Collection<SiegeLocation> locations;

    public SM_ABYSS_ARTIFACT_INFO(Collection<SiegeLocation> locations) {
        this.locations = locations;
    }

    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        FastList<SiegeLocation> validLocations = new FastList<SiegeLocation>();

        for (SiegeLocation loc : locations) {
            if (loc.getSiegeType() == SiegeType.ARTIFACT || loc.getSiegeType() == SiegeType.FORTRESS) {
                if (loc.getLocationId() >= 1011 && loc.getLocationId() < 2000) {
                    validLocations.add(loc);
                }
            }
        }

        writeH(buf, validLocations.size()); // Artifact Count

        for (SiegeLocation loc : validLocations) {
            writeD(buf, loc.getLocationId());
            writeD(buf, 0); //unk
            writeD(buf, 0); //unk
        }
    }
}
