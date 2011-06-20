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
package gameserver.utils.rates;

/**
 * @author ATracer
 */
public abstract class Rates {
    public abstract int getGroupXpRate();

    public abstract int getXpRate();

    public abstract float getApNpcRate();

    public abstract float getApPlayerRate();
    
    public abstract float getApLostPlayerRate();

    public abstract float getGatheringXPRate();

    public abstract float getCraftingXPRate();

    public abstract int getDropRate();

    public abstract int getQuestXpRate();

    public abstract int getQuestKinahRate();

    public abstract int getKinahRate();

    public abstract int getDpRate();

    public abstract int getPvpDpRate();
    
    public abstract int getGroupDpRate();

    /**
     * @param membership
     * @return Rates
     */
    public static Rates getRatesFor(byte membership) {
        switch (membership) {
            case 0:
                return new RegularRates();
            case 1:
                return new PremiumRates();
            case 2:
                return new VipRates();
            default:
                return new RegularRates();
        }
    }
}
