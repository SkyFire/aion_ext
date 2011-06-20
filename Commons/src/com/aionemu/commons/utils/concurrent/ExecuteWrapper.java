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

package com.aionemu.commons.utils.concurrent;

import javolution.text.TextBuilder;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @author NB4L1
 */
public class ExecuteWrapper implements Runnable {
    private static final Logger log = Logger.getLogger(ExecuteWrapper.class);

    private final Runnable runnable;

    public ExecuteWrapper(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public final void run() {
        ExecuteWrapper.execute(runnable, getMaximumRuntimeInMillisecWithoutWarning());
    }

    protected long getMaximumRuntimeInMillisecWithoutWarning() {
        return Long.MAX_VALUE;
    }

    public static void execute(Runnable runnable) {
        execute(runnable, Long.MAX_VALUE);
    }

    public static void execute(Runnable runnable, long maximumRuntimeInMillisecWithoutWarning) {
        long begin = System.nanoTime();

        try {
            runnable.run();
        }
        catch (RuntimeException e) {
            log.warn("Exception in a Runnable execution:", e);
        }
        finally {
            long runtimeInNanosec = System.nanoTime() - begin;
            Class<? extends Runnable> clazz = runnable.getClass();

            RunnableStatsManager.handleStats(clazz, runtimeInNanosec);

            long runtimeInMillisec = TimeUnit.NANOSECONDS.toMillis(runtimeInNanosec);

            if (runtimeInMillisec > maximumRuntimeInMillisecWithoutWarning) {
                TextBuilder tb = TextBuilder.newInstance();

                tb.append(clazz);
                tb.append(" - execution time: ");
                tb.append(runtimeInMillisec);
                tb.append("msec");

                log.warn(tb.toString());

                TextBuilder.recycle(tb);
            }
        }
    }
}