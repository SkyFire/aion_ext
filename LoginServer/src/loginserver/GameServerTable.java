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
package loginserver;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.network.IPRange;
import com.aionemu.commons.utils.NetworkUtils;
import loginserver.dao.GameServersDAO;
import loginserver.model.Account;
import loginserver.network.gameserver.GsAuthResponse;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.serverpackets.SM_REQUEST_KICK_ACCOUNT;
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * GameServerTable contains list of GameServers registered on this LoginServer. GameServer may by online or down.
 *
 * @author -Nemesiss-
 */
public class GameServerTable {
    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(GameServerTable.class);

    /**
     * Map<Id,GameServer>
     */
    private static Map<Byte, GameServerInfo> gameservers;

    /**
     * Return collection contains all registered [up/down] GameServers.
     *
     * @return collection of GameServers.
     */
    public static Collection<GameServerInfo> getGameServers() {
        return Collections.unmodifiableCollection(gameservers.values());
    }

    /**
     * Load GameServers from database.
     */
    public static void load() {
        gameservers = getDAO().getAllGameServers();
        log.info("GameServerTable loaded " + gameservers.size() + " registered GameServers.");
    }

    /**
     * Register GameServer if its possible.
     *
     * @param gsConnection   Connection object
     * @param requestedId    id of server that was requested
     * @param defaultAddress default network address from server, usually internet address
     * @param ipRanges       mapping of various ip ranges, usually used for local area networks
     * @param port           port that is used by server
     * @param maxPlayers     maximum amount of players
     * @param password       server password that is specified configs, used to check if gs can auth on ls
     * @return GsAuthResponse
     */
    public static GsAuthResponse registerGameServer(GsConnection gsConnection, byte requestedId, byte[] defaultAddress,
                                                    List<IPRange> ipRanges, int port, int maxPlayers, int requiredAccess, String password) {
        GameServerInfo gsi = gameservers.get(requestedId);

        /**
         * This id is not Registered at LoginServer.
         */
        if (gsi == null) {
            log.info(gsConnection + " requestedID=" + requestedId + " not aviable!");
            return GsAuthResponse.NOT_AUTHED;
        }

        /**
         * Check if this GameServer is not already registered.
         */
        if (gsi.getGsConnection() != null)
            return GsAuthResponse.ALREADY_REGISTERED;

        /**
         * Check if password and ip are ok.
         */
        if (!gsi.getPassword().equals(password) || (!NetworkUtils.checkIPMatching(gsi.getIp(), gsConnection.getIP()) && !checkIPMatching(gsi.getIp(), gsConnection.getIP()))) {
            log.info(gsConnection + " wrong ip or password!");
            return GsAuthResponse.NOT_AUTHED;
        }

        gsi.setDefaultAddress(defaultAddress);
        gsi.setIpRanges(ipRanges);
        gsi.setPort(port);
        gsi.setMaxPlayers(maxPlayers);
        gsi.setRequiredAccess(requiredAccess);
        gsi.setGsConnection(gsConnection);

        gsConnection.setGameServerInfo(gsi);
        return GsAuthResponse.AUTHED;
    }

    /**
     * Check whether or not the given ips are matched
     */
    public static boolean checkIPMatching(String pattern, String address) {
        if (pattern.equals("*.*.*.*") || pattern.equals("*"))
            return true;

        InetAddress ia = null;
        String addressToCheck = null;
        try {
            ia = InetAddress.getByName(pattern);
            addressToCheck = ia.getHostAddress();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (addressToCheck == null)
            return false;

        String[] mask = addressToCheck.split("\\.");
        String[] ip_address = address.split("\\.");
        for (int i = 0; i < mask.length; i++) {
            if (mask[i].equals("*") || mask[i].equals(ip_address[i]))
                continue;
            else if (mask[i].contains("-")) {
                byte min = Byte.parseByte(mask[i].split("-")[0]);
                byte max = Byte.parseByte(mask[i].split("-")[1]);
                byte ip = Byte.parseByte(ip_address[i]);
                if (ip < min || ip > max)
                    return false;
            } else
                return false;
        }
        return true;
    }

    /**
     * Returns GameSererInfo object for given gameserverId.
     *
     * @param gameServerId
     * @return GameSererInfo object for given gameserverId.
     */
    public static GameServerInfo getGameServerInfo(byte gameServerId) {
        return gameservers.get(gameServerId);
    }

    /**
     * Check if account is already in use on any GameServer. If so - kick account from GameServer.
     *
     * @param acc account to check
     * @return true is account is logged in on one of GameServers
     */
    public static boolean isAccountOnAnyGameServer(Account acc) {
        for (GameServerInfo gsi : getGameServers()) {
            if (gsi.isAccountOnGameServer(acc.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method, used to kick account from any gameServer if it's logged in
     *
     * @param account account to kick
     */
    public static void kickAccountFromGameServer(Account account) {
        for (GameServerInfo gsi : getGameServers()) {
            if (gsi.isAccountOnGameServer(account.getId())) {
                gsi.getGsConnection().sendPacket(new SM_REQUEST_KICK_ACCOUNT(account.getId()));
                break;
            }
        }
    }

    /**
     * Retuns {@link loginserver.dao.GameServersDAO} , just a shortcut
     *
     * @return {@link loginserver.dao.GameServersDAO}
     */
    private static GameServersDAO getDAO() {
        return DAOManager.getDAO(GameServersDAO.class);
    }
}
