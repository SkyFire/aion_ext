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
package gameserver.model.siege;

import gameserver.model.templates.siege.SiegeLocationTemplate;

/**
 * @author Sarynth
 */
public class Commander extends SiegeLocation {
    public Commander(SiegeLocationTemplate template) {
        super(template);

        // Commander always 0?
        setVulnerable(false);
        setNextState(0);
    }

    @Override
    public int getInfluenceValue() {
        // unknown
        return 0;
    }
}
