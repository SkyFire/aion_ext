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

package gameserver.model.templates.bonus;

import com.aionemu.commons.utils.Rnd;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemBonus;
import gameserver.model.templates.quest.QuestItems;
import gameserver.services.ItemService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WrappedBonus")
public class WrappedBonus extends AbstractInventoryBonus {
    @XmlAttribute(name = "itemId")
    protected int originalItemId;

    @XmlAttribute(name = "maxCount")
    protected int maxCount;

    @XmlAttribute(name = "type")
    protected InventoryBonusType bonusType;

    public int getWrapperItem() {
        return originalItemId;
    }

    public int getMaxCount() {
        return maxCount;
    }

    @Override
    public boolean apply(Player player, Item item) {
        // if explicitly given, check only if matched types
        if (item != null) {
            ItemBonus bonusInfo = item.getItemTemplate().getBonusInfo();
            if (bonusInfo == null || bonusInfo.getBonusType() != bonusType)
                return true;

            List<QuestItems> qi = Collections.singletonList(new QuestItems(item.getItemId(), Rnd.get(0, maxCount)));
            return ItemService.addItems(player, qi);
        }

        // TODO: needs check what ranges to choose
        List<Integer> itemIds =
                DataManager.ITEM_DATA.getBonusItems(bonusType, bonusLevel, bonusLevel + 10);
        if (itemIds.size() == 0)
            return true;

        List<QuestItems> addedItems = new ArrayList<QuestItems>();
        for (int i = 0; i < maxCount; ++i) {
            int itemId = itemIds.get(Rnd.get(itemIds.size()));
            addedItems.add(new QuestItems(itemId, 1));
        }
        if (ItemService.addItems(player, addedItems)) {
            player.getInventory().removeFromBagByItemId(originalItemId, 1);
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
      * @see gameserver.model.templates.bonus.AbstractInventoryBonus#canApply(com.aionemu.gameserver.model.gameobjects.player.Player, com.aionemu.gameserver.model.gameobjects.Item, int)
      */

    @Override
    public boolean canApply(Player player, int itemId, int questId) {
        // TODO: check how client behaves in retail with originalItemId = 0
        // actually, those item names were missing in client strings

        if (originalItemId == 0 || player.getInventory().getItemCountByItemId(itemId) < count)
            return false;

        // check if wrapper item is present
        if (player.getInventory().getItemCountByItemId(originalItemId) == 0)
            return false;
        //
        return true;
    }

    /* (non-Javadoc)
      * @see gameserver.model.templates.bonus.AbstractInventoryBonus#getType()
      */

    @Override
    public InventoryBonusType getType() {
        return bonusType;
    }
}
