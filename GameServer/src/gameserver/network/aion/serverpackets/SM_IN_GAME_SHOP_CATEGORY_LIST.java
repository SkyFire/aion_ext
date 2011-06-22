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

import gameserver.dao.InGameShopDAO;
import gameserver.model.inGameShop.InGameShopCategory;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.List;

import com.aionemu.commons.database.dao.DAOManager;

/**
 * @author jenose
 */

public class SM_IN_GAME_SHOP_CATEGORY_LIST extends AionServerPacket {
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        List<InGameShopCategory> category = (DAOManager.getDAO(InGameShopDAO.class)).loadInGameShopCategory();

        writeD(buf, 2);
        writeH(buf, category.size());
        for (InGameShopCategory ShopCategory : category) {
            writeD(buf, ShopCategory.getId());
            writeS(buf, ShopCategory.getName());
        }
    }
}