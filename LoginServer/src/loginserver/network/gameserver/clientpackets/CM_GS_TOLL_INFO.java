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

package loginserver.network.gameserver.clientpackets;

import com.aionemu.commons.database.dao.DAOManager;
import loginserver.dao.AccountDAO;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;

import java.nio.ByteBuffer;

/**
 * @author PZIKO333
 */

public class CM_GS_TOLL_INFO extends GsClientPacket {

    private int toll_count;

    private String name;

    public CM_GS_TOLL_INFO(ByteBuffer buf, GsConnection client) {
        super(buf, client, 0x09);
    }

    private static AccountDAO getAccountDAO() {
        return DAOManager.getDAO(AccountDAO.class);
    }

    @Override
    protected void readImpl() {
        this.toll_count = readD();
        this.name = readS();
    }

    @Override
    protected void runImpl() {
        getAccountDAO().updateTollcount(toll_count, name);
    }
}