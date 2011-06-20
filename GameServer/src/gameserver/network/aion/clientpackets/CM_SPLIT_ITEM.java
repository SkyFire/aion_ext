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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameserver.network.aion.clientpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.ItemService;

/**
 * @author kosyak
 */
public class CM_SPLIT_ITEM extends AionClientPacket {

    int sourceItemObjId;
    int sourceStorageType;
    long itemAmount;
    int destinationItemObjId;
    int destinationStorageType;
    int slotNum; // destination slot.

    public CM_SPLIT_ITEM(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        sourceItemObjId = readD();       // drag item unique ID. If merging and itemCount becoming null, this item must be deleted.
        itemAmount = readD();            // Items count to be moved.
        @SuppressWarnings("unused")
        byte[] zeros = readB(4);         // Nothing
        sourceStorageType = readC();     // Source storage
        destinationItemObjId = readD();  // Destination item unique ID if merging. Null if spliting.
        destinationStorageType = readC();// Destination storage
        slotNum = readH();               // Destination slot. Not needed right now, Items adding only to next available slot. Not needed at all when merge.
    }


    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();

        if (destinationItemObjId == 0)
            ItemService.splitItem(player, sourceItemObjId, itemAmount, slotNum, sourceStorageType, destinationStorageType);
        else
            ItemService.mergeItems(player, sourceItemObjId, itemAmount, destinationItemObjId, sourceStorageType, destinationStorageType);
    }
}
