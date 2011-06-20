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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.skillengine.model.Effect;

import java.nio.ByteBuffer;
import java.util.Collection;

/**
 * @author Avol, ATracer
 */
public class SM_ABNORMAL_STATE extends AionServerPacket {
    private Collection<Effect> effects;
    private int abnormals;

    public SM_ABNORMAL_STATE(Collection<Effect> effects, int abnormals) {
        this.effects = effects;
        this.abnormals = abnormals;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, abnormals);
        writeH(buf, effects.size());

        for (Effect effect : effects) {
            writeD(buf, effect.getEffectorId());
            writeH(buf, effect.getSkillId());
            writeC(buf, effect.getSkillLevel());
            writeC(buf, effect.getTargetSlot());
            writeD(buf, effect.getElapsedTime());
        }

    }
}