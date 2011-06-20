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

import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author -Nemesiss-, ZeroSignal
 */
public class Util {
    /**
     * @param s
     */
    public static void printSection(String s) {
        s = "-[ " + s + " ]";

        while (s.length() < 79)
            s = "=" + s;

        System.out.println(s);
    }

    /**
     * Convert data from given ByteBuffer to hex
     *
     * @param data
     * @return hex
     */
    public static String toHex(ByteBuffer data) {
        StringBuilder result = new StringBuilder();
        int counter = 0;
        int b;
        while (data.hasRemaining()) {
            if (counter % 16 == 0)
                result.append(String.format("%04X: ", counter));

            b = data.get() & 0xff;
            result.append(String.format("%02X ", b));

            counter++;
            if (counter % 16 == 0) {
                result.append("  ");
                toText(data, result, 16);
                result.append("\n");
            }
        }
        int rest = counter % 16;
        if (rest > 0) {
            for (int i = 0; i < 17 - rest; i++) {
                result.append("   ");
            }
            toText(data, result, rest);
        }
        return result.toString();
    }

    /**
     * Gets last <tt>cnt</tt> read bytes from the <tt>data</tt> buffer and puts into <tt>result</tt> buffer in special
     * format:
     * <ul>
     * <li>if byte represents char from partition 0x1F to 0x80 (which are normal ascii chars) then it's put into buffer
     * as it is</li>
     * <li>otherwise dot is put into buffer</li>
     * </ul>
     *
     * @param data
     * @param result
     * @param cnt
     */
    private static void toText(ByteBuffer data, StringBuilder result, int cnt) {
        int charPos = data.position() - cnt;
        for (int a = 0; a < cnt; a++) {
            int c = data.get(charPos++);
            if (c > 0x1f && c < 0x80)
                result.append((char) c);
            else
                result.append('.');
        }
    }

    /**
     * Converts name to valid pattern For example : "atracer" -> "Atracer"
     *
     * @param name
     * @return String
     */
    public static String convertName(String name) {
        if (!name.isEmpty())
            return name.substring(0, 1).toUpperCase() + name.toLowerCase().substring(1);
        else
            return "";
    }

    public static String[] splitCommandArgs(String rawData) {
        List<String> matchList = new ArrayList<String>();

        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'"); 
        Matcher regexMatcher = regex.matcher(rawData); 

        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                matchList.add(regexMatcher.group(2));
            } else {
                matchList.add(regexMatcher.group());
            }
        }

        String matchArray[] = new String[matchList.size()];
        matchArray = matchList.toArray(matchArray);
        return matchArray; 
    }

    public static String[] splitStringFixedLen(String data, int interval) {
        List<String> dataPiece = new ArrayList<String>();

        int addedOffset;
        for (int offset = 0; offset < data.length(); offset += addedOffset) {
            String subData = data.substring(offset, Math.min(data.length(), (offset + interval)));
            addedOffset = subData.lastIndexOf('\n');
            if (addedOffset >= 0) {
                subData = subData.substring(0, addedOffset);
                ++addedOffset;
            }
            else {
                addedOffset = interval;
            }
            dataPiece.add(subData);
        }

        String[] result = new String[dataPiece.size()];
        dataPiece.toArray(result);
        return result;
    }
}
