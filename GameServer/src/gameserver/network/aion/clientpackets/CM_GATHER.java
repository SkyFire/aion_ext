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

import gameserver.model.gameobjects.Gatherable;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;

/**
 * @author ATracer
 */
public class CM_GATHER extends AionClientPacket {
    boolean isStartGather = false;

    public CM_GATHER(int opcode) {
        super(opcode);
    }


    @Override
    protected void readImpl() {
        int action = readD();
        if (action == 0)
            isStartGather = true;

    }

    @Override
    protected void runImpl() {

        Player player = getConnection().getActivePlayer();
        if (player != null) {
            VisibleObject target = player.getTarget();
            if (target != null && target instanceof Gatherable) {
                if (isStartGather) {
                    ((Gatherable) target).getController().onStartUse(player);
                } else {
                    ((Gatherable) target).getController().finishGathering(player);
                }
            }
        }
    }

}
