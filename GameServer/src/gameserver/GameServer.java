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
package gameserver;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.log4j.exceptions.Log4jInitializationError;
import com.aionemu.commons.network.NioServer;
import com.aionemu.commons.network.ServerCfg;
import com.aionemu.commons.services.LoggingService;
import com.aionemu.commons.utils.AEInfos;
import com.aionengine.commons.network.Server;
import com.aionengine.commons.network.ServerConfig;
import gameserver.cache.HTMLCache;
import gameserver.configs.Config;
import gameserver.configs.main.GSConfig;
import gameserver.configs.main.TaskManagerConfig;
import gameserver.configs.main.ThreadConfig;
import gameserver.configs.network.NetworkConfig;
import gameserver.dao.PlayerDAO;
import gameserver.dataholders.DataManager;
import gameserver.model.Race;
import gameserver.model.siege.Influence;
import gameserver.network.aion.GameConnectionFactoryImpl;
import gameserver.network.chatserver.ChatServer;
import gameserver.network.loginserver.LoginServer;
import gameserver.network.rdc.RDCConnectionFactory;
import gameserver.network.rdc.commands.RDCACommandTable;
import gameserver.questEngine.QuestEngine;
import gameserver.services.*;
import gameserver.spawnengine.SpawnEngine;
import gameserver.taskmanager.tasks.PacketBroadcaster;
import gameserver.utils.*;
import gameserver.utils.chathandlers.ChatHandlers;
import gameserver.utils.gametime.GameTimeManager;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.utils.idfactory.IDFactory;
import gameserver.utils.scheduler.Scheduler;
import gameserver.world.World;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <tt>GameServer</tt> is the main class of the application and represents the whole game server.<br>
 * This class is also an entry point with main() method.
 *
 * @author -Nemesiss-
 * @author SoulKeeper
 */
public class GameServer {
    /**
     * Logger for gameserver
     */
    private static final Logger log = Logger.getLogger(GameServer.class);

    private static int ELYOS_COUNT = 0;
    private static int ASMOS_COUNT = 0;
    private static double ELYOS_RATIO = 0.0;
    private static double ASMOS_RATIO = 0.0;
    private static final ReentrantLock lock = new ReentrantLock();
    public static Server nioServer;
    public static Server rdcServer;

    public static String CONFIGURATION_FILE;

    /**
     * Launching method for GameServer
     *
     * @param args arguments, not used
     */
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        if (args.length == 0)
            CONFIGURATION_FILE = "./config/default.config";
        else
            CONFIGURATION_FILE = args[0];

        initUtilityServicesAndConfig();

        Util.printSection("World");
        DataManager.getInstance();
        IDFactory.getInstance();
        World.getInstance();

        GameServer gs = new GameServer();
        // Set all players is offline
        DAOManager.getDAO(PlayerDAO.class).setPlayersOffline(false);

        Util.printSection("Spawns");
        SpawnEngine.getInstance();

        Util.printSection("Quests");
        QuestEngine.getInstance();
        QuestEngine.getInstance().load();

        Util.printSection("TaskManagers");
        PacketBroadcaster.getInstance();

        GameTimeService.getInstance();

        AnnouncementService.getInstance();

        DebugService.getInstance();

        ZoneService.getInstance();

        WeatherService.getInstance();

        DuelService.getInstance();

        MailService.getInstance();

        GroupService.getInstance();

        AllianceService.getInstance();

        BrokerService.getInstance();

        SiegeService.getInstance();

        Influence.getInstance();

        DropService.getInstance();

        ExchangeService.getInstance();

        PeriodicSaveService.getInstance();

        PetitionService.getInstance();

        NpcShoutsService.getInstance();

        ShieldService.getInstance();

        FlyRingService.getInstance();

        LanguageHandler.getInstance();

        ChatHandlers.getInstance();

        HTMLCache.getInstance();

        AbyssRankingService.getInstance();

		RentalService.getInstance();

        Util.printSection("System");
        AEVersions.printFullVersionInfo();
        System.gc();
        AEInfos.printAllInfos();

        log.info("GameServer started at Server Version: " + GSConfig.SERVER_VERSION);

        Util.printSection("GameServerLog");
        log.info("Game Server started in " + (System.currentTimeMillis() - start) / 1000 + " seconds.");

        gs.startServers();
        GameTimeManager.startClock();
        
        Scheduler scheduler = Scheduler.getInstance();

        if (TaskManagerConfig.DEADLOCK_DETECTOR_ENABLED) {
            log.info("Starting deadlock detector");
            new Thread(new DeadlockDetector(TaskManagerConfig.DEADLOCK_DETECTOR_INTERVAL)).start();
        }

        Runtime.getRuntime().addShutdownHook(ShutdownHook.getInstance());

