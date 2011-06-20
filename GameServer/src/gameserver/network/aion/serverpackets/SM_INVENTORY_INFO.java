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
import java.util.Collections;
import java.util.List;

/**
 * In this packet Server is sending Inventory Info
 *
 * @author -Nemesiss-
 * @updater alexa026
 * @finisher Avol ;d
 * <p/>
 * modified by ATracer
 */
public class SM_INVENTORY_INFO extends InventoryPacket {
    public static final int EMPTY = 0;
    public static final int FULL = 1;
    public int CUBE = 0;

    private List<Item> items;
    private int size;

    public int packetType = FULL;

    /**
     * @param items
     */
    public SM_INVENTORY_INFO(List<Item> items, int cubesize) {
        //this should prevent client crashes but need to discover when item is null
        items.removeAll(Collections.singletonList(null));
        this.items = items;
        this.size = items.size();
        this.CUBE = cubesize;
    }

    /**
     * @param isEmpty
     */
    public SM_INVENTORY_INFO() {
        this.packetType = EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        if (this.packetType == EMPTY) {
            writeD(buf, 0);
            writeH(buf, 0);
            return;
        }

        // something wrong with cube part.
        writeC(buf, 1); // TRUE/FALSE (1/0) update cube size
        writeC(buf, CUBE); // cube size from npc (so max 5 for now)
        writeC(buf, 0); // cube size from quest (so max 2 for now)
        writeC(buf, 0); // unk?
        writeH(buf, size); // number of entries

        for (Item item : items) {
            writeGeneralInfo(buf, item);

            ItemTemplate itemTemplate = item.getItemTemplate();

            if (itemTemplate.getTemplateId() == ItemId.KINAH.value()) {
                writeKinah(buf, item, true);
            } else if (itemTemplate.isWeapon()) {
                writeWeaponInfo(buf, item, true);
            } else if (itemTemplate.isArmor()) {
                writeArmorInfo(buf, item, true, false, false);
            } else if (itemTemplate.isStigma()) {
                writeStigmaInfo(buf, item);
            } else {
                writeGeneralItemInfo(buf, item, false, false);
                writeC(buf, 0);
            }
        }
    }
}
