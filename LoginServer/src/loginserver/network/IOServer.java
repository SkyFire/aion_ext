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

package loginserver.network;

import com.aionemu.commons.network.NioServer;
import com.aionemu.commons.network.ServerCfg;
import loginserver.configs.Config;
import loginserver.network.aion.AionConnectionFactoryImpl;
import loginserver.network.gameserver.GsConnectionFactoryImpl;
import loginserver.utils.ThreadPoolManager;

/**
 * This class create NioServer and store its instance.
 *
 * @author -Nemesiss-
 */
public class IOServer {
    /**
     * NioServer instance that will handle io.
     */
    private final static NioServer instance;

    static {
        ServerCfg aion = new ServerCfg(Config.LOGIN_BIND_ADDRESS, Config.LOGIN_PORT, "Aion Connections",
                new AionConnectionFactoryImpl());

        ServerCfg gs = new ServerCfg(Config.GAME_BIND_ADDRESS, Config.GAME_PORT, "Gs Connections",
                new GsConnectionFactoryImpl());

        instance = new NioServer(Config.NIO_READ_THREADS, ThreadPoolManager.getInstance(), gs, aion);
    }

    /**
     * @return NioServer instance.
     */
    public static NioServer getInstance() {
        return instance;
    }
}
