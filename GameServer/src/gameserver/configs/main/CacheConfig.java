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
package gameserver.configs.main;

import com.aionemu.commons.configuration.Property;
import gameserver.model.account.Account;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;

/**
 * This config contains settings about cache.
 *
 * @author Luno
 */
public class CacheConfig {
    /**
     * Says whether cache for such things like PlayerCommonData or Appereance etc is cached in {@link WeakCacheMap} or
     * in {@link SoftCacheMap}
     */
    @Property(key = "gameserver.cache.softcache", defaultValue = "false")
    public static boolean SOFT_CACHE_MAP = false;

    /**
     * If true then whole {@link Player} objects are cached as long as there is memory for them
     */
    @Property(key = "gameserver.cache.players", defaultValue = "false")
    public static boolean CACHE_PLAYERS = false;

    /**
     * If true then whole {@link PlayerCommonData} objects are cached as long as there is memory for them
     */
    @Property(key = "gameserver.cache.pcd", defaultValue = "false")
    public static boolean CACHE_COMMONDATA = false;

    /**
     * If true then whole {@link Account} objects are cached as long as there is memory for them
     */
    @Property(key = "gameserver.cache.accounts", defaultValue = "false")
    public static boolean CACHE_ACCOUNTS = false;
}
