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
package gameserver.itemengine.actions;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

public class ReadAction extends AbstractItemAction {

    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        if (parentItem == null)
            return false;

        ItemTemplate template = parentItem.getItemTemplate();
        int questId = template.getItemQuestId();
        ItemActions actions = template.getActions();

        if (questId == 0 || actions == null)
            return false;

        for (AbstractItemAction action : actions.getItemActions())
            if (action instanceof ReadAction)
                return true;

        return false;
    }

    @Override
    public void act(final Player player, Item parentItem, Item targetItem) {
        final int itemObjId = parentItem.getObjectId();
        final int id = parentItem.getItemTemplate().getTemplateId();

        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 50, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
            }
        }, 50);
    }

}
