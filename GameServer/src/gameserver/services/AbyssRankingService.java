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
package gameserver.services;

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.configs.main.CustomConfig;
import gameserver.dao.AbyssRankDAO;
import gameserver.model.AbyssRankingResult;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ABYSS_RANK;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.Executor;
import gameserver.world.World;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * Abyss Ranking Service
 *
 * @author kecimis
 */
public class AbyssRankingService {
    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(AbyssRankingService.class);

    private final HashMap<Race, ArrayList<AbyssRankingResult>> inviduals;
    private final HashMap<Race, ArrayList<AbyssRankingResult>> legions;

    private long timeofUpdate = 0;

    /**
     * TODO SYNCHRONIZE WITH LEGION SERVICE!
     */

    private AbyssRankingService() {
        //initilaze cache
        inviduals = new HashMap<Race, ArrayList<AbyssRankingResult>>();
        legions = new HashMap<Race, ArrayList<AbyssRankingResult>>();
        this.load();
    }

    public static AbyssRankingService getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Reload the Abyss Ranking system
     */
    public void reload() {

    }

    /**
     * Load the Abyss Ranking system
     * Called only on start of server
     */
    private void load() {
        log.info("AbyssRankService: Loaded!");

        scheduleUpdate();
    }

    /**
     * Should be called only from load()
     */
    private void scheduleUpdate() {
        String[] time = CustomConfig.TOP_RANKING_TIME.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        calendar.set(Calendar.SECOND, Integer.parseInt(time[2]));

        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();


        final Executor<Player> playerUpdateRanking = new Executor<Player>() {
            @Override
            public boolean run(Player p) {
                //load from db
                getDAO().loadAbyssRank(p);
                PacketSendUtility.sendPacket(p, new SM_ABYSS_RANK(p.getAbyssRank()));
                return true;
            }
        };

        final Executor<Player> playerSaveToDB = new Executor<Player>() {
            @Override
            public boolean run(Player p) {
                //reset daily/weekly kills/ap
                p.getAbyssRank().doUpdate();
                //saves to db
                getDAO().storeAbyssRank(p);
                return true;
            }
        };

        ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //save to db
                World.getInstance().doOnAllPlayers(playerSaveToDB);

                //update rankings
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        getDAO().updatePlayerRanking();
                        getDAO().updateLegionRanking();
                    }
                }, 15000);

                //update players in-game
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        World.getInstance().doOnAllPlayers(playerUpdateRanking);
                        reloadRankings();
                    }
                }, 30000);
            }
        }, delay, CustomConfig.TOP_RANKING_DELAY * 60 * 60 * 1000);
    }

    private void reloadRankings() {
        inviduals.clear();
        legions.clear();
        inviduals.put(Race.ELYOS, getDAO().getAbyssRankingPlayers(Race.ELYOS));
        inviduals.put(Race.ASMODIANS, getDAO().getAbyssRankingPlayers(Race.ASMODIANS));
        legions.put(Race.ASMODIANS, getDAO().getAbyssRankingLegions(Race.ASMODIANS));
        legions.put(Race.ELYOS, getDAO().getAbyssRankingLegions(Race.ELYOS));

        timeofUpdate = System.currentTimeMillis();

        log.info("AbyssRankService: Rankings were reloaded!");
    }

    /**
     * Returns Inviduals and Legions Rankings
     *
     * @param race
     * @return
     */
    public ArrayList<AbyssRankingResult> getInviduals(Race race) {
        return inviduals.get(race);
    }

    public ArrayList<AbyssRankingResult> getLegions(Race race) {
        return legions.get(race);
    }

    public long getTimeOfUpdate() {
        return timeofUpdate;
    }


    /**
     * Retuns {@link gameserver.dao.AbyssRankDAO} , just a shortcut
     *
     * @return {@link gameserver.dao.AbyssRankDAO}
     */
    private AbyssRankDAO getDAO() {
        return DAOManager.getDAO(AbyssRankDAO.class);
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final AbyssRankingService instance = new AbyssRankingService();
    }
}
