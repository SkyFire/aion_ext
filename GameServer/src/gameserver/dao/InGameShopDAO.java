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
package gameserver.dao;

import gameserver.model.inGameShop.InGameShop;
import gameserver.model.inGameShop.InGameShopCategory;

import java.util.List;

/**
 * @author PZIKO333
 */

public abstract class InGameShopDAO implements IDFactoryAwareDAO {
    public abstract boolean deleteIngameShopItem(int paramInt1, int paramInt2, int paramInt3);

    public abstract List<InGameShop> loadInGameShopItems();

    public abstract List<InGameShop> loadInGameShopItemsCat(int paramInt1);

    public abstract List<InGameShop> loadInGameShopSalesRanking(int paramInt1, int paramInt2, int paramInt3);

    public abstract InGameShop loadInGameShopItem(int paramInt);

    public abstract void saveIngameShopItem(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, String paramString);

    public abstract void saveIngameShopLog(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, long paramLong);

    public abstract int getMaxList(int paramInt);

    public abstract List<InGameShopCategory> loadInGameShopCategory();

    @Override
    public String getClassName() {
        return InGameShopDAO.class.getName();
    }
}