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

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemCategory;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.ItemService;
import gameserver.utils.MathUtil;
import gameserver.world.World;

/**
 * @author ATracer
 */
public class CM_GODSTONE_SOCKET extends AionClientPacket {

    private int npcId;
    private int weaponId;
    private int stoneId;

    public CM_GODSTONE_SOCKET(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        this.npcId = readD();
        this.weaponId = readD();
        this.stoneId = readD();
    }

    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();

        Npc npc = (Npc) World.getInstance().findAionObject(npcId);
        if (npc == null)
            return;

        if (!MathUtil.isInRange(activePlayer, npc, 15))
            return;

        Item itemStone = activePlayer.getInventory().getItemByObjId(stoneId);
        if (itemStone == null)
            return;

        ItemTemplate temp = itemStone.getItemTemplate();
        if (temp.getItemCategory() != ItemCategory.HOLYSTONE)
            return;

        ItemService.socketGodstone(activePlayer, weaponId, stoneId);
    }
}
