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

import gameserver.model.gameobjects.Kisk;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author Sarynth
 *         0xB0 for 1.5.1.10 and 1.5.1.15
 */
public class SM_KISK_UPDATE extends AionServerPacket {
    // useMask values determine who can bind to the kisk.
    // 1 ~ race
    // 2 ~ legion
    // 3 ~ solo
    // 4 ~ group
    // 5 ~ alliance
    // of course, we must programmatically check as well.

    private int objId;
    private int useMask;
    private int currentMembers;
    private int maxMembers;
    private int remainingRessurects;
    private int maxRessurects;
    private int remainingLifetime;

    public SM_KISK_UPDATE(Kisk kisk) {
        this.objId = kisk.getObjectId();
        this.useMask = kisk.getUseMask();
        this.currentMembers = kisk.getCurrentMemberCount();
        this.maxMembers = kisk.getMaxMembers();
        this.remainingRessurects = kisk.getRemainingResurrects();
        this.maxRessurects = kisk.getMaxRessurects();
        this.remainingLifetime = kisk.getRemainingLifetime();
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, objId);
        writeD(buf, useMask);
        writeD(buf, currentMembers);
        writeD(buf, maxMembers);
        writeD(buf, remainingRessurects);
        writeD(buf, maxRessurects);
        writeD(buf, remainingLifetime);
    }

}
