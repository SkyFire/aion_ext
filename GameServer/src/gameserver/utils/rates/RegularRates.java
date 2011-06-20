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

import gameserver.configs.main.RateConfig;

/**
 * @author ATracer
 */
public class RegularRates extends Rates {
    @Override
    public int getGroupXpRate() {
        return RateConfig.GROUPXP_RATE;
    }

    @Override
    public int getDropRate() {
        return RateConfig.DROP_RATE;
    }

    @Override
    public float getApNpcRate() {
        return RateConfig.AP_NPC_RATE;
    }

    @Override
    public float getApPlayerRate() {
        return RateConfig.AP_PLAYER_RATE;
    }
    
    @Override
    public float getApLostPlayerRate() {
        return RateConfig.AP_LOST_PLAYER_RATE;
    }

    @Override
    public int getQuestKinahRate() {
        return RateConfig.QUEST_KINAH_RATE;
    }

    @Override
    public int getQuestXpRate() {
        return RateConfig.QUEST_XP_RATE;
    }

    @Override
    public int getXpRate() {
        return RateConfig.XP_RATE;
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.utils.rates.Rates#getCraftingXPRate()
      */

    @Override
    public float getCraftingXPRate() {
        return RateConfig.CRAFTING_XP_RATE;
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.utils.rates.Rates#getGatheringXPRate()
      */

    @Override
    public float getGatheringXPRate() {
        return RateConfig.GATHERING_XP_RATE;
    }

    @Override
    public int getKinahRate() {
        return RateConfig.KINAH_RATE;
    }

    @Override
    public int getDpRate() {
        return RateConfig.DP_RATE;
    };

    @Override
    public int getPvpDpRate() {
        return RateConfig.PVP_DP_RATE;
    };

    @Override
    public int getGroupDpRate() {
        return RateConfig.GROUPDP_RATE;
    };

}
