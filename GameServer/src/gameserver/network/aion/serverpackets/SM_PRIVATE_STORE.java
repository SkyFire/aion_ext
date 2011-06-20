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
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PrivateStore;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.trade.TradePSItem;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;

import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author Simple, ZeroSignal
 */
public class SM_PRIVATE_STORE extends InventoryPacket {
    private static final Logger log = Logger.getLogger(SM_PRIVATE_STORE.class);
    /**
     * Private store Information *
     */
    private PrivateStore store;

    public SM_PRIVATE_STORE(PrivateStore store) {
        this.store = store;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        if (store == null)
            return;
        List<TradePSItem> storeItems = store.getSoldItems();
        if (storeItems.isEmpty())
            return;
        Player storePlayer = store.getOwner();
        writeD(buf, storePlayer.getObjectId());
        writeH(buf, storeItems.size());
        for (TradePSItem storeItem : storeItems) {
            Item item = storePlayer.getInventory().getItemByObjId(storeItem.getItemObjId());
            writeD(buf, storeItem.getItemObjId());
            writeD(buf, storeItem.getItemTemplate().getTemplateId());
            writeH(buf, (int) storeItem.getCount());
            writeD(buf, (int) storeItem.getPrice());

            ItemTemplate itemTemplate = storeItem.getItemTemplate();
            if (item == null || itemTemplate == null) {
                log.warn("SM_PRIVATE_STORE.writeImpl - Item is null or ItemTemplate is null. " + storeItem.toString());
            }
            if (itemTemplate.isWeapon()) {
                writeWeaponInfo(buf, item, false, false, true, false);
            } else if (itemTemplate.isArmor()) {
                writeArmorInfo(buf, item, false, true, false);
            } else {
                writeGeneralItemInfo(buf, item, true, false);
            }
        }
    }
}