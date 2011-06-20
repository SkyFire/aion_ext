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
public class PremiumRates extends Rates {
    @Override
    public int getGroupXpRate() {
        return RateConfig.PREMIUM_GROUPXP_RATE;
    }

    @Override
    public float getApNpcRate() {
        return RateConfig.PREMIUM_AP_NPC_RATE;
    }

    @Override
    public float getApPlayerRate() {
        return RateConfig.PREMIUM_AP_PLAYER_RATE;
    }
    
    @Override
    public float getApLostPlayerRate() {
        return RateConfig.PREMIUM_AP_LOST_PLAYER_RATE;
    }

    @Override
    public int getDropRate() {
        return RateConfig.PREMIUM_DROP_RATE;
    }

    @Override
    public int getQuestKinahRate() {
        return RateConfig.PREMIUM_QUEST_KINAH_RATE;
    }

    @Override
    public int getQuestXpRate() {
        return RateConfig.PREMIUM_QUEST_XP_RATE;
    }

    @Override
    public int getXpRate() {
        return RateConfig.PREMIUM_XP_RATE;
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.utils.rates.Rates#getCraftingXPRate()
      */

    @Override
    public float getCraftingXPRate() {
        return RateConfig.PREMIUM_CRAFTING_XP_RATE;
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.utils.rates.Rates#getGatheringXPRate()
      */

    @Override
    public float getGatheringXPRate() {
        return RateConfig.PREMIUM_GATHERING_XP_RATE;
    }

    @Override
    public int getKinahRate() {
        return RateConfig.PREMIUM_KINAH_RATE;
    }

    @Override
    public int getDpRate() {
        return RateConfig.PREMIUM_DP_RATE;
    };

    @Override
    public int getPvpDpRate() {
        return RateConfig.PREMIUM_PVP_DP_RATE;
    };

    @Override
    public int getGroupDpRate() {
        return RateConfig.PREMIUM_GROUPDP_RATE;
    };

}
