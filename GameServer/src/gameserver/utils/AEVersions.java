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
package gameserver.utils;

import com.aionemu.commons.utils.AEInfos;
import com.aionemu.commons.versionning.Version;
import gameserver.GameServer;
import org.apache.log4j.Logger;

/**
 * @author lord_rex
 */
public class AEVersions {
    private static final Logger log = Logger.getLogger(AEVersions.class);
    private static final Version commons = new Version(AEInfos.class);
    private static final Version gameserver = new Version(GameServer.class);

    private static String getRevisionInfo(Version version) {
        return String.format("%-6s", version.getRevision());
    }

    private static String getDateInfo(Version version) {
        return String.format("[ %4s ]", version.getDate());
    }

    public static String[] getFullVersionInfo() {
        return new String[]{
                "Commons Revision: " + getRevisionInfo(commons),
                "Commons Build Date: " + getDateInfo(commons),
                "GS Revision: " + getRevisionInfo(gameserver),
                "GS Build Date: " + getDateInfo(gameserver),
                "..................................................",
                ".................................................."
        };
    }

    public static String getGameRevision(Version version) {
        return String.format("%-6s", version.getRevision());
    }

    public static void printFullVersionInfo() {
        for (String line : getFullVersionInfo())
            log.info(line);
    }
}
