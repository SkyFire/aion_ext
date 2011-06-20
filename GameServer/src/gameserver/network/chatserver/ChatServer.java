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
package gameserver.network.chatserver;

import com.aionemu.commons.network.Dispatcher;
import com.aionemu.commons.network.NioServer;
import gameserver.configs.network.NetworkConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.chatserver.serverpackets.SM_CS_PLAYER_AUTH;
import gameserver.network.chatserver.serverpackets.SM_CS_PLAYER_LOGOUT;
import gameserver.network.factories.CsPacketHandlerFactory;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

import java.nio.channels.SocketChannel;

/**
 * @author ATracer
 */
public class ChatServer {
    private static final Logger log = Logger.getLogger(ChatServer.class);

    private ChatServerConnection chatServer;
    private NioServer nioServer;

    private boolean serverShutdown = false;

    public static final ChatServer getInstance() {
        return SingletonHolder.instance;
    }

    private ChatServer() {
    }

    public void setNioServer(NioServer nioServer) {
        this.nioServer = nioServer;
    }

    /**
     * @return
     */
    public ChatServerConnection connect() {
        SocketChannel sc;
        for (; ;) {
            chatServer = null;
            log.info("Connecting to ChatServer: " + NetworkConfig.CHAT_ADDRESS);
            try {
                sc = SocketChannel.open(NetworkConfig.CHAT_ADDRESS);
                sc.configureBlocking(false);
                Dispatcher d = nioServer.getReadWriteDispatcher();
                CsPacketHandlerFactory csPacketHandlerFactory = new CsPacketHandlerFactory();
                chatServer = new ChatServerConnection(sc, d, csPacketHandlerFactory.getPacketHandler());
                return chatServer;
            }
            catch (Exception e) {
                log.info("Cant connect to ChatServer: " + e.getMessage());
            }
        }
    }

    /**
     * This method is called when we lost connection to ChatServer.
     */
    public void chatServerDown() {
        log.warn("Connection with ChatServer lost...");

        chatServer = null;

        if (!serverShutdown) {
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    connect();
                }
            }, 5000);
        }
    }

    /**
     * @param player
     * @param token
     */
    public void sendPlayerLoginRequst(Player player) {
        if (chatServer != null)
            chatServer.sendPacket(new SM_CS_PLAYER_AUTH(player.getObjectId(), player.getAcountName()));
    }

    /**
     * @param player
     */
    public void sendPlayerLogout(Player player) {
        if (chatServer != null)
            chatServer.sendPacket(new SM_CS_PLAYER_LOGOUT(player.getObjectId()));
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final ChatServer instance = new ChatServer();
	}
}
