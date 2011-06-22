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
package gameserver.network.aion.clientpackets;

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.configs.main.InGameShopConfig;
import gameserver.dao.InGameShopDAO;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.inGameShop.InGameShop;
import gameserver.model.templates.mail.MailMessage;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_IN_GAME_SHOP_CATEGORY_LIST;
import gameserver.network.aion.serverpackets.SM_IN_GAME_SHOP_ITEM;
import gameserver.network.aion.serverpackets.SM_IN_GAME_SHOP_LIST;
import gameserver.network.aion.serverpackets.SM_MAIL_SERVICE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_TOLL_INFO;
import gameserver.network.loginserver.LoginServer;
import gameserver.services.ItemService;
import gameserver.services.SystemMailService;
import gameserver.utils.PacketSendUtility;

/**
 * @author PZIKO333
 */

public class CM_IN_GAME_SHOP_INFO extends AionClientPacket {
    private int unk;
    private int categoryId;
    private int listInCategory;
    private String senderName;
    private String senderMessage;

    public CM_IN_GAME_SHOP_INFO(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        this.unk = readC();
        this.categoryId = readD();
        this.listInCategory = readD();
        this.senderName = readS();
        this.senderMessage = readS();
    }

    @Override
    protected void runImpl() {
        if (InGameShopConfig.ENABLE_IN_GAME_SHOP) {
            Player player = (getConnection()).getActivePlayer();
            Storage inventory = player.getInventory();
            InGameShop item = null;

            int playerToll = player.getClientConnection().getAccount().getToll();
            int toll = 0;
            switch (unk) {
                case 0x01:
                    item = (DAOManager.getDAO(InGameShopDAO.class)).loadInGameShopItem(categoryId);
                    if (item != null) {
                        PacketSendUtility.sendPacket(player, new SM_IN_GAME_SHOP_ITEM(categoryId));
                    }
                    break;
                case 0x02:
                    PacketSendUtility.sendPacket(player, new SM_IN_GAME_SHOP_CATEGORY_LIST());
                    break;
                case 0x04:
                    if (categoryId > 1) {
                        player.setNrCategoryInGameShop(categoryId);
                    }
                    PacketSendUtility.sendPacket(player, new SM_IN_GAME_SHOP_LIST(player, listInCategory, 0));
                    break;
                case 0x08:
                    if (categoryId > 1) {
                        player.setNrCategoryInGameShop(categoryId);
                    }
                    PacketSendUtility.sendPacket(player, new SM_IN_GAME_SHOP_LIST(player, listInCategory, 1));
                    break;
                case 0x10:
                    PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(playerToll));
                    break;
                case 0x20:
                    if (inventory.isFull()) {
                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_INVEN_ERROR);
                    } else {
                        item = (DAOManager.getDAO(InGameShopDAO.class)).loadInGameShopItem(categoryId);
                        if (playerToll < item.getItemPrice()) {
                            PacketSendUtility.sendMessage(player, "You dont have enough Toll to buy that.");
                        } else {
                            toll = playerToll - item.getItemPrice();
                            player.getClientConnection().getAccount().SetToll(toll);
                            LoginServer.getInstance().sendTollInfo(toll, player.getClientConnection().getAccount().getName());

                            ItemService.addItem(player, item.getItemId(), item.getItemCount());
                            PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(toll));
                            if (!InGameShopConfig.ENABLE_ITEM_LOG) {
                                break;
                            }
                            (DAOManager.getDAO(InGameShopDAO.class)).saveIngameShopLog(player.getAcountName(), player.getName(), player.getName(), item.getItemId(), item.getItemCount(), item.getItemPrice());
                        }
                    }
                    break;
                case 0x40:
                    if (!(DAOManager.getDAO(PlayerDAO.class)).isNameUsed(senderName)) {
                        PacketSendUtility.sendMessage(player, "Could not find a Recipient by that name.");
                    } else {
                        PlayerCommonData recipientCommonData = (DAOManager.getDAO(PlayerDAO.class)).loadPlayerCommonDataByName(senderName);

                        if (recipientCommonData.getMailboxLetters() >= 100) {
                            PacketSendUtility.sendMessage(player, senderName + "Players mail box is full");
                        } else {
                            if ((!InGameShopConfig.ENABLE_GIFT_OTHER_RACE) && (player.getCommonData().getRace() != recipientCommonData.getRace())) {
                                PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(MailMessage.MAIL_IS_ONE_RACE_ONLY));
                                return;
                            }

                            item = (DAOManager.getDAO(InGameShopDAO.class)).loadInGameShopItem(categoryId);
                            if (playerToll < item.getItemPrice()) {
                                PacketSendUtility.sendMessage(player, "You dont have enough Toll to buy that.");
                            } else {
                                toll = playerToll - item.getItemPrice();
                                player.getClientConnection().getAccount().SetToll(toll);
                                LoginServer.getInstance().sendTollInfo(toll, player.getClientConnection().getAccount().getName());

                                SystemMailService.getInstance().sendMail(player.getName(), senderName, "In Game Shop", senderMessage, item.getItemId(), item.getItemCount(), 0, false);

                                PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(toll));
                                if (!InGameShopConfig.ENABLE_ITEM_LOG) {
                                    break;
                                }
                                (DAOManager.getDAO(InGameShopDAO.class)).saveIngameShopLog(player.getAcountName(), player.getName(), senderName, item.getItemId(), item.getItemCount(), item.getItemPrice());
                            }
                        }
                    }
            }
        }
    }
}