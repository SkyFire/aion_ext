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
package gameserver.services;

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.dao.InventoryDAO;
import gameserver.dao.MailDAO;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Letter;
import gameserver.model.gameobjects.player.*;
import gameserver.model.templates.mail.MailMessage;
import gameserver.network.aion.serverpackets.*;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.idfactory.IDFactory;
import gameserver.world.World;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author kosyachok
 * @author Jego, LokiReborn
 */
public class MailService {
    private static final Logger log = Logger.getLogger(MailService.class);

    protected Queue<Player> newPlayers;

    public static final MailService getInstance() {
        return SingletonHolder.instance;
    }

    private MailService() {
        newPlayers = new ConcurrentLinkedQueue<Player>();
    }

    /**
     * TODO split this method
     *
     * @param sender
     * @param recipientName
     * @param title
     * @param message
     * @param attachedItemObjId
     * @param attachedItemCount
     * @param attachedKinahCount
     * @param express
     */
    public void sendMail(Player sender, String recipientName, String title, String message, int attachedItemObjId,
                         int attachedItemCount, int attachedKinahCount, boolean express, boolean isPetitionReply) {

        if (recipientName.length() > 16)
            return;

        if (title.length() > 20)
            title = title.substring(0, 20);

        if (message.length() > 1000)
            message = message.substring(0, 1000);

        if (!DAOManager.getDAO(PlayerDAO.class).isNameUsed(recipientName)) {
            PacketSendUtility.sendPacket(sender, new SM_MAIL_SERVICE(MailMessage.NO_SUCH_CHARACTER_NAME));
            return;
        }

        PlayerCommonData recipientCommonData = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(
                recipientName);
        Player onlineRecipient;

        if (recipientCommonData == null) {
            PacketSendUtility.sendPacket(sender, new SM_MAIL_SERVICE(MailMessage.NO_SUCH_CHARACTER_NAME));
            return;
        }

        if (recipientCommonData.getRace().getRaceId() != sender.getCommonData().getRace().getRaceId() && !isPetitionReply) {
            PacketSendUtility.sendPacket(sender, new SM_MAIL_SERVICE(MailMessage.MAIL_IS_ONE_RACE_ONLY));
            return;
        }

        if (recipientCommonData.isOnline()) {
            onlineRecipient = World.getInstance().findPlayer(recipientCommonData.getPlayerObjId());
            if (!onlineRecipient.getMailbox().haveFreeSlots()) {
                PacketSendUtility.sendPacket(sender, new SM_MAIL_SERVICE(MailMessage.RECIPIENT_MAILBOX_FULL));
                return;
            }
        } else {
            if (recipientCommonData.getMailboxLetters() >= 100) {
                PacketSendUtility.sendPacket(sender, new SM_MAIL_SERVICE(MailMessage.RECIPIENT_MAILBOX_FULL));
                return;
            }
            onlineRecipient = null;
        }

        if (!isPetitionReply)
            if (!validateMailSendPrice(sender, attachedKinahCount, attachedItemObjId, attachedItemCount))
                return;

        Item attachedItem = null;
        int finalAttachedKinahCount = 0;

        int kinahMailCommission = 0;
        int itemMailCommission = 0;

        Storage senderInventory = sender.getInventory();

        if (attachedItemObjId != 0 && attachedItemCount > 0) {
            Item senderItem = senderInventory.getItemByObjId(attachedItemObjId);
            if (senderItem == null)
                return;

            // Check Mailing Untradeable Hack
            if (!senderItem.getItemTemplate().isTradeable())
                return;

            float qualityPriceRate;
            switch (senderItem.getItemTemplate().getItemQuality()) {
                case JUNK:
                case COMMON:
                    qualityPriceRate = 0.02f;
                    break;

                case RARE:
                    qualityPriceRate = 0.03f;
                    break;

                case LEGEND:
                case UNIQUE:
                    qualityPriceRate = 0.04f;
                    break;

                case MYTHIC:
                case EPIC:
                    qualityPriceRate = 0.05f;
                    break;

                default:
                    qualityPriceRate = 0.02f;
                    break;
            }

            if (senderItem.getItemCount() == attachedItemCount) {
                senderInventory.removeFromBag(senderItem, false);
                PacketSendUtility.sendPacket(sender, new SM_DELETE_ITEM(attachedItemObjId));

                senderItem.setEquipped(false);
                senderItem.setEquipmentSlot(0);
                senderItem.setItemLocation(StorageType.MAILBOX.getId());

                attachedItem = senderItem;

                itemMailCommission = Math.round((senderItem.getItemTemplate().getPrice() * attachedItem
                        .getItemCount())
                        * qualityPriceRate);
            } else if (senderItem.getItemCount() > attachedItemCount) {
                attachedItem = ItemService.newItem(senderItem.getItemTemplate().getTemplateId(),
                    attachedItemCount, senderItem.getItemCreator());
                senderItem.decreaseItemCount(attachedItemCount);
                PacketSendUtility.sendPacket(sender, new SM_UPDATE_ITEM(senderItem));

                attachedItem.setEquipped(false);
                attachedItem.setEquipmentSlot(0);
                attachedItem.setItemLocation(StorageType.MAILBOX.getId());

                itemMailCommission = Math.round((attachedItem.getItemTemplate().getPrice() * attachedItem
                        .getItemCount())
                        * qualityPriceRate);
            }
        }

        if (attachedKinahCount > 0) {
            if (senderInventory.getKinahItem().getItemCount() - attachedKinahCount >= 0) {
                finalAttachedKinahCount = attachedKinahCount;
                kinahMailCommission = Math.round(attachedKinahCount * 0.01f);
            }
        }

        Timestamp time = new Timestamp(Calendar.getInstance().getTimeInMillis());

        Letter newLetter = new Letter(IDFactory.getInstance().nextId(), recipientCommonData.getPlayerObjId(),
                attachedItem, finalAttachedKinahCount, title, message, sender.getName(), time, true, express);

        if (!DAOManager.getDAO(MailDAO.class).storeLetter(time, newLetter))
            return;

        /**
         * Calculate kinah
         */
        if (!isPetitionReply) {
            if (senderInventory.getKinahItem().getItemCount() > finalAttachedKinahCount) {
                senderInventory.decreaseKinah(finalAttachedKinahCount);
            } else {
                log.warn("[AUDIT] Mail kinah exploit: " + sender.getObjectId());
                return;
            }

            if (attachedItem != null)
                if (!DAOManager.getDAO(InventoryDAO.class).store(attachedItem, recipientCommonData.getPlayerObjId()))
                    return;

            int finalMailCommission = 0;

            if (express)
                finalMailCommission = 520 + kinahMailCommission + itemMailCommission;
            else
                finalMailCommission = 10 + kinahMailCommission + itemMailCommission;

            if (senderInventory.getKinahItem().getItemCount() > finalMailCommission) {
                senderInventory.decreaseKinah(finalMailCommission);
            } else {
                log.warn("[AUDIT] Mail kinah exploit: " + sender.getObjectId());
                return;
            }
        }

        /**
         * Send mail update packets
         */
        if (onlineRecipient != null) {
            Mailbox recipientMailbox = onlineRecipient.getMailbox();
            recipientMailbox.putLetterToMailbox(newLetter);

            Collection<Letter> lts = onlineRecipient.getMailbox().getLetters();
            int mailCount = 0;
            int unreadMailCount = 0;
            boolean hasExpress = false;

            for (Letter lt : lts) {
                mailCount++;
                if (lt.isUnread()) {
                    unreadMailCount++;
                    if (!hasExpress && lt.isExpress())
                        hasExpress = true;
                }
            }

            PacketSendUtility.sendPacket(onlineRecipient, new SM_MAIL_SERVICE(onlineRecipient, onlineRecipient
                    .getMailbox().getLetters()));

            PacketSendUtility.sendPacket(onlineRecipient, new SM_MAIL_SERVICE(mailCount, unreadMailCount, hasExpress));

            PacketSendUtility.sendPacket(onlineRecipient, new SM_SYSTEM_MESSAGE((express) ? 1300899 : 1300711));
        }

        PacketSendUtility.sendPacket(sender, new SM_MAIL_SERVICE(MailMessage.MAIL_SEND_SECCESS));

        /**
         * Update loaded common data and db if player is offline
         */
        if (!recipientCommonData.isOnline()) {
            recipientCommonData.setMailboxLetters(recipientCommonData.getMailboxLetters() + 1);
            DAOManager.getDAO(MailDAO.class).updateOfflineMailCounter(recipientCommonData);
        }

    }

