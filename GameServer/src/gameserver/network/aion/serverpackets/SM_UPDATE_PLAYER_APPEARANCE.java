/*
 * This file is part of aion-unique <www.aion-unique.com>.
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.Item;
import gameserver.model.items.GodStone;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author Avol
 *         modified by ATracer
 */
public class SM_UPDATE_PLAYER_APPEARANCE extends AionServerPacket {
    public int playerId;
    public int size;
    public List<Item> items;

    public SM_UPDATE_PLAYER_APPEARANCE(int playerId, List<Item> items) {
        this.playerId = playerId;
        this.items = items;
        this.size = items.size();
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, playerId);

        short mask = 0;
        for (Item item : items) {
            mask |= item.getEquipmentSlot();
        }

        writeH(buf, mask);

        for (Item item : items) {
            writeD(buf, item.getItemSkinTemplate().getTemplateId());
            GodStone godStone = item.getGodStone();
            writeD(buf, godStone != null ? godStone.getItemId() : 0);
            writeD(buf, item.getItemColor());
            writeH(buf, 0x00);// unk (0x00)
        }
    }
}