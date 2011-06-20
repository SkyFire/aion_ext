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

import org.apache.log4j.Logger;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.BrokerService;

/**
 * @author kosyak, Lyahim
 */
public class CM_REGISTER_BROKER_ITEM extends AionClientPacket {
    private static final Logger	log	= Logger.getLogger(CM_REGISTER_BROKER_ITEM.class);

    private int brokerId;
    private int itemUniqueId;
    private long price;
    private int itemCount;

    public CM_REGISTER_BROKER_ITEM(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        this.brokerId = readD();
        this.itemUniqueId = readD();
        this.price = readQ();
        this.itemCount = readH();
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (price < 0 || itemCount < 1)
        {
                log.warn("[AUDIT] Possible client hack Player: "+player.getName()+" Account name: "+player.getAcountName()+toString());
                player.getClientConnection().close(true);
                return;
        }

        BrokerService.getInstance().registerItem(player, itemUniqueId, price, itemCount);
    }

    @Override
    public String toString() {
        return "CM_REGISTER_BROKER_ITEM [brokerId=" + brokerId
                + ", itemUniqueId=" + itemUniqueId + ", price=" + price
                + ", itemCount=" + itemCount + "]";
    }
}
