/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is pryvate software: you can redistribute it and/or modify
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
package gameserver.controllers.attack;

import gameserver.configs.main.CustomConfig;
import javolution.util.FastMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Sarynth
 */
public class KillList {
    private static final long DAY_IN_MILLISECONDS = CustomConfig.DAILY_PVP_PERIOD * 3600000;
    private FastMap<Integer, List<Long>> killList;

    public KillList() {
        killList = new FastMap<Integer, List<Long>>();
    }

    /**
     * @param winnerId
     * @param victimId
     * @return killsForVictimId
     */
    public int getKillsFor(int victimId) {
        List<Long> killTimes = killList.get(victimId);

        if (killTimes == null)
            return 0;

        long now = System.currentTimeMillis();
        int killCount = 0;

        for (Iterator<Long> i = killTimes.iterator(); i.hasNext();) {
            if (now - i.next().longValue() > DAY_IN_MILLISECONDS) {
                i.remove();
            } else {
                killCount++;
            }
        }

        return killCount;
    }

    /**
     * @param victimId
     */
    public void addKillFor(int victimId) {
        List<Long> killTimes = killList.get(victimId);
        if (killTimes == null) {
            killTimes = new ArrayList<Long>();
            killList.put(victimId, killTimes);
        }

        killTimes.add(System.currentTimeMillis());
    }

}
