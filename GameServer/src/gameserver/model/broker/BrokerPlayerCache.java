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
package gameserver.model.broker;

import gameserver.model.gameobjects.BrokerItem;

/**
 * @author ATracer
 */
public class BrokerPlayerCache {
    private BrokerItem[] brokerListCache = new BrokerItem[0];
    private int brokerMaskCache;
    private int brokerSoftTypeCache;
    private int brokerStartPageCache;

    /**
     * @return the brokerListCache
     */
    public BrokerItem[] getBrokerListCache() {
        return brokerListCache;
    }

    /**
     * @param brokerListCache the brokerListCache to set
     */
    public void setBrokerListCache(BrokerItem[] brokerListCache) {
        this.brokerListCache = brokerListCache;
    }

    /**
     * @return the brokerMaskCache
     */
    public int getBrokerMaskCache() {
        return brokerMaskCache;
    }

    /**
     * @param brokerMaskCache the brokerMaskCache to set
     */
    public void setBrokerMaskCache(int brokerMaskCache) {
        this.brokerMaskCache = brokerMaskCache;
    }

    /**
     * @return the brokerSoftTypeCache
     */
    public int getBrokerSortTypeCache() {
        return brokerSoftTypeCache;
    }

    /**
     * @param brokerSoftTypeCache the brokerSoftTypeCache to set
     */
    public void setBrokerSortTypeCache(int brokerSoftTypeCache) {
        this.brokerSoftTypeCache = brokerSoftTypeCache;
    }

    /**
     * @return the brokerStartPageCache
     */
    public int getBrokerStartPageCache() {
        return brokerStartPageCache;
    }

    /**
     * @param brokerStartPageCache the brokerStartPageCache to set
     */
    public void setBrokerStartPageCache(int brokerStartPageCache) {
        this.brokerStartPageCache = brokerStartPageCache;
	}
}
