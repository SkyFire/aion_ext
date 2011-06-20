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
package gameserver.model;

/**
 * @author Luno
 */
public enum NpcType {
    /**
     * These are regular monsters
     */
    ATTACKABLE(0),
    /**
     * These are monsters that are pre-aggressive
     */
    AGGRESSIVE(8),
    /**
     * These are non attackable NPCs
     */
    NON_ATTACKABLE(38),
    /**
     * Binding obelisks *
     */
    RESURRECT(38), //TODO check
    /**
     * Mail boxes *
     */
    POSTBOX(38),
    /**
     * Action item *
     */
    USEITEM(38),
    /**
     * Portals *
     */
    PORTAL(38),
    /**
     * Artifact *
     */
    ARTIFACT(38),
    /**
     * High Protector *
     */
    ARTIFACT_PROTECTOR(0);


    private int someClientSideId;

    private NpcType(int id) {
        this.someClientSideId = id;
    }

    public int getId() {
        return someClientSideId;
    }
}
