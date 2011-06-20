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

import java.nio.ByteBuffer;

/**
 * @author Sweetkr
 */
public class SM_SKILL_ACTIVATION extends AionServerPacket {

    private boolean isActive;
    private int unk;
    private int skillId;

    /**
     * For toggle skills
     *
     * @param skillId
     * @param isActive
     */
    public SM_SKILL_ACTIVATION(int skillId, boolean isActive) {
        this.skillId = skillId;
        this.isActive = isActive;
        this.unk = 0;
    }

    /**
     * For stigma remove should work in 1.5.1.15
     *
     * @param skillId
     */
    public SM_SKILL_ACTIVATION(int skillId) {
        this.skillId = skillId;
        this.isActive = true;
        this.unk = 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeH(buf, skillId);
        writeD(buf, unk);
        writeC(buf, isActive ? 1 : 0);
    }
}
