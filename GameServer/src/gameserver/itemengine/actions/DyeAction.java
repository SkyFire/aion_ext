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
package gameserver.itemengine.actions;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author IceReaper
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DyeAction")

public class DyeAction extends AbstractItemAction {

    @XmlAttribute(name = "color")
    protected String color;

    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        if (targetItem == null) { // no item selected.
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_ERROR);
            return false;
        }

        return true;
    }

    @Override
    public void act(Player player, Item parentItem, Item targetItem) {
        if (targetItem.getItemTemplate().isItemDyePermitted()) {
            if (color.equals("no")) {
                targetItem.setItemColor(0);
            } else {
                int rgb = Integer.parseInt(color, 16);
                int bgra = 0xFF | ((rgb & 0xFF) << 24) | ((rgb & 0xFF00) << 8) | ((rgb & 0xFF0000) >>> 8);
                targetItem.setItemColor(bgra);
            }

            // item is equipped, so need broadcast packet
            if (player.getEquipment().getEquippedItemByObjId(targetItem.getObjectId()) != null) {
                PacketSendUtility.broadcastPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(), player.getEquipment().getEquippedItemsWithoutStigma()), true);
                player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
            }

            // item is not equipped
            else
                player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);

            PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(targetItem));
            player.getInventory().removeFromBagByObjectId(parentItem.getObjectId(), 1);
        }
    }
}