    public boolean sendSystemMail(String sysSender, String sysTitle, String sysMessage, int reciever, Item item, long kinah) {
        String senderName = "$$" + sysSender;
        String title = sysTitle; //autofilled by senderName, however sometimes variables are passed
        String message = sysMessage; //autofilled by senderName, however sometimes variables are passed
        int receiverId = reciever;
        Item attachedItem = item;
        long attachedKinah = kinah;

        PlayerCommonData recipientCommonData = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonData(receiverId);
        Player onlineRecipient = null;

        if (recipientCommonData == null)
            return false;

        if (recipientCommonData.isOnline()) {
            onlineRecipient = World.getInstance().findPlayer(recipientCommonData.getPlayerObjId());
        }

        attachedItem.setEquipped(false);
        attachedItem.setEquipmentSlot(0);
        attachedItem.setItemLocation(StorageType.MAILBOX.getId());

        Timestamp time = new Timestamp(System.currentTimeMillis());
        Letter newLetter = new Letter(IDFactory.getInstance().nextId(), recipientCommonData.getPlayerObjId(),
                attachedItem, attachedKinah, title, message, senderName, time, true, false);

        if (!DAOManager.getDAO(MailDAO.class).storeLetter(time, newLetter))
            return false;

        if (attachedItem != null)
            if (!DAOManager.getDAO(InventoryDAO.class).store(attachedItem, recipientCommonData.getPlayerObjId()))
                return false;

        /**
         * Send mail update packets
         */
        if (onlineRecipient != null) {
            Mailbox recipientMailbox = onlineRecipient.getMailbox();
            recipientMailbox.putLetterToMailbox(newLetter);

            Collection<Letter> lts = onlineRecipient.getMailbox().getLetters();
            int mailCount = 0;
            int unreadMailCount = 0;
            boolean hasExpress = false;

            for (Letter lt : lts) {
                mailCount++;
                if (lt.isUnread()) {
                    unreadMailCount++;
                    if (!hasExpress && lt.isExpress())
                        hasExpress = true;
                }
            }

            PacketSendUtility.sendPacket(onlineRecipient, new SM_MAIL_SERVICE(onlineRecipient, onlineRecipient
                    .getMailbox().getLetters()));

            PacketSendUtility.sendPacket(onlineRecipient, new SM_MAIL_SERVICE(mailCount, unreadMailCount, hasExpress));
        }

        /**
         * Update loaded common data and db if player is offline
         */
        if (!recipientCommonData.isOnline()) {
            recipientCommonData.setMailboxLetters(recipientCommonData.getMailboxLetters() + 1);
            DAOManager.getDAO(MailDAO.class).updateOfflineMailCounter(recipientCommonData);
        }
        return true;
    }

