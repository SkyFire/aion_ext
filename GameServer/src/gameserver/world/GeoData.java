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

package gameserver.world;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author alakazaam @contribuitor bluecrime
 */
public class GeoData {
    /**
     * Logger
     */
    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(GeoData.class);

    private GeoData() {
    }

    /**
     * @param worldId
     * @param x
     * @param y
     * @param clientZ
     * @return fixed z
     */
    public static float getZ(int worldId, float x, float y, float clientZ) {
        float z = 0F;
        float x0 = x / 2F;
        float y0 = y / 2F;
        int x1 = (int) (x0);
        int y1 = (int) (y0);
        int x2 = (int) (x0);
        int y2 = (int) (y0) + 1;
        int x3 = (int) (x0) + 1;
        int y3 = (int) (y0);
        int x4 = (int) (x0) + 1;
        int y4 = (int) (y0) + 1;
        float z1 = 0F;
        float z2 = 0F;
        float z3 = 0F;
        float z4 = 0F;
        float z12 = 0F;
        float z23 = 0F;
        float z34 = 0F;
        float z41 = 0F;
        float z1234 = 0F;
        float z2341 = 0F;

        int fraction = 0;
        int number = 0;
        long filePosition = 0;
        int side = 0;

        RandomAccessFile geoFile;
        try {
            geoFile = new RandomAccessFile(new File("./data/geo/" + String.valueOf(worldId) + ".map"), "r");
            side = (int) Math.sqrt(geoFile.length() / 2);
            /*
                * P1
                */
            filePosition = 2 * (long) (y1 + x1 * side);
            geoFile.seek(filePosition);
            number = geoFile.read();
            fraction = geoFile.read();
            z1 = number * 8F + fraction * 0.03125F;

            /*
                * P2
                */
            filePosition = 2 * (long) (y2 + x2 * side);
            geoFile.seek(filePosition);
            number = geoFile.read();
            fraction = geoFile.read();
            z2 = number * 8F + fraction * 0.03125F;

            /*
                * P3
                */
            filePosition = 2 * (long) (y3 + x3 * side);
            geoFile.seek(filePosition);
            number = geoFile.read();
            fraction = geoFile.read();
            z3 = number * 8F + fraction * 0.03125F;

            /*
                * P4
                */
            filePosition = 2 * (long) (y4 + x4 * side);
            geoFile.seek(filePosition);
            number = geoFile.read();
            fraction = geoFile.read();
            z4 = number * 8F + fraction * 0.03125F;

            geoFile.close();

            /*
                * Linear interpolation with square around P(x,y)
                */
            /*
                * P12
                */
            z12 = (y0 - y1) * (z2 - z1) + z1;

            /*
                * P23
                */
            z23 = (x0 - x1) * (z3 - z2) + z2;

            /*
                * P34
                */
            z34 = (y0 - y1) * (z4 - z3) + z3;

            /*
                * P41
                */
            z41 = (x0 - x1) * (z1 - z4) + z4;

            /*
                * P1234
                */
            z1234 = (x0 - x1) * (z34 - z12) + z12;

            /*
                * P2341
                */
            z2341 = (y0 - y1) * (z23 - z41) + z41;

            z = (z1234 + z2341) / 2;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            z = clientZ;
        }
        catch (IOException e) {
            e.printStackTrace();
            z = clientZ;
        }
        return z;
    }
}
