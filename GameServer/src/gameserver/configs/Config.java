/*
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
package gameserver.configs;

import com.aionemu.commons.configuration.ConfigurableProcessor;
import com.aionemu.commons.database.DatabaseConfig;
import com.aionemu.commons.utils.PropertiesUtils;
import gameserver.GameServer;
import gameserver.configs.administration.AdminConfig;
import gameserver.configs.main.*;
import gameserver.configs.network.IPConfig;
import gameserver.configs.network.NetworkConfig;
import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * @author -Nemesiss-
 * @author SoulKeeper
 */
public class Config {
    /**
     * Logger for this class.
     */
    protected static final Logger log = Logger.getLogger(Config.class);

    /**
     * Initialize all configs in com.aionemu.gameserver.configs package
     */
    public static void load() {
        try {
            Properties props = PropertiesUtils.load(GameServer.CONFIGURATION_FILE);
            ConfigurableProcessor.process(Config.class, props);
            ConfigurableProcessor.process(AdminConfig.class, props);
            ConfigurableProcessor.process(LegionConfig.class, props);
            ConfigurableProcessor.process(RateConfig.class, props);
            ConfigurableProcessor.process(CacheConfig.class, props);
            ConfigurableProcessor.process(ShutdownConfig.class, props);
            ConfigurableProcessor.process(TaskManagerConfig.class, props);
            ConfigurableProcessor.process(GroupConfig.class, props);
            ConfigurableProcessor.process(CustomConfig.class, props);
            ConfigurableProcessor.process(EnchantsConfig.class, props);
            ConfigurableProcessor.process(FallDamageConfig.class, props);
            ConfigurableProcessor.process(GSConfig.class, props);
            ConfigurableProcessor.process(NpcMovementConfig.class, props);
            ConfigurableProcessor.process(PeriodicSaveConfig.class, props);
            ConfigurableProcessor.process(PricesConfig.class, props);
            ConfigurableProcessor.process(SiegeConfig.class, props);
            ConfigurableProcessor.process(ThreadConfig.class, props);
            ConfigurableProcessor.process(GeoDataConfig.class, props);
            ConfigurableProcessor.process(NetworkConfig.class, props);
            ConfigurableProcessor.process(DatabaseConfig.class, props);
            ConfigurableProcessor.process(HTMLConfig.class, props);
            ConfigurableProcessor.process(CraftConfig.class, props);
            ConfigurableProcessor.process(InGameShopConfig.class, props);
        } catch (Exception e) {
            log.fatal("Can't load gameserver configuration: ", e);
            throw new Error("Can't load gameserver configuration: ", e);
        }

        IPConfig.load();
    }
}