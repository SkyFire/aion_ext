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
 * @author ATracer
 */
public class EnchantStatEffectId extends StatEffectId {

    private int slot;

    private EnchantStatEffectId(int id, int slot) {
        super(id, StatEffectType.ENCHANT_EFFECT);
        this.slot = slot;
    }

    public static EnchantStatEffectId getInstance(int id, int slot) {
        return new EnchantStatEffectId(id, slot);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = super.equals(o);
        result = (result) && (o != null);
        result = (result) && (o instanceof EnchantStatEffectId);
        result = (result) && (((EnchantStatEffectId) o).slot == slot);
        return result;
    }

    @Override
    public int compareTo(StatEffectId o) {
        int result = super.compareTo(o);
        if (result == 0) {
            if (o instanceof EnchantStatEffectId) {
                result = slot - ((EnchantStatEffectId) o).slot;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        final String str = super.toString() + ",slot:" + slot;
        return str;
    }

    public int getSlot() {
        return slot;
    }
}
