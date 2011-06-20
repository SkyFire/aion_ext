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
package gameserver.model.drop;

import com.aionemu.commons.utils.Rnd;
import gameserver.model.gameobjects.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author ATracer
 * @author Jego
 */
public class DropItem {

    private int index = 0;
    private long count = 0;
    private DropTemplate dropTemplate;
    private List<Integer> questPlayerObjId = null;
    private boolean isFreeForAll = false;
    private long highestValue = 0;
    private Player winningPlayer = null;
    private boolean isItemWonNotCollected = false;
    private boolean registeredSpecial = false;
    private List<Player> playerStatus = null;
    private boolean processed = false;
    private ScheduledFuture<?> specialDropTimeout = null;
    private boolean questDropForEachMemeber = false;
    private int distributionType = 0;

    public DropItem(DropTemplate dropTemplate) {
        this.dropTemplate = dropTemplate;
    }

    /**
     * Regenerates item count upon each call
     */
    public void calculateCount(float rate) {
        // TODO input parameters - based on attacker stats
        // TODO more precise calculations (non-linear)
        if (Rnd.get() * 100 < dropTemplate.getChance() * rate) {
            count = Rnd.get(dropTemplate.getMin(), dropTemplate.getMax());
        }
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * @param count
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * @return the dropTemplate
     */
    public DropTemplate getDropTemplate() {
        return dropTemplate;
    }

    /**
     * @return True if the player can loot the (quest) item.
     */
    public boolean hasQuestPlayerObjId(int playerObjId) {
        if (questPlayerObjId == null || questPlayerObjId.size() == 0)
            return true;

        return questPlayerObjId.contains(playerObjId);
    }

    /**
     * @param playerObjId the playerObjId to add
     */
    public void addQuestPlayerObjId(int playerObjId) {
        if (questPlayerObjId == null)
            questPlayerObjId = new ArrayList<Integer>();

        questPlayerObjId.add(playerObjId);
    }

    /**
     * @param isFreeForAll to set
     */
    public void setFreeForAll(boolean isFreeForAll) {
        this.isFreeForAll = isFreeForAll;
    }

    /**
     * @return isFreeForAll
     */
    public boolean isFreeForAll() {
        return isFreeForAll;
    }

    /**
     * @return highestValue
     */
    public long getHighestValue() {
        return highestValue;
    }

    /**
     * @param highestValue to set
     */
    public void setHighestValue(long highestValue) {
        this.highestValue = highestValue;
    }

    /**
     * @param WinningPlayer to set
     */
    public void setWinningPlayer(Player winningPlayer) {
        this.winningPlayer = winningPlayer;

    }

    /**
     * @return winningPlayer
     */
    public Player getWinningPlayer() {
        return winningPlayer;
    }

    /**
     * @param isItemWonNotCollected to set
     */
    public void setItemWonNotCollected(boolean isItemWonNotCollected) {
        this.isItemWonNotCollected = isItemWonNotCollected;
    }

    /**
     * @return isItemWonNotCollected
     */
    public boolean isItemWonNotCollected() {
        return isItemWonNotCollected;
    }

    /**
     * Set to true when the item is added to the roll/bid list.
     *
     * @param isSpecial
     */
    public void setRegisteredSpecial() {
        this.registeredSpecial = true;
    }

    /**
     * @return True if the item is in the roll/bid list.
     */
    public boolean isRegisteredSpecial() {
        return registeredSpecial;
    }

    /**
     * @param addPlayerStatus
     */
    public void addSpecialPlayer(Player player) {
        if (playerStatus == null) {
            playerStatus = new ArrayList<Player>();
        }
        playerStatus.add(player);
    }

    /**
     * @param delPlayerStatus
     */
    public void delSpecialPlayer(Player player) {
        if (playerStatus == null) {
            return;
        }
        playerStatus.remove(player);
    }

    /**
     * @return the playerStatus
     */
    public int getSpecialPlayerSize() {
        if (playerStatus == null) {
            return 0;
        }
        return playerStatus.size();
    }

    /**
     * @return true if player is found in list
     */
    public boolean containsSpecialPlayer(Player player) {
        if (playerStatus == null) {
            return false;
        }
        return playerStatus.contains(player);
    }

    /**
     * @return True if the item is given to a player, false if it's still in the corpse.
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * Called when the item is given to a player.
     */
    public void setProcessed() {
        this.processed = true;
    }

    /**
     * @return True if the item is a quest drop for each member in the group.
     */
    public boolean isQuestDropForEachMemeber() {
        return questDropForEachMemeber;
    }

    /**
     * The item will drop for each member of the group as a quest item.
     */
    public void setQuestDropForEachMemeber() {
        this.questDropForEachMemeber = true;
    }

    /**
     * @param distributionType
     */
    public void setDistributionType(int distributionType) {
        this.distributionType = distributionType;
    }

    /**
     * @return the DistributionType
     */
    public int getDistributionType() {
        return distributionType;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof DropItem))
            return false;

        DropItem otherDrop = (DropItem) other;
        if (otherDrop.dropTemplate.getItemId() != this.dropTemplate.getItemId())
            return false;
        if (otherDrop.questPlayerObjId == this.questPlayerObjId)
            return true;
        if (otherDrop.questPlayerObjId.size() != this.questPlayerObjId.size())
            return false;

        for (int playerId : this.questPlayerObjId) {
            if (!otherDrop.questPlayerObjId.contains(playerId))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int playerIds = 0;
        if (this.questPlayerObjId != null) {
            for (int playerId : questPlayerObjId) {
                playerIds += playerId;
            }
        }
        // use primary numbers
        int hash = 1000000007 * this.dropTemplate.getItemId();
        hash += 1000000009 * playerIds;
        return hash;
    }

    /**
     * @param specialDropTimeout The timeout task for special item rolling/bidding.
     */
    public void setSpecialDropTimeout(ScheduledFuture<?> specialDropTimeout) {
        this.specialDropTimeout = specialDropTimeout;
    }

    /**
     * Cancel the timeout task for special items.
     */
    public void cancelTimeoutTask() {
        if (specialDropTimeout == null)
            return;

        specialDropTimeout.cancel(true);
        specialDropTimeout = null;
    }
}
