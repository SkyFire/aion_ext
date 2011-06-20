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

package gameserver.world.exceptions;

/**
 * This Exception will be thrown when some object is referencing to World map that do not exist. This Exception
 * indicating serious error.
 *
 * @author -Nemesiss-
 */
@SuppressWarnings("serial")
public class WorldMapNotExistException extends RuntimeException {
    /**
     * Constructs an <code>WorldMapNotExistException</code> with no detail message.
     */
    public WorldMapNotExistException() {
        super();
    }

    /**
     * Constructs an <code>WorldMapNotExistException</code> with the specified detail message.
     *
     * @param s the detail message.
     */
    public WorldMapNotExistException(String s) {
        super(s);
    }

    /**
     * Creates new error
     *
     * @param message exception description
     * @param cause   reason of this exception
     */
    public WorldMapNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates new error
     *
     * @param cause reason of this exception
     */
    public WorldMapNotExistException(Throwable cause)
	{
		super(cause);
	}
}
