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

import gameserver.model.Petition;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.PetitionService;

import java.nio.ByteBuffer;

/**
 * @author zdead
 */
public class SM_PETITION extends AionServerPacket {
    private Petition petition;

    public SM_PETITION() {
        this.petition = null;
    }

    public SM_PETITION(Petition petition) {
        this.petition = petition;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        if (petition == null) {
            writeD(buf, 0x00);
            writeD(buf, 0x00);
            writeD(buf, 0x00);
            writeD(buf, 0x00);
            writeH(buf, 0x00);
            writeC(buf, 0x00);
        } else {
            writeC(buf, 0x01); // Action ID ?
            writeD(buf, 100); // unk (total online players ?)
            writeH(buf, PetitionService.getInstance().getWaitingPlayers(con.getActivePlayer().getObjectId())); // Users waiting for Support
            writeS(buf, Integer.toString(petition.getPetitionId())); // Ticket ID
            writeH(buf, 0x00);
            writeC(buf, 50); // Total Petitions
            writeC(buf, 49); // Remaining Petitions
            writeH(buf, PetitionService.getInstance().calculateWaitTime(petition.getPlayerObjId())); // Estimated minutes before GM reply
            writeD(buf, 0x00);
        }
    }
}
