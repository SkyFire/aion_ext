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
public class SM_WAREHOUSE_UPDATE extends InventoryPacket {
    private int warehouseType;
    private Item item;


    public SM_WAREHOUSE_UPDATE(Item item, int warehouseType) {
        this.warehouseType = warehouseType;
        this.item = item;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, warehouseType);
        writeH(buf, 13);
        writeH(buf, 1);

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
        ItemTemplate itemTemplate = item.getItemTemplate();
        writeD(buf, itemTemplate.getTemplateId());
        writeC(buf, 0); //some item info (4 - weapon, 7 - armor, 8 - rings, 17 - bottles)
        writeH(buf, 0x24);
        writeD(buf, itemTemplate.getNameId());
        writeH(buf, 0);
    }
}
