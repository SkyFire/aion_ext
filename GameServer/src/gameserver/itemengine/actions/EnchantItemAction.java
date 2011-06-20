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

import gameserver.model.TaskId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.EnchantService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Nemiroff Date: 16.12.2009
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnchantItemAction")
public class EnchantItemAction extends AbstractItemAction {
    @XmlAttribute(name = "count")
    protected int sub_enchant_material_many;

    public int getEnchantCount() {
        return sub_enchant_material_many;
    }

    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        if (targetItem == null) { // no item selected.
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_ERROR);
            return false;
        }

        return true;
    }

    @Override
    public void act(final Player player, final Item parentItem, final Item targetItem) {
        act(player, parentItem, targetItem, null, 1);
    }

    //necessary overloading to not change AbstractItemAction

    public void act(final Player player, final Item parentItem, final Item targetItem, final Item supplementItem, final int targetWeapon) {
        PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
                parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 5000, 0, 0));
        player.getController().cancelTask(TaskId.ITEM_USE);
        player.getController().addNewTask(TaskId.ITEM_USE,
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {

                        int itemId = parentItem.getItemTemplate().getTemplateId();
                        if (itemId > 166000000 && itemId < 167000000) {
                            boolean result = EnchantService.enchantItem(player, parentItem, targetItem, supplementItem);
                            PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem
                                    .getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, result ? 1 : 2, 0));
                        } else {
                            boolean result = EnchantService.socketManastone(player, parentItem, targetItem, supplementItem, targetWeapon);
                            PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem
                                    .getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, result ? 1 : 2, 0));
                        }

                    }

                }, 5000));
    }
}
