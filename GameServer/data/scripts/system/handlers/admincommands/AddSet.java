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
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.itemset.ItemPart;
import gameserver.model.templates.itemset.ItemSetTemplate;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.world.World;

/**
 * @author Antivirus
 */

public class AddSet extends AdminCommand {

    public AddSet() {
        super("addset");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_ADDSET) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }

        if (params.length == 0 || params.length > 2) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDSET_SYNTAX));
            return;
        }

        int itemSetId = 0;
        Player receiver = null;

        try {
            itemSetId = Integer.parseInt(params[0]);
            receiver = admin;
        }
        catch (NumberFormatException e) {
            receiver = World.getInstance().findPlayer(Util.convertName(params[0]));

            if (receiver == null) {
                PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.PLAYER_NOT_ONLINE, Util.convertName(params[0])));
                return;
            }

            try {
                itemSetId = Integer.parseInt(params[1]);
            }
            catch (NumberFormatException ex) {
                PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.INTEGER_PARAMETER_REQUIRED));
                return;
            }
            catch (Exception ex2) {
                PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.SOMETHING_WRONG_HAPPENED));
                return;
            }
        }

        ItemSetTemplate itemSet = DataManager.ITEM_SET_DATA.getItemSetTemplate(itemSetId);

        if (itemSet == null) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDSET_SET_DOES_NOT_EXISTS, itemSetId));
            return;
        }

        if (receiver.getInventory().getNumberOfFreeSlots() < itemSet.getItempart().size()) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDSET_NOT_ENOUGH_SLOTS, itemSet.getItempart().size()));
            return;
        }

        for (ItemPart setPart : itemSet.getItempart()) {
            long count = ItemService.addItem(receiver, setPart.getItemid(), 1, admin.getName());

            if (count != 0) {
                PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDSET_CANNOT_ADD_ITEM, setPart.getItemid(), receiver.getName()));
                return;
            }
        }
        PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDSET_ADMIN_SUCCESS, itemSetId, receiver.getName()));
        PacketSendUtility.sendMessage(receiver, LanguageHandler.translate(CustomMessageId.COMMAND_ADDSET_PLAYER_SUCCESS, admin.getName()));
    }
}
