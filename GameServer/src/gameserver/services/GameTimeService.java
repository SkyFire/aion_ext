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

import gameserver.ai.events.Event;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_GAME_TIME;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.gametime.DayTime;
import gameserver.world.Executor;
import gameserver.world.World;
import org.apache.log4j.Logger;

/**
 * @author ATracer
 */
public class GameTimeService {
    private static Logger log = Logger.getLogger(GameTimeService.class);

    public static final GameTimeService getInstance() {
        return SingletonHolder.instance;
    }

    private final static int GAMETIME_UPDATE = 3 * 60000;

    private GameTimeService() {
        /**
         * Update players with current game time
         */
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                log.debug("Sending current game time to all players");
                World.getInstance().doOnAllPlayers(new Executor<Player>() {
                    @Override
                    public boolean run(Player object) {
                        PacketSendUtility.sendPacket(object, new SM_GAME_TIME());
                        return true;
                    }
                });
            }
        }, GAMETIME_UPDATE, GAMETIME_UPDATE);

        log.info("GameTimeService started. Update interval:" + GAMETIME_UPDATE);
    }

    /**
     * @param dayTime
     */
    public void sendDayTimeChangeEvents(DayTime dayTime) {
        World.getInstance().doOnAllNpcs(new Executor<Npc>() {
            @Override
            public boolean run(Npc object) {
                object.getAi().handleEvent(Event.DAYTIME_CHANGE);
                return true;
            }
        });
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final GameTimeService instance = new GameTimeService();
	}
}
