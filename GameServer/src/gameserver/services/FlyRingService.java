/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.services;

import gameserver.dataholders.DataManager;
import gameserver.model.flyring.FlyRing;
import gameserver.model.templates.flyring.FlyRingTemplate;
import org.apache.log4j.Logger;

/**
 * @author xavier
 */
public class FlyRingService {
    Logger log = Logger.getLogger(FlyRingService.class);

    private static class SingletonHolder {
        protected static final FlyRingService instance = new FlyRingService();
    }

    public static final FlyRingService getInstance() {
        return SingletonHolder.instance;
    }

    private FlyRingService() {
        for (FlyRingTemplate t : DataManager.FLY_RING_DATA.getFlyRingTemplates()) {
            FlyRing f = new FlyRing(t);
            f.spawn();
            log.debug("Added " + f.getName() + " at m=" + f.getWorldId() + ",x=" + f.getX() + ",y=" + f.getY() + ",z=" + f.getZ());
        }
    }
}
