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

package com.aionengine.commons.network;

/**
 * @author xavier
 */
public class ServerConfig {
    public String name;

    public String hostname;

    public int port;

    public ConnectionFactory factory;

    public int readThreads;

    public int writeThreads;

    public boolean enableWorkers;

    public int workerThreads;

    public int bufferCount;

    public int readTries;

    public int writeTries;

    public boolean debugEnabled;

    public ServerConfig(String name, String hostname, int port, ConnectionFactory factory, int readThreads, int writeThreads, boolean enableWorkers, int workerThreads, int bufferCount, int readTries, int writeTries, boolean debugEnabled) {
        this.name = name;
        this.hostname = hostname;
        this.port = port;
        this.factory = factory;
        this.readThreads = readThreads;
        this.writeThreads = writeThreads;
        this.enableWorkers = enableWorkers;
        this.workerThreads = workerThreads;
        this.bufferCount = bufferCount;
        this.readTries = readTries;
        this.writeTries = writeTries;
        this.debugEnabled = debugEnabled;
    }
}
