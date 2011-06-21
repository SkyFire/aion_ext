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
package admincommands;

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.configs.administration.AdminConfig;
import gameserver.dao.InGameShopDAO;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.inGameShop.InGameShop;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.serverpackets.SM_TOLL_INFO;
import gameserver.network.loginserver.LoginServer;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.idfactory.IDFactory;
import gameserver.world.World;
import java.util.List;

public class GameShop extends AdminCommand {

    public GameShop() {
        super("gameshop");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_GAMESHOP) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        if (params.length == 0 || params.length > 7) {
            PacketSendUtility.sendMessage(admin, "No parameters detected please use:\n" + "//gameshop add <itemId> <count> <price> <category> <list> <description>\n" + "//gameshop delete <itemId> <category> <list>\n" + "//gameshop addranking <itemId> <count> <price> <description>\n" + "//gameshop deleteranking <itemId>\n" + "//gameshop settoll <target|player> <toll>\n" + "//gameshop addtoll <target|player> <toll>");
            return;
        }
        int itemId, count, price, category, list, toll, playerToll;
        int id = IDFactory.getInstance().nextId();
        Player player = null;

        if ("delete".startsWith(params[0])) {
            try {
                itemId = Integer.parseInt(params[1]);
                category = Integer.parseInt(params[2]);
                list = Integer.parseInt(params[3]);
            } catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "<itemId, category, list> values must be an integer.");
                return;
            }
            DAOManager.getDAO(InGameShopDAO.class).deleteIngameShopItem(itemId, category, list - 1);
            PacketSendUtility.sendMessage(admin, "You removed [item:" + itemId + "]");
        } else if ("add".startsWith(params[0])) {
            try {
                itemId = Integer.parseInt(params[1]);
                count = Integer.parseInt(params[2]);
                price = Integer.parseInt(params[3]);
                category = Integer.parseInt(params[4]);
                list = Integer.parseInt(params[5]);
            } catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "<itemId, count, price, category, list> values must be an integer.");
                return;
            }
            String description = Util.convertName(params[6]);

            if (list < 1) {
                PacketSendUtility.sendMessage(admin, "<list> : minium is 1.");
                return;
            }

            if (category < 3 || category > 19) {
                PacketSendUtility.sendMessage(admin, "<category> : minimum is 3, maximum is 19.");
                return;
            }

            ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
            if (itemTemplate == null) {
                PacketSendUtility.sendMessage(admin, "Item id is incorrect: " + itemId);
                return;
            }
            List<InGameShop> items = null;
            items = DAOManager.getDAO(InGameShopDAO.class).loadInGameShopSalesRanking(category, list - 1, 1);

            if (items.size() >= 9) {
                PacketSendUtility.sendMessage(admin, "Max items in category is 9.");
                return;
            }
            DAOManager.getDAO(InGameShopDAO.class).saveIngameShopItem(id, itemId, count, price, category, list - 1, 1, description);
            PacketSendUtility.sendMessage(admin, "You added [item:" + itemId + "]");
        } else if ("deleteranking".startsWith(params[0])) {
            try {
                itemId = Integer.parseInt(params[1]);
            } catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "<itemId> value must be an integer.");
                return;
            }
            DAOManager.getDAO(InGameShopDAO.class).deleteIngameShopItem(itemId, -1, -1);
            PacketSendUtility.sendMessage(admin, "You removed from Ranking Sales [item:" + itemId + "]");
        } else if ("addranking".startsWith(params[0])) {
            try {
                itemId = Integer.parseInt(params[1]);
                count = Integer.parseInt(params[2]);
                price = Integer.parseInt(params[3]);
            } catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "<itemId, count, price> value must be an integer.");
                return;
            }
            String description = Util.convertName(params[4]);

            ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);

            if (itemTemplate == null) {
                PacketSendUtility.sendMessage(admin, "Item id is incorrect: " + itemId);
                return;
            }

            List<InGameShop> items = null;
            items = DAOManager.getDAO(InGameShopDAO.class).loadInGameShopSalesRanking(-1, -1, 0);
            if (items.size() >= 6) {
                PacketSendUtility.sendMessage(admin, "Max items in Sales Ranking can be 6.");
                return;
            }
            DAOManager.getDAO(InGameShopDAO.class).saveIngameShopItem(id, itemId, count, price, -1, -1, 0, description);
            PacketSendUtility.sendMessage(admin, "You added Ranking Sales [item:" + itemId + "]");
        } else if ("settoll".startsWith(params[0])) {
            if (params.length == 3) {
                try {
                    toll = Integer.parseInt(params[2]);
                } catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "<toll> value must be an integer.");
                    return;
                }

                String name = Util.convertName(params[1]);

                player = World.getInstance().findPlayer(name);
                if (player == null) {
                    PacketSendUtility.sendMessage(admin, "The specified player is not online.");
                    return;
                }
                player.getClientConnection().getAccount().SetToll(toll);

                PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(toll));
                LoginServer.getInstance().sendTollInfo(toll, player.getClientConnection().getAccount().getName());
                PacketSendUtility.sendMessage(admin, "Toll setted to " + toll + ".");
            }
            if (params.length == 2) {
                try {
                    toll = Integer.parseInt(params[1]);
                } catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "<toll> value must be an integer.");
                    return;
                }

                VisibleObject target = admin.getTarget();
                if (target == null) {
                    PacketSendUtility.sendMessage(admin, "You should select a target first!");
                    return;
                }

                if (target instanceof Player) {
                    player = (Player) target;
                }
                player.getClientConnection().getAccount().SetToll(toll);
                PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(toll));
                LoginServer.getInstance().sendTollInfo(toll, player.getClientConnection().getAccount().getName());
                PacketSendUtility.sendMessage(admin, "Tolls setted to " + toll + ".");
            }
        } else if ("addtoll".startsWith(params[0])) {
            if (params.length == 3) {
                try {
                    toll = Integer.parseInt(params[2]);
                } catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "<toll> value must be an integer.");
                    return;
                }

                String name = Util.convertName(params[1]);

                player = World.getInstance().findPlayer(name);
                if (player == null) {
                    PacketSendUtility.sendMessage(admin, "The specified player is not online.");
                    return;
                }

                playerToll = player.getClientConnection().getAccount().getToll();
                player.getClientConnection().getAccount().SetToll(playerToll + toll);
                PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(playerToll + toll));
                LoginServer.getInstance().sendTollInfo(playerToll + toll, player.getClientConnection().getAccount().getName());
                PacketSendUtility.sendMessage(admin, "You added " + toll + " tolls to Player");
            }
            if (params.length == 2) {
                try {
                    toll = Integer.parseInt(params[1]);
                } catch (NumberFormatException e) {
                    PacketSendUtility.sendMessage(admin, "<toll> value must be an integer.");
                    return;
                }

                VisibleObject target = admin.getTarget();
                if (target == null) {
                    PacketSendUtility.sendMessage(admin, "You should select a target first!");
                    return;
                }

                if (target instanceof Player) {
                    player = (Player) target;
                }

                playerToll = player.getClientConnection().getAccount().getToll();
                player.getClientConnection().getAccount().SetToll(playerToll + toll);
                PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(playerToll + toll));
                LoginServer.getInstance().sendTollInfo(playerToll + toll, player.getClientConnection().getAccount().getName());
                PacketSendUtility.sendMessage(admin, "Toll setted to " + toll + ".");
            }
        } else {
            PacketSendUtility.sendMessage(admin, "You can use only, addtoll, settoll, deleteranking, addranking, delete or add.");
            return;
        }
    }
}
