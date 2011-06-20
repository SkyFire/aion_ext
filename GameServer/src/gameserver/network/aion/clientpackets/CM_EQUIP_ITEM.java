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

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Equipment;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import gameserver.restrictions.RestrictionsManager;
import gameserver.skillengine.model.Effect;
import gameserver.utils.PacketSendUtility;

/**
 * @author Avol modified by ATracer
 */
public class CM_EQUIP_ITEM extends AionClientPacket {
    public int slotRead;
    public int itemUniqueId;
    public int action;

    public CM_EQUIP_ITEM(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        action = readC(); // 0/1 = equip/unequip
        slotRead = readD();
        itemUniqueId = readD();
    }

    @Override
    protected void runImpl() {
        final Player activePlayer = getConnection().getActivePlayer();
        Equipment equipment = activePlayer.getEquipment();
        Item resultItem = null;

        if (!RestrictionsManager.canChangeEquip(activePlayer))
            return;

        switch (action) {
            case 0:
                resultItem = equipment.equipItem(itemUniqueId, slotRead);
                break;
            case 1:
                resultItem = equipment.unEquipItem(itemUniqueId, slotRead);
                break;
            case 2:
                equipment.switchHands();
                break;
        }

        if (resultItem != null || action == 2) {
            PacketSendUtility.broadcastPacket(activePlayer, new SM_UPDATE_PLAYER_APPEARANCE(activePlayer.getObjectId(),
                    equipment.getEquippedItemsWithoutStigma()), true);
        }

        if (!equipment.isShieldEquipped()) {
            for (Effect effect : activePlayer.getEffectController().getNoShowEffects()) {
                if (effect.isStance())
                    activePlayer.getEffectController().removeNoshowEffect(effect.getSkillId());
            }
        }

    }
}
