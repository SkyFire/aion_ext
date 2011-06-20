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
package gameserver.model.gameobjects;

import java.sql.Timestamp;

/**
 * @author kosyachok
 */
public class Letter extends AionObject {
    private int recipientId;
    private Item attachedItem;
    private long attachedKinahCount;
    private String senderName;
    private String title;
    private String message;
    private boolean unread;
    private boolean express;
    private Timestamp timeStamp;
    private PersistentState persistentState;

    /**
     * @param objId
     * @param attachedItem
     * @param attachedKinah
     * @param title
     * @param message
     * @param senderId
     * @param senderName
     * @param timeStamp     new letter constructor
     */
    public Letter(int objId, int recipientId, Item attachedItem, long attachedKinahCount, String title, String message,
                  String senderName, Timestamp timeStamp, boolean unread, boolean express) {
        super(objId);

        this.recipientId = recipientId;
        this.attachedItem = attachedItem;
        this.attachedKinahCount = attachedKinahCount;
        this.title = title;
        this.message = message;
        this.senderName = senderName;
        this.timeStamp = timeStamp;
        this.unread = unread;
        this.express = express;
        this.persistentState = PersistentState.NEW;

    }

    @Override
    public String getName() {
        return String.valueOf(attachedItem.getItemTemplate().getNameId());
    }

    public int getRecipientId() {
        return recipientId;
    }

    public Item getAttachedItem() {
        return attachedItem;
    }

    public long getAttachedKinah() {
        return attachedKinahCount;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderName() {
        return senderName;
    }

    public boolean isSystemMessage() {
        return (senderName.startsWith("$$"));
    }

    public boolean isUnread() {
        return unread;
    }

    public void setReadLetter() {
        this.unread = false;
        this.persistentState = PersistentState.UPDATE_REQUIRED;
    }

    public boolean isExpress() {
        return express;
    }

    public PersistentState getLetterPersistentState() {
        return persistentState;
    }

    public void removeAttachedItem() {
        this.attachedItem = null;
        this.persistentState = PersistentState.UPDATE_REQUIRED;
    }

    public void removeAttachedKinah() {
        this.attachedKinahCount = 0;
        this.persistentState = PersistentState.UPDATE_REQUIRED;
    }

    public void delete() {
        this.persistentState = PersistentState.DELETED;
    }

    public void setPersistState(PersistentState state) {
        this.persistentState = state;
    }

    /**
     * @return the timeStamp
     */
    public Timestamp getTimeStamp() {
		return timeStamp;
	}
}
