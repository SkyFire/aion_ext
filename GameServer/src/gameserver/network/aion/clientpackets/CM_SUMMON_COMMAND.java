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

import gameserver.controllers.SummonController.UnsummonType;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.world.World;

/**
 * @author ATracer
 */
public class CM_SUMMON_COMMAND extends AionClientPacket {

    private int mode;
    private int targetObjId;

    public CM_SUMMON_COMMAND(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        mode = readC();
        readD();
        readD();
        targetObjId = readD();
    }

    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();
        Summon summon = activePlayer.getSummon();
        if (summon != null) {
            switch (mode) {
                case 0:
                    AionObject target = World.getInstance().findAionObject(targetObjId);
                    if (target != null && target instanceof Creature) {
                        summon.getController().attackMode();
                    }
                    break;
                case 1:
                    summon.getController().guardMode();
                    break;
                case 2:
                    summon.getController().restMode();
                    break;
                case 3:
                    summon.getController().release(UnsummonType.COMMAND);
                    break;

            }
        }
    }

}
