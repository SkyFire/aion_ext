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
package gameserver.model.account;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.PlayerAppearance;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.model.legion.Legion;
import gameserver.model.legion.LegionMember;

import java.sql.Timestamp;
import java.util.List;

/**
 * This class is holding information about player, that is displayed on char selection screen, such as: player
 * commondata, player's appearance and creation/deletion time.
 *
 * @author Luno
 * @see PlayerCommonData
 * @see PlayerAppearance
 */
public class PlayerAccountData {
    private PlayerCommonData playerCommonData;
    private PlayerAppearance appereance;
    private List<Item> equipment;
    private Timestamp creationDate = null;
    private Timestamp deletionDate = null;
    private LegionMember legionMember;

    public PlayerAccountData(PlayerCommonData playerCommonData, PlayerAppearance appereance,
                             List<Item> equipment, LegionMember legionMember) {
        this.playerCommonData = playerCommonData;
        this.appereance = appereance;
        this.equipment = equipment;
        this.legionMember = legionMember;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     * Sets deletion date.
     *
     * @param deletionDate
     */
    public void setDeletionDate(Timestamp deletionDate) {
        this.deletionDate = deletionDate;
    }

    /**
     * Get deletion date.
     *
     * @return Timestamp date when char should be deleted.
     */
    public Timestamp getDeletionDate() {
        return deletionDate;
    }

    /**
     * Get time in seconds when this player will be deleted ( 0 if player was not set to be deleted )
     *
     * @return deletion time in seconds
     */
    public int getDeletionTimeInSeconds() {
        return deletionDate == null ? 0 : (int) (deletionDate.getTime() / 1000);
    }

    /**
     * @return the playerCommonData
     */
    public PlayerCommonData getPlayerCommonData() {
        return playerCommonData;
    }

    /**
     * @param playerCommonData the playerCommonData to set
     */
    public void setPlayerCommonData(PlayerCommonData playerCommonData) {
        this.playerCommonData = playerCommonData;
    }

    public PlayerAppearance getAppereance() {
        return appereance;
    }

    /**
     * @param timestamp
     */
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the legionMember
     */
    public Legion getLegion() {
        return legionMember.getLegion();
    }

    /**
     * Returns true if player is a legion member
     *
     * @return true or false
     */
    public boolean isLegionMember() {
        return legionMember != null;
    }

    /**
     * @return the equipment
     */
    public List<Item> getEquipment() {
        return equipment;
    }

    /**
     * @param equipment the equipment to set
     */
    public void setEquipment(List<Item> equipment)
	{
		this.equipment = equipment;
	}
}
