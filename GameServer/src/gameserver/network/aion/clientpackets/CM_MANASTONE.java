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

package gameserver.network.aion.clientpackets;

import gameserver.itemengine.actions.EnchantItemAction;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.templates.item.ItemCategory;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

/**
 * @author ATracer
 */
public class CM_MANASTONE extends AionClientPacket {

    private int npcObjId;
    private int slotNum;

    private int actionType;
    private int targetFusedSlot;
    private int stoneUniqueId;
    private int targetItemUniqueId;
    private int supplementUniqueId;
    private ItemCategory actionCategory;

    /**
     * @param opcode
     */
    public CM_MANASTONE(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        actionType = readC();
        targetFusedSlot = readC();
        //This is specifing which part of the weapon to socket with fused weapons,
        //1 is primary weapon 2 is fused weapon. System needs to be rewritten accordingly.
        targetItemUniqueId = readD();
        switch (actionType) {
            case 1:
            case 2:
                stoneUniqueId = readD();
                supplementUniqueId = readD();
                break;
            case 3:
                slotNum = readC();
                readC();
                readH();
                npcObjId = readD();
                break;
        }
    }

    @Override
    protected void runImpl() {
        AionObject npc = World.getInstance().findAionObject(npcObjId);
        Player player = getConnection().getActivePlayer();
        Storage inventory = player.getInventory();

        switch (actionType) {
            case 1: //enchant stone
            case 2: //add manastone
                EnchantItemAction action = new EnchantItemAction();
                Item manastone = inventory.getItemByObjId(stoneUniqueId);
                if (manastone == null)
                    return;
                Item targetItem = (inventory.isItemByObjId(targetItemUniqueId)) ?
                    inventory.getItemByObjId(targetItemUniqueId) :
                    player.getEquipment().getEquippedItemByObjId(targetItemUniqueId);
                if (targetItem == null)
                    return;

                if (actionType == 1)
                    actionCategory = ItemCategory.ENCHANTSTONE;
                else
                    actionCategory = ItemCategory.MAGICSTONE;

                if (manastone.getItemTemplate().getItemCategory() != actionCategory)
                    return;
                if (manastone != null && targetItem != null && action.canAct(player, manastone, targetItem)) {
                    int msID = Math.round(manastone.getItemTemplate().getTemplateId()/1000000);
                    int tID = Math.round(targetItem.getItemTemplate().getTemplateId()/1000000);
                    if((msID != 167 && msID != 166) || tID >= 120)
                    return;

                    Item supplement = player.getInventory().getItemByObjId(supplementUniqueId);
                    action.act(player, manastone, targetItem, supplement, targetFusedSlot);
                }
                break;
            case 3: // remove manastone
                long price = player.getPrices().getPriceForService(500, player.getCommonData().getRace());
                if (player.getInventory().getKinahItem().getItemCount() < price) {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.NOT_ENOUGH_KINAH(price));
                    return;
                }
                if (npc != null) {
                    player.getInventory().decreaseKinah(price);
                    if (targetFusedSlot == 1)
                        ItemService.removeManastone(player, targetItemUniqueId, slotNum);
                    else
                        ItemService.removeFusionstone(player, targetItemUniqueId, slotNum);
                }
        }
    }
}
