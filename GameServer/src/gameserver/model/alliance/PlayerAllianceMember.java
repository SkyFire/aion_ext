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
package gameserver.model.alliance;

import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;

/**
 * @author Sarynth
 */
public class PlayerAllianceMember extends AionObject {
    private Player player;

    private String name;
    private int allianceId;
    private PlayerCommonData playerCommonData;

    /**
     * @param member
     */
    public PlayerAllianceMember(Player player) {
        super(player.getObjectId());
        this.player = player;
        this.name = player.getName();
        this.playerCommonData = player.getCommonData();
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Called from PlayerAliance when a player logs in.
     * Player object should be null when player is off-line.
     * This will store new player object.
     *
     * @param player
     */
    public void onLogin(Player player) {
        this.player = player;
        this.playerCommonData = player.getCommonData();
    }

    /**
     * Called from PlayerAlliance. Sets player to null while disconnected.
     */
    public void onDisconnect() {
        this.player = null;
    }

    /**
     * @return isOnline
     */
    public boolean isOnline() {
        return (player != null);
    }

    /**
     * @return player common data
     */
    public PlayerCommonData getCommonData() {
        return playerCommonData;
    }

    /**
     * @return allianceId
     */
    public int getAllianceId() {
        return allianceId;
    }

    /**
     * @param allianceId
     */
    public void setAllianceId(int allianceId) {
        this.allianceId = allianceId;
	}
}
