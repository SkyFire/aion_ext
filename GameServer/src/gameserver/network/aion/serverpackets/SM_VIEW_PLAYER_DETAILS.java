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

import gameserver.model.gameobjects.Item;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author Avol, AionChs
 */

public class SM_VIEW_PLAYER_DETAILS extends InventoryPacket {
    private List<Item> items;
    private int size;
    private int targetObjId;

    public SM_VIEW_PLAYER_DETAILS(int targetObjId, List<Item> items) {
        this.items = items;
        this.size = items.size();
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, targetObjId); // unk
        writeC(buf, 11); //unk
        writeC(buf, size); // itemCount
        writeC(buf, 0);
        writeD(buf, 0);
        for (Item item : items) {
            writeGeneralInfo(buf, item);
            if (item.getItemTemplate().isArmor())
                writeArmorInfo(buf, item, false, false, true);
            else if (item.getItemTemplate().isWeapon())
                writeWeaponInfo(buf, item, false, false, false, true);
            else
                writeGeneralItemInfo(buf, item, false, true);
        }
    }

   protected void writeGeneralInfo(ByteBuffer buf, Item item) {
        writeD(buf, 0);
        writeD(buf, item.getItemTemplate().getTemplateId()); // itemId
        writeH(buf, 36); //
        writeD(buf, item.getItemTemplate().getNameId()); // itemNameId
        writeH(buf, 0);
   }
}