        if (GSConfig.FACTIONS_RATIO_LIMITED) {
            addStartupHook(new StartupHook() {
                @Override
                public void onStartup() {
                    lock.lock();
                    try {
                        GameServer.ASMOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ASMODIANS);
                        GameServer.ELYOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ELYOS);
                        computeRatios();
                    }
                    catch (Exception e) {
                    }
                    finally {
                        lock.unlock();
                    }
                    displayRatios(false);
                }
            });
        }

        // gs.injector.getInstance(com.aionemu.gameserver.utils.chathandlers.ChatHandlers.class);
        onStartup();

        // RDC !!
        Util.printSection("RDCServer");
        RDCACommandTable.initialize();
    }

    /**
     * Starts servers for connection with aion client and login server.
     */
    private void startServers() {
        ServerCfg aion = new ServerCfg(NetworkConfig.GAME_BIND_ADDRESS, NetworkConfig.GAME_PORT, "Game Connections", new GameConnectionFactoryImpl());

        //if (NetworkConfig.NIO_READ_THREADS <= 0 || NetworkConfig.NIO_WRITE_THREADS <=0)
        //{
        //	throw new GameServerError("You should set gameserver.network.nio.threads.read and gameserver.network.nio.threads.write to a positive value greater than 0");
        //}

        //ServerConfig newAion = new ServerConfig(GSConfig.SERVER_NAME, NetworkConfig.GAME_BIND_ADDRESS, NetworkConfig.GAME_PORT, new GameConnectionFactoryImpl(), NetworkConfig.NIO_READ_THREADS, NetworkConfig.NIO_WRITE_THREADS, NetworkConfig.NIO_ENABLE_WORKERS, NetworkConfig.NIO_WORKER_THREADS, NetworkConfig.NIO_WORKER_THREAD_BUFFERS);
        //ServerCfg login = new ServerCfg(NetworkConfig.LOGIN_ADDRESS.getHostName(), NetworkConfig.LOGIN_ADDRESS.getPort(), "Login Connections", new LoginConnectionFactoryImpl());
        NioServer nioServer = new NioServer(NetworkConfig.NIO_READ_THREADS + NetworkConfig.NIO_WRITE_THREADS, ThreadPoolManager.getInstance(), aion);
        //nioServer = new Server(newAion);

        LoginServer loginServer = LoginServer.getInstance();
        ChatServer chatServer = ChatServer.getInstance();
        ServerConfig rdcConfig = new ServerConfig("RDC Server", NetworkConfig.RDC_BIND_ADDRESS, NetworkConfig.RDC_BIND_PORT, new RDCConnectionFactory(), 1, 1, false, 0, 0, 3, 3, NetworkConfig.NIO_DEBUG);
        rdcServer = new Server(rdcConfig);

        loginServer.setNioServer(nioServer);
        chatServer.setNioServer(nioServer);

        // Nio must go first
        nioServer.connect();
        loginServer.connect();

        if (!GSConfig.DISABLE_CHAT_SERVER)
            chatServer.connect();

        if (!GSConfig.DISABLE_RDC_SERVER)
            rdcServer.start();
    }

    /**
     * Initialize all helper services, that are not directly related to aion gs, which includes:
     * <ul>
     * <li>Logging</li>
     * <li>Database factory</li>
     * <li>Thread pool</li>
     * </ul>
     * <p/>
     * This method also initializes {@link Config}
     *
     * @throws Log4jInitializationError
     */
    private static void initUtilityServicesAndConfig() throws Log4jInitializationError {
        // Set default uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
        // First of all we must initialize logging
        LoggingService.init();
        // init config
        Config.load();
        // Second should be database factory
        Util.printSection("DataBase");
        DatabaseFactory.init();
        // Initialize DAOs
        DAOManager.init();
        // Initialize thread pools
        Util.printSection("Threads");
        ThreadConfig.load();
        ThreadPoolManager.getInstance();
    }

    private static Set<StartupHook> startUpHooks = new HashSet<StartupHook>();

    public synchronized static void addStartupHook(StartupHook hook) {
        if (startUpHooks != null)
            startUpHooks.add(hook);
        else
            hook.onStartup();
    }

    private synchronized static void onStartup() {
        final Set<StartupHook> startupHooks = startUpHooks;

        startUpHooks = null;

        for (StartupHook hook : startupHooks)
            hook.onStartup();
    }

    public interface StartupHook {
        public void onStartup();
    }

    /**
     * @param race
     * @param i
     */
    public static void updateRatio(Race race, int i) {
        lock.lock();
        try {
            switch (race) {
                case ASMODIANS:
                    GameServer.ASMOS_COUNT += i;
                    break;
                case ELYOS:
                    GameServer.ELYOS_COUNT += i;
                    break;
                default:
                    break;
            }

            computeRatios();
        }
        catch (Exception e) {
        }
        finally {
            lock.unlock();
        }

        displayRatios(true);
    }

    private static void computeRatios() {
        if ((GameServer.ASMOS_COUNT <= GSConfig.FACTIONS_RATIO_MINIMUM) && (GameServer.ELYOS_COUNT <= GSConfig.FACTIONS_RATIO_MINIMUM)) {
            GameServer.ASMOS_RATIO = GameServer.ELYOS_RATIO = 50.0;
        } else {
            GameServer.ASMOS_RATIO = GameServer.ASMOS_COUNT * 100.0 / (GameServer.ASMOS_COUNT + GameServer.ELYOS_COUNT);
            GameServer.ELYOS_RATIO = GameServer.ELYOS_COUNT * 100.0 / (GameServer.ASMOS_COUNT + GameServer.ELYOS_COUNT);
        }
    }

    private static void displayRatios(boolean updated) {
        log.info("==== RATIOS " + (updated ? "UPDATED " : "") + ": E " + String.format("%.1f", GameServer.ELYOS_RATIO) + " % / A " + String.format("%.1f", GameServer.ASMOS_RATIO) + " %");
    }

    public static double getRatiosFor(Race race) {
        switch (race) {
            case ASMODIANS:
                return GameServer.ASMOS_RATIO;
            case ELYOS:
                return GameServer.ELYOS_RATIO;
			default:
				return 0.0;
		}
	}
}
