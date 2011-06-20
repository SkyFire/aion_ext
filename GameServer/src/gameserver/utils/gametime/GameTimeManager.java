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
package gameserver.utils.gametime;

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.dao.GameTimeDAO;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

/**
 * Manages ingame time
 *
 * @author Ben
 */
public class GameTimeManager {
    private static final Logger log = Logger.getLogger(GameTimeManager.class);
    private static GameTime instance;
    private static GameTimeUpdater updater;
    private static boolean clockStarted = false;

    static {
        GameTimeDAO dao = DAOManager.getDAO(GameTimeDAO.class);
        instance = new GameTime(dao.load());
    }

    /**
     * Gets the current GameTime
     *
     * @return GameTime
     */
    public static GameTime getGameTime() {
        return instance;
    }

    /**
     * Starts the counter that increases the clock every tick
     *
     * @throws IllegalStateException If called twice
     */
    public static void startClock() {
        if (clockStarted) {
            throw new IllegalStateException("Clock is already started");
        }

        updater = new GameTimeUpdater(getGameTime());
        ThreadPoolManager.getInstance().scheduleAtFixedRate(updater, 0, 5000);

        clockStarted = true;
    }

    /**
     * Saves the current time to the database
     *
     * @return Success
     */
    public static boolean saveTime() {
        log.info("Game time saved...");
        return DAOManager.getDAO(GameTimeDAO.class).store(getGameTime().getTime());
    }
}
