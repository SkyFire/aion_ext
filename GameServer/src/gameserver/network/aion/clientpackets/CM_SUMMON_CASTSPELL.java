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

import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.world.World;

/**
 * @author ATracer
 */
public class CM_SUMMON_CASTSPELL extends AionClientPacket {
    @SuppressWarnings("unused")
    private int summonObjId;
    private int targetObjId;
    private int skillId;
    @SuppressWarnings("unused")
    private int skillLvl;
    @SuppressWarnings("unused")
    private float unk;

    public CM_SUMMON_CASTSPELL(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        summonObjId = readD();
        skillId = readH();
        skillLvl = readC();
        targetObjId = readD();
        unk = readF();
    }

    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();
        Summon summon = activePlayer.getSummon();

        if (summon == null)//TODO log here?
            return;

        AionObject targetObject = World.getInstance().findAionObject(targetObjId);
        if (targetObject instanceof Creature) {
            summon.getController().useSkill(skillId, (Creature) targetObject);
        }
    }
}
