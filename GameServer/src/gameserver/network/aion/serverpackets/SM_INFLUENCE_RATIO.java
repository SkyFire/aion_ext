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

import gameserver.model.siege.Influence;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.SiegeService;

import java.nio.ByteBuffer;

/**
 * @author Nemiroff
 *         Total Influence Ratio
 */
public class SM_INFLUENCE_RATIO extends AionServerPacket {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        Influence inf = Influence.getInstance();

        writeD(buf, SiegeService.getInstance().getSiegeTime());
        writeF(buf, inf.getElyos());
        writeF(buf, inf.getAsmos());
        writeF(buf, inf.getBalaur());

        //TODO: 1.9 has writeH(buf, 3) with balauria values
        writeH(buf, 1);

        writeD(buf, 400010000);
        writeF(buf, inf.getElyos());
        writeF(buf, inf.getAsmos());
        writeF(buf, inf.getBalaur());

    }
}
