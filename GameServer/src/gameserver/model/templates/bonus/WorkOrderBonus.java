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
import gameserver.model.templates.item.ItemRace;
import gameserver.model.templates.item.ItemTemplate;
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
@XmlType(name = "WorkOrderBonus")
public class WorkOrderBonus extends SimpleCheckItemBonus {

    static final InventoryBonusType type = InventoryBonusType.TASK;

    @XmlAttribute()
    protected int skillId;


    public int getSkillId() {
        return skillId;
    }

    @Override
    public boolean apply(Player player, Item item) {
        int skillPoints = player.getSkillList().getSkillLevel(skillId);

        int startLvl = ((skillId & 0xF) << 10) | Math.max(0, skillPoints / 100 * 100 - 50);
        int endLvl = ((skillId & 0xF) << 10) | skillPoints;

        // materials and products from -50 up to current skill level
        List<Integer> itemIds = DataManager.ITEM_DATA.getBonusItems(type, startLvl, endLvl + 1);
        itemIds.addAll(itemIds); // increase chances twice;

        // recipes to skill level + 50, but not exceeding max limit
        startLvl = ((skillId & 0xF) << 10) | Math.max(0, skillPoints / 100 * 100 - 50);
        endLvl = skillPoints + 50;
        endLvl = ((skillId & 0xF) << 10) | Math.min(skillPoints + 100, endLvl);
        List<Integer> recipeIds = DataManager.ITEM_DATA.getBonusItems(InventoryBonusType.RECIPE,
                startLvl, endLvl);
        itemIds.addAll(recipeIds);
        if (itemIds.size() == 0)
            return true;

        List<Integer> finalIds = new ArrayList<Integer>();
        for (Integer itemId : itemIds) {
            ItemTemplate template = ItemService.getItemTemplate(itemId);
            ItemRace itemRace = template.getOriginRace();
            if (String.valueOf(itemRace) != String.valueOf(player.getCommonData().getRace()) &&
                    itemRace != ItemRace.ALL)
                continue;
            finalIds.add(itemId);
        }

        Collections.shuffle(finalIds);

        int itemId = finalIds.get(Rnd.get(finalIds.size()));
        int itemCount = 1;
        if (itemId >= 169400010 && itemId <= 169405025)
            itemCount = Rnd.get(1, 5);
        return ItemService.addItems(player, Collections.singletonList(new QuestItems(itemId, itemCount)));
    }

    @Override
    public InventoryBonusType getType() {
        return type;
    }

}
