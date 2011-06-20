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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameserver.model.gameobjects.player;

/**
 * @author kosyachok, IlBuono
 */
public enum StorageType {
    CUBE(0, 109),
    REGULAR_WAREHOUSE(1, 96),
    ACCOUNT_WAREHOUSE(2, 16),
    LEGION_WAREHOUSE(3, 56),
    PET_BAG_6(32, 6),
    PET_BAG_12(33, 12),
    PET_BAG_18(34, 18),
    PET_BAG_24(35, 24),
    BROKER(126),
    MAILBOX(127);

    private int id;
    private int limit;

    private StorageType(int id) {
        this.id = id;
    }

    private StorageType(int id, int limit) {
        this.id = id;
        this.limit = limit;
    }

    public int getId() {
        return id;
    }

    public int getLimit() {
        return limit;
    }

    public static StorageType getStorageTypeById(int id) {
        for (StorageType st : values()) {
            if (st.id == id)
                return st;
        }
        return null;
    }
}
