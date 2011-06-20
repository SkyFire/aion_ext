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

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

/**
 * @author Phantom, ATracer
 */
public class Remove extends AdminCommand {

    public Remove() {
        super("remove");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_REMOVE) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
            return;
        }

        if (params.length < 2) {
            PacketSendUtility.sendMessage(admin, "syntax //remove <player name> <item id> <quantity>");
            return;
        }

        int itemId = 0;
        long itemCount = 1;
        Player target = World.getInstance().findPlayer(Util.convertName(params[0]));
        if (target == null) {
            PacketSendUtility.sendMessage(admin, "Could not find an online player with that name.");
            return;
        }

        try {
            itemId = Integer.parseInt(params[1]);
            if (params.length == 3) {
                itemCount = Long.parseLong(params[2]);
            }
        }
        catch (NumberFormatException e) {
            PacketSendUtility.sendMessage(admin, "Parameter needs to be an integer.");
            return;
        }

        Storage bag = target.getInventory();

        long itemsInBag = bag.getItemCountByItemId(itemId);
        if (itemsInBag == 0) {
            //Kinah cannot be removed from player's inventory using this command, bug?
            PacketSendUtility.sendMessage(admin, "Items with that id are not found in that player's inventory.");
            return;
        }

        Item item = bag.getFirstItemByItemId(itemId);
        if (itemsInBag <= itemCount) {
            bag.removeFromBag(item, true);
            PacketSendUtility.sendPacket(target, new SM_DELETE_ITEM(item.getObjectId()));
        } else {
            bag.removeFromBagByObjectId(item.getObjectId(), itemCount);
            PacketSendUtility.sendPacket(target, new SM_UPDATE_ITEM(item));
        }
        PacketSendUtility.sendMessage(admin, "Item(s) successfully removed from player " + target.getName());
        PacketSendUtility.sendMessage(target, "Admin " + admin.getName() + " removed an item from you");
    }
}
