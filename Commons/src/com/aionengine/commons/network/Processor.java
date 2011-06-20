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

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author xavier
 */
public abstract class Processor extends Thread {
    private boolean idle;
    private boolean running;
    protected Logger log;
    protected ByteBuffer buffer;

    public Processor(String name, boolean direct) throws IOException {
        super(name);

        this.idle = true;
        this.running = false;
        if (direct) {
            this.buffer = ByteBuffer.allocateDirect(8192 * 2).order(ByteOrder.LITTLE_ENDIAN);
        } else {
            this.buffer = ByteBuffer.allocate(8192 * 2).order(ByteOrder.LITTLE_ENDIAN);
        }
        this.log = Logger.getLogger(getClass());
    }

    public Processor(String name) throws IOException {
        this(name, false);
    }

    abstract void manage(Connection conn) throws RuntimeException;

    abstract int getNumberOfConnections();

    public void close() {
        running = false;
    }

    public boolean isIdle() {
        return idle;
    }

    protected void imIdle() {
        this.idle = true;
    }

    protected void imBusy() {
        this.idle = false;
    }

    protected void imRunning() {
        this.running = true;
    }

    protected boolean running() {
        return this.running;
    }
}
