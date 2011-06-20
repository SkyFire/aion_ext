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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author Sweetkr
 */
public class SM_TARGET_SELECTED extends AionServerPacket {
    @SuppressWarnings("unused")
    private Player player;
    private int level;
    private int maxHp;
    private int currentHp;
    private int targetObjId;

    public SM_TARGET_SELECTED(Player player) {
        this.player = player;
        if (player.getTarget() instanceof Creature) {
            this.level = ((Creature) player.getTarget()).getLevel();
            this.maxHp = ((Creature) player.getTarget()).getLifeStats().getMaxHp();
            this.currentHp = ((Creature) player.getTarget()).getLifeStats().getCurrentHp();
        } else {
            //TODO: check various gather on retail
            this.level = 1;
            this.maxHp = 1;
            this.currentHp = 1;
        }

        if (player.getTarget() != null)
            targetObjId = player.getTarget().getObjectId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, targetObjId);
        writeH(buf, level);
        writeD(buf, maxHp);
        writeD(buf, currentHp);
    }
}
