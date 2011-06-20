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
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author alexa026
 * @author ATracer
 */
public class SM_ATTACK_STATUS extends AionServerPacket {
    private Creature creature;
    private TYPE type;
    private int skillId;
    private int value;
    private int unknown;


    public static enum TYPE {
        NATURAL_HP(3),
        REGULAR(5),
        DAMAGE(7),
        HP(7),
        HEAL_MP(19),
        MP(21),
        NATURAL_MP(22),
        FP_RINGS(23),
        FP(25),
        NATURAL_FP(26);

        private int value;

        private TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public SM_ATTACK_STATUS(Creature creature, TYPE type, int skillId, int value, int unknown) {
        this(creature, type, skillId, value);
        this.unknown = unknown;
    }

    public SM_ATTACK_STATUS(Creature creature, TYPE type, int skillId, int value) {
        this.creature = creature;
        this.type = type;
        this.skillId = skillId;
        this.value = value;
        this.unknown = 0xA6;
    }

    public SM_ATTACK_STATUS(Creature creature, int value) {
        this.creature = creature;
        this.type = TYPE.REGULAR;
        this.skillId = 0;
    }

    /**
     * {@inheritDoc} ddchcc
     */

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, creature.getObjectId());
        switch (type) {
            case DAMAGE:
                writeD(buf, -value);
                break;
            default:
                writeD(buf, value);
        }
        writeC(buf, type.getValue());
        writeC(buf, creature.getLifeStats().getHpPercentage());
        writeH(buf, skillId);
        writeH(buf, unknown);
    }
}
