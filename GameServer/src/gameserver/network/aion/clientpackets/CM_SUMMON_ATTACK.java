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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.world.World;
import org.apache.log4j.Logger;

/**
 * @author ATracer
 */
public class CM_SUMMON_ATTACK extends AionClientPacket {
    private static final Logger log = Logger.getLogger(CM_SUMMON_ATTACK.class);

    @SuppressWarnings("unused")
    private int summonObjId;
    private int targetObjId;
    @SuppressWarnings("unused")
    private int unk1;
    @SuppressWarnings("unused")
    private int unk2;
    @SuppressWarnings("unused")
    private int unk3;

    public CM_SUMMON_ATTACK(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        summonObjId = readD();
        targetObjId = readD();
        unk1 = readC();
        unk2 = readH();
        unk3 = readC();
    }

    @Override
    protected void runImpl() {
        // TODO: Use summonObjId to get summon, instead of activePlayer?
        Player activePlayer = getConnection().getActivePlayer();
        if (activePlayer == null) {
            log.error("CM_SUMMON_ATTACK packet received but cannot get master player.");
            return;
        }

        Summon summon = activePlayer.getSummon();

        if (summon == null) {
            log.error("CM_SUMMON_ATTACK packet received but cannot get summon.");
            return;
        }

        Creature creature = (Creature) World.getInstance().findAionObject(targetObjId);
        summon.getController().attackTarget(creature);
    }
}
