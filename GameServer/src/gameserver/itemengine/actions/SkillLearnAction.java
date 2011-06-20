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

import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.skillengine.model.learn.SkillClass;
import gameserver.skillengine.model.learn.SkillRace;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillLearnAction")
public class SkillLearnAction extends AbstractItemAction {
    @XmlAttribute
    protected int skillid;
    @XmlAttribute
    protected int level;
    @XmlAttribute(name = "class")
    protected SkillClass playerClass;
    @XmlAttribute
    protected SkillRace race;

    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        return validateConditions(player);
    }

    @Override
    public void act(Player player, Item parentItem, Item targetItem) {
        //item animation and message
        ItemTemplate itemTemplate = parentItem.getItemTemplate();
        //PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.USE_ITEM(itemTemplate.getDescription()));
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
                parentItem.getObjectId(), itemTemplate.getTemplateId()), true);
        //add skill
        player.getSkillList().addSkill(player, skillid, 1, true);
        //remove book from inventory (assuming its not stackable)
        Item item = player.getInventory().getItemByObjId(parentItem.getObjectId());
        if (item == null)
            return;

        player.getInventory().removeFromBag(item, true);
        PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(parentItem.getObjectId()));
    }

    private boolean validateConditions(Player player) {
        //1. check player level
        if (player.getCommonData().getLevel() < level)
            return false;

        PlayerClass pc = player.getCommonData().getPlayerClass();

        if (!validateClass(pc))
            return false;

        //4. check player race and SkillRace.ALL
        if (player.getCommonData().getRace().ordinal() != race.ordinal()
                && race != SkillRace.ALL)
            return false;
        //5. check whether this skill is already learned
        if (player.getSkillList().isSkillPresent(skillid))
            return false;

        return true;
    }

    private boolean validateClass(PlayerClass pc) {
        boolean result = false;
        //2. check if current class is second class and book is for starting class
        if (!pc.isStartingClass() && PlayerClass.getStartingClassFor(pc).ordinal() == playerClass.ordinal())
            result = true;
        //3. check player class and SkillClass.ALL
        if (pc.ordinal() == playerClass.ordinal()
                || playerClass == SkillClass.ALL)
            result = true;

        return result;
    }
}
