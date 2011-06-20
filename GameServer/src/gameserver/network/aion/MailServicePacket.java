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

package gameserver.network.aion;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Letter;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;

import java.nio.ByteBuffer;
import java.util.Collection;

/**
 * @author kosyachok
 */
public abstract class MailServicePacket extends InventoryPacket {
    protected void writeLettersList(ByteBuffer buf, Collection<Letter> letters, Player player) {
        writeC(buf, 2); // 2 - Mailbox letters update
        writeD(buf, player.getObjectId());
        writeC(buf, 0);
        writeH(buf, player.getMailbox().getFreeSlots()); // mailbox free slots
        int counter = 0;

        for (Letter letter : letters) {
            counter++;
            writeD(buf, letter.getObjectId());
            writeS(buf, letter.getSenderName());
            writeS(buf, letter.getTitle());
            if (letter.isUnread())
                writeC(buf, 0);
            else
                writeC(buf, 1);
            if (letter.getAttachedItem() != null) {
                writeD(buf, letter.getAttachedItem().getObjectId());
                writeD(buf, letter.getAttachedItem().getItemTemplate().getTemplateId());
            } else {
                writeD(buf, 0);
                writeD(buf, 0);
            }
            writeQ(buf, letter.getAttachedKinah());
            writeC(buf, 0);
        }
    }

    protected void writeEmptyLettersList(ByteBuffer buf, Player player) {
        writeC(buf, 2);
        writeD(buf, player.getObjectId());
        writeH(buf, 0);
        writeC(buf, 0);
    }

    protected void writeMailMessage(ByteBuffer buf, int messageId) {
        writeC(buf, 1);
        writeC(buf, messageId);
    }

    protected void writeMailboxState(ByteBuffer buf, int mailCount, int unreadCount, boolean hasExpress) {
        writeC(buf, 0);
        writeC(buf, mailCount);
        writeC(buf, 0);
        writeC(buf, unreadCount);
        writeC(buf, 0);
        writeC(buf, hasExpress ? 1 : 0);
        writeH(buf, 0);
        writeC(buf, 0);
    }

    protected void writeLetterRead(ByteBuffer buf, Letter letter, long time) {
        writeC(buf, 3);
        writeD(buf, letter.getRecipientId());
        writeD(buf, 1);
        writeD(buf, 0);
        writeD(buf, letter.getObjectId());
        writeD(buf, letter.getRecipientId());
        writeS(buf, letter.getSenderName());
        writeS(buf, letter.getTitle());
        writeS(buf, letter.getMessage());

        Item item = letter.getAttachedItem();
        if (item != null) {
            ItemTemplate itemTemplate = item.getItemTemplate();

            writeMailGeneralInfo(buf, item);

            if (itemTemplate.isArmor())
                writeArmorInfo(buf, item, false, false, true);
            else if (itemTemplate.isWeapon())
                writeWeaponInfo(buf, item, false, false, false, true);
            else
                writeGeneralItemInfo(buf, item, false, true);
        } else {
            writeD(buf, 0);
            writeD(buf, 0);
            writeD(buf, 0);
            writeD(buf, 0);
            writeD(buf, 0);
        }

        writeQ(buf, letter.getAttachedKinah());
        writeC(buf, 0);
        writeQ(buf, time / 1000);
        writeC(buf, 0);
    }

    protected void writeLetterState(ByteBuffer buf, int letterId, int attachmentType) {
        writeC(buf, 5);
        writeD(buf, letterId);
        writeC(buf, attachmentType);
        writeC(buf, 1);
    }

    protected void writeLetterDelete(ByteBuffer buf, int letterId) {
        writeC(buf, 6);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, letterId);
    }
}