    /**
     * Read letter with specified letter id
     *
     * @param player
     * @param letterId
     */
    public void readMail(Player player, int letterId) {
        Letter letter = player.getMailbox().getLetterFromMailbox(letterId);
        if (letter == null) {
            log.warn("Cannot read mail " + player.getObjectId() + " " + letterId);
            return;
        }

        PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(player, letter, letter.getTimeStamp().getTime()));
        letter.setReadLetter();
    }

    /**
     * @param player
     * @param letterId
     * @param attachmentType
     */
    public void getAttachments(Player player, int letterId, int attachmentType) {
        Letter letter = player.getMailbox().getLetterFromMailbox(letterId);

        if (letter == null)
            return;

        switch (attachmentType) {
            case 0: {
                Item attachedItem = letter.getAttachedItem();
                if (attachedItem == null)
                    return;
                if (player.getInventory().isFull()) {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_FULL_INVENTORY);
                    return;
                }
                Item inventoryItem = player.getInventory().putToBag(attachedItem);
                PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(Collections.singletonList(inventoryItem)));
                PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(letterId, attachmentType));
                letter.removeAttachedItem();
                break;
            }
            case 1: {
                player.getInventory().increaseKinah(letter.getAttachedKinah());
                PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(letterId, attachmentType));
                letter.removeAttachedKinah();
                break;
            }
        }
    }

    /**
     * @param player
     * @param letterId
     */
    public void deleteMail(Player player, int letterId) {
        Mailbox mailbox = player.getMailbox();

        mailbox.removeLetter(letterId);
        DAOManager.getDAO(MailDAO.class).deleteLetter(letterId);
        PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(letterId));
    }

    /**
     * @param sender
     * @param attachedKinahCount
     * @param attachedItemObjId
     * @param attachedItemCount
     * @return
     */
    private boolean validateMailSendPrice(Player sender, int attachedKinahCount,
        int attachedItemObjId, int attachedItemCount)
    {
        int itemMailCommission = 0;
        int kinahMailCommission = Math.round(attachedKinahCount * 0.01f);

        if (attachedItemObjId != 0) {
            Item senderItem = sender.getInventory().getItemByObjId(attachedItemObjId);
            if (senderItem == null)
                return false;

            float qualityPriceRate;
            switch (senderItem.getItemTemplate().getItemQuality()) {
                case JUNK:
                case COMMON:
                    qualityPriceRate = 0.02f;
                    break;

                case RARE:
                    qualityPriceRate = 0.03f;
                    break;

                case LEGEND:
                case UNIQUE:
                    qualityPriceRate = 0.04f;
                    break;

                case MYTHIC:
                case EPIC:
                    qualityPriceRate = 0.05f;
                    break;

                default:
                    qualityPriceRate = 0.02f;
                    break;
            }

            itemMailCommission = Math.round((senderItem.getItemTemplate().getPrice() * attachedItemCount)
                    * qualityPriceRate);
        }

        int finalMailPrice = 10 + itemMailCommission + kinahMailCommission;

        if (sender.getInventory().getKinahItem().getItemCount() >= finalMailPrice)
            return true;

        return false;
    }

    /**
     * @param player
     */
    public void onPlayerLogin(Player player) {
        ThreadPoolManager.getInstance().schedule(new MailLoadTask(player), 5000);
    }

    /**
     * Task to load all player mail items
     *
     * @author ATracer
     */
    private class MailLoadTask implements Runnable {
        private Player player;

        private MailLoadTask(Player player) {
            this.player = player;
        }

        @Override
        public void run() {
            player.setMailbox(DAOManager.getDAO(MailDAO.class).loadPlayerMailbox(player));
            PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(player, player.getMailbox().getLetters()));

            Collection<Letter> lts = player.getMailbox().getLetters();
            int mailCount = 0;
            int unreadMailCount = 0;
            boolean hasExpress = false;

            for (Letter lt : lts) {
                mailCount++;
                if (lt.isUnread()) {
                    unreadMailCount++;
                    if (!hasExpress && lt.isExpress())
                        hasExpress = true;
                }
            }

            if (unreadMailCount > 0 || hasExpress)
                PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(mailCount, unreadMailCount, hasExpress));
        }
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder
	{
		protected static final MailService instance = new MailService();
	}
}