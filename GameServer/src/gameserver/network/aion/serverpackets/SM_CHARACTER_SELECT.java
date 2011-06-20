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

import gameserver.configs.main.CustomConfig;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author acu77
 */
public class SM_CHARACTER_SELECT extends AionServerPacket {
    private int type;       // 0: new passkey input window, 1: passkey input window, 2: message window
    private int messageType; // 0: newpasskey complete, 2: passkey edit complete, 3: passkey input
    private int wrongCount;

    public SM_CHARACTER_SELECT(int type) {
        this.type = type;
    }

    public SM_CHARACTER_SELECT(int type, int messageType, int wrongCount) {
        this.type = type;
        this.messageType = messageType;
        this.wrongCount = wrongCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, type);

        switch (type) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                writeH(buf, messageType); // 0: newpasskey complete, 2: passkey edit complete, 3: passkey input
                writeC(buf, wrongCount > 0 ? 1 : 0); // 0: right passkey, 1: wrong passkey
                writeD(buf, wrongCount); // wrong passkey input count
                writeD(buf, CustomConfig.PASSKEY_WRONG_MAXCOUNT); // Enter the number of possible wrong numbers (retail server default value: 5)
                break;
        }
    }
}
