/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author xavier
 */
public class SM_MACRO_RESULT extends AionServerPacket {
    public static SM_MACRO_RESULT SM_MACRO_CREATED = new SM_MACRO_RESULT(0x00);
    public static SM_MACRO_RESULT SM_MACRO_DELETED = new SM_MACRO_RESULT(0x01);

    private int code;

    private SM_MACRO_RESULT(int code) {
        this.code = code;
    }

    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, code);
    }
}
