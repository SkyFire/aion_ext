/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author ginho1
 */
public class SM_REPURCHASE extends AionServerPacket {
    private int targetObjectId;

    public SM_REPURCHASE(Npc npc, Player player) {
        targetObjectId = npc.getObjectId();
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, targetObjectId);
        writeD(buf, 0);
        //writeH(buf, items.size());
        writeH(buf, 0);

        /*for(Item item : items)
          {
              writeD(buf, item.getObjectId());
              writeD(buf, item.getItemTemplate().getTemplateId());
          }*/
    }
}