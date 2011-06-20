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
package gameserver.model.items;

/**
 * @author ATracer
 */
public enum ItemId {
    KINAH(182400001),
    ANGELS_EYE(186000037),
    DEMONS_EYE(186000038),
    BROKEN_COIN(182005367),
    PLATINUM_ELYOS(186000005),
    PLATINUM_ASMODIANS(186000010);

    private int itemId;

    private ItemId(int itemId) {
        this.itemId = itemId;
    }

    public int value() {
        return itemId;
    }
}
