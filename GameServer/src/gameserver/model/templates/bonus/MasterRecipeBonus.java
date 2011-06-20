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

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.SkillList;
import gameserver.model.templates.item.ItemBonus;
import gameserver.model.templates.quest.QuestItems;
import gameserver.services.ItemService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.Collections;
import java.util.List;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MasterRecipeBonus")
public class MasterRecipeBonus extends SimpleCheckItemBonus {

    static final InventoryBonusType type = InventoryBonusType.MASTER_RECIPE;

    @XmlAttribute()
    protected int skillId;

    @Override
    public boolean canApply(Player player, int itemId, int questId) {
        if (!super.canApply(player, itemId, questId))
            return false;
        SkillList skillList = player.getSkillList();
        if (!skillList.isSkillPresent(skillId))
            return false;
        // TODO: should we check?
        switch (skillList.getSkillLevel(skillId)) {
            default:
                return true;
        }
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.itemengine.bonus.AbstractInventoryBonus#apply(com.aionemu.gameserver.model.gameobjects.player.Player)
      */

    @Override
    public boolean apply(Player player, Item item) {
        // if explicitly given, check only if matched types
        if (item != null) {
            ItemBonus bonusInfo = item.getItemTemplate().getBonusInfo();
            if (bonusInfo == null)
                return true;

            List<QuestItems> qi = Collections.singletonList(new QuestItems(item.getItemId(), 1));
            return ItemService.addItems(player, qi);
        }

        // TODO: not known yet, simply ignore
        return true;
    }

    /* (non-Javadoc)
      * @see gameserver.model.templates.bonus.AbstractInventoryBonus#getType()
      */

    @Override
    public InventoryBonusType getType() {
        return type;
    }
}
