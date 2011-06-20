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
import gameserver.model.items.ItemId;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;

import java.nio.ByteBuffer;

/**
 * @author Avol
 * @author ATracer
 */
public class SM_EXCHANGE_ADD_ITEM extends InventoryPacket {
    private int action;
    private Item item;

    public SM_EXCHANGE_ADD_ITEM(int action, Item item) {
        this.action = action;
        this.item = item;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {

        writeC(buf, action); // 0 -self 1-other

        writeGeneralInfo(buf, item);

        ItemTemplate itemTemplate = item.getItemTemplate();

        if (itemTemplate.getTemplateId() == ItemId.KINAH.value()) {
            writeKinah(buf, item, true);
        } else if (itemTemplate.isWeapon()) {
            writeWeaponInfo(buf, item, true);
        } else if (itemTemplate.isArmor()) {
            writeArmorInfo(buf, item, true, false, false);
        } else {
            writeGeneralItemInfo(buf, item, false, false);
            writeC(buf, 0);
        }
    }

    @Override
    protected void writeGeneralInfo(ByteBuffer buf, Item item) {
        ItemTemplate itemTemplate = item.getItemTemplate();
        writeD(buf, itemTemplate.getTemplateId());
        writeD(buf, item.getObjectId());
        writeH(buf, 0x24);
        writeD(buf, itemTemplate.getNameId());
        writeH(buf, 0);
    }
}