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

import gameserver.model.gameobjects.player.Prices;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author xavier, Sarynth
 *         Price/tax in Influence ration dialog
 */
public class SM_PRICES extends AionServerPacket {
    private Prices prices;

    /**
     * @param prices
     */
    public SM_PRICES(Prices prices) {
        this.prices = prices;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, prices.getGlobalPrices(con.getActivePlayer().getCommonData().getRace()));        // Display Buying Price %
        writeC(buf, prices.getGlobalPricesModifier());    // Buying Modified Price %
        writeC(buf, prices.getTaxes(con.getActivePlayer().getCommonData().getRace()));                // Tax = -100 + C %
    }
}
