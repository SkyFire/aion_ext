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
package gameserver.model.gameobjects.stats.id;

import gameserver.model.gameobjects.stats.StatEffectType;

/**
 * @author Antivirus
 */
public class ItemSetStatEffectId extends StatEffectId {
    // Effect for the number of item set parts equipped
    private int setpart;

    private ItemSetStatEffectId(int id, int setpart) {
        super(id, StatEffectType.ITEM_SET_EFFECT);
        this.setpart = setpart;
    }

    public static ItemSetStatEffectId getInstance(int id, int setpart) {
        return new ItemSetStatEffectId(id, setpart);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = super.equals(o);
        result = (result) && (o != null);
        result = (result) && (o instanceof ItemSetStatEffectId);
        result = (result) && (((ItemSetStatEffectId) o).setpart == setpart);
        return result;
    }

    @Override
    public int compareTo(StatEffectId o) {
        int result = super.compareTo(o);
        if (result == 0) {
            if (o instanceof ItemSetStatEffectId) {
                result = setpart - ((ItemSetStatEffectId) o).setpart;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        final String str = super.toString() + ",parts:" + setpart;
        return str;
    }
}
