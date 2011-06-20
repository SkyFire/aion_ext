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

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.Item;
import gameserver.model.items.ItemId;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;

import java.nio.ByteBuffer;

/**
 * @author kosyachok
 */
public class SM_UPDATE_WAREHOUSE_ITEM extends InventoryPacket {
    Item item;
    int warehouseType;

    public SM_UPDATE_WAREHOUSE_ITEM(Item item, int warehouseType) {
        this.item = item;
        this.warehouseType = warehouseType;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeGeneralInfo(buf, item);

        ItemTemplate itemTemplate = item.getItemTemplate();

        if (itemTemplate.getTemplateId() == ItemId.KINAH.value()) {
            writeKinah(buf, item, false);
        } else if (itemTemplate.isWeapon()) {
            writeWeaponInfo(buf, item, false);
        } else if (itemTemplate.isArmor()) {
            writeArmorInfo(buf, item, false, false, false);
        } else {
            writeGeneralItemInfo(buf, item, false, false);
        }
    }

    @Override
    protected void writeGeneralInfo(ByteBuffer buf, Item item) {
        writeD(buf, item.getObjectId());
        writeC(buf, warehouseType);
        ItemTemplate itemTemplate = item.getItemTemplate();
        writeH(buf, 0x24);
        writeD(buf, itemTemplate.getNameId());
        writeH(buf, 0);
    }

    @Override
    protected void writeKinah(ByteBuffer buf, Item item, boolean isInventory) {
        writeH(buf, 0x16); //length of details
        writeC(buf, 0);
        writeH(buf, item.getItemMask());
        writeQ(buf, item.getItemCount());
        writeD(buf, 0);
        writeD(buf, 0);
        writeH(buf, 0);
        writeC(buf, 0);
        writeC(buf, 0xFF); // FF FF equipment
        writeC(buf, 0xFF);
    }
}
