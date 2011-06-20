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

import gameserver.model.gameobjects.player.ToyPet;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author xitanium
 */
public class SM_PET_MOVE extends AionServerPacket {
    private int actionId;
    private ToyPet pet;

    public SM_PET_MOVE(int actionId, ToyPet pet) {
        this.actionId = actionId;
        this.pet = pet;
    }


    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, pet.getDatabaseIndex());
        writeC(buf, actionId);
        switch (actionId) {
            case 12:
                // move
                writeF(buf, pet.getX1());
                writeF(buf, pet.getY1());
                writeF(buf, pet.getZ1());
                writeC(buf, pet.getH());
                writeF(buf, pet.getX2());
                writeF(buf, pet.getY2());
                writeF(buf, pet.getZ2());
                break;
            default:
                break;
        }
    }
}
