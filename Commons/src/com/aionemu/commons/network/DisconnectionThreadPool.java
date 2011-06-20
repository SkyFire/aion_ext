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

package com.aionemu.commons.network;

/**
 * DisconnectionThreadPool that will be used to execute DisconnectionTask
 *
 * @author -Nemesiss-
 */
public interface DisconnectionThreadPool {
    /**
     * Schedule Disconnection task.
     *
     * @param dt    <code>DisconnectionTask</code>
     * @param delay
     */
    public void scheduleDisconnection(DisconnectionTask dt, long delay);

    /**
     * Waits till all task end work.
     */
    public void waitForDisconnectionTasks();
}
