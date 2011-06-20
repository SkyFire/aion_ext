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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_CHAT_INIT;
import gameserver.network.chatserver.ChatServer;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ATracer
 */
public class ChatService {
    private static final Logger log = Logger.getLogger(ChatService.class);

    private static Map<Integer, Player> players = new HashMap<Integer, Player>();

    private static byte[] ip = {127, 0, 0, 1};
    private static int port = 10241;

    /**
     * Send token to chat server
     *
     * @param player
     */
    public static void onPlayerLogin(final Player player) {
    	if(player.isConnectedChat())
    		return;
    	
        ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                if (!isPlayerConnected(player)) {
                    ChatServer.getInstance().sendPlayerLoginRequst(player);
                } else {
                    log.warn("Player already registered with chat server " + player.getName());
                    // TODO do force relog in chat server?
                    onPlayerLogout(player);
                    ChatServer.getInstance().sendPlayerLoginRequst(player);
                }
            }
        }, 10000);

    }

    /**
     * Disonnect from chat server
     *
     * @param player
     */
    public static void onPlayerLogout(Player player) {
        players.remove(player.getObjectId());
        ChatServer.getInstance().sendPlayerLogout(player);
    }

    /**
     * @param player
     * @return
     */
    public static boolean isPlayerConnected(Player player) {
        return players.containsKey(player.getObjectId());
    }

    /**
     * @param playerId
     * @param token
     */
    public static void playerAuthed(int playerId, byte[] token) {
        Player player = World.getInstance().findPlayer(playerId);
        if (player != null) {
            players.put(playerId, player);
            PacketSendUtility.sendPacket(player, new SM_CHAT_INIT(token));
            player.setConnectedChat(true);
        }
    }

    /**
     * @return the ip
     */
    public static byte[] getIp() {
        return ip;
    }

    /**
     * @return the port
     */
    public static int getPort() {
        return port;
    }

    /**
     * @param ip the ip to set
     */
    public static void setIp(byte[] _ip) {
        ip = _ip;
    }

    /**
     * @param port the port to set
     */
    public static void setPort(int _port)
	{
		port = _port;
	}
}
