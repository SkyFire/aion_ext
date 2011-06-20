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
package gameserver.model.gameobjects.player;

import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Player macrosses collection, contains all player macrosses.
 * <p/>
 * Created on: 13.07.2009 16:28:23
 *
 * @author Aquanox
 */
public class MacroList {
    /**
     * Class logger
     */
    private static final Logger logger = Logger.getLogger(MacroList.class);

    /**
     * Container of macrosses, position to xml.
     */
    private final Map<Integer, String> macrosses;

    /**
     * Creates an empty macro list
     */
    public MacroList() {
        this.macrosses = new HashMap<Integer, String>();
    }

    /**
     * Create new instance of <tt>MacroList</tt>.
     *
     * @param arg
     */
    public MacroList(Map<Integer, String> arg) {
        this.macrosses = arg;
    }

    /**
     * Returns map with all macrosses
     *
     * @return all macrosses
     */
    public Map<Integer, String> getMacrosses() {
        return Collections.unmodifiableMap(macrosses);
    }

    /**
     * Add macro to the collection.
     *
     * @param macroPosition Macro order.
     * @param macroXML      Macro Xml contents.
     * @return <tt>true</tt> if macro addition was successful, and it can be stored into database. Otherwise
     *         <tt>false</tt>.
     */
    public synchronized boolean addMacro(int macroPosition, String macroXML) {
        if (macrosses.containsKey(macroPosition)) {
            macrosses.remove(macroPosition);
            macrosses.put(macroPosition, macroXML);
            return false;
        }

        macrosses.put(macroPosition, macroXML);
        return true;
    }

    /**
     * Remove macro from the list.
     *
     * @param macroPosition
     * @return <tt>true</tt> if macro deletion was successful, and changes can be stored into database. Otherwise
     *         <tt>false</tt>.
     */
    public synchronized boolean removeMacro(int macroPosition) {
        String m = macrosses.remove(macroPosition);
        if (m == null)//
        {
            logger.warn("Trying to remove non existing macro.");
            return false;
        }
        return true;
    }

    /**
     * Returns count of available macrosses.
     *
     * @return count of available macrosses.
     */
    public int getSize() {
        return macrosses.size();
    }

    /**
     * Returns an entry set of macro id to macro contents.
     */
    public Set<Entry<Integer, String>> entrySet()
	{
		return Collections.unmodifiableSet(getMacrosses().entrySet());
	}
}
