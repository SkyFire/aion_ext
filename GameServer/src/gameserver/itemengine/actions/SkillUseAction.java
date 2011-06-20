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
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.skillengine.effect.Effects;
import gameserver.skillengine.SkillEngine;
import gameserver.skillengine.model.Skill;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillUseAction")
public class SkillUseAction extends AbstractItemAction {
    @XmlAttribute
    protected int skillid;
    @XmlAttribute
    protected int level;

    /**
     * Gets the value of the skillid property.
     */
    public int getSkillid() {
        return skillid;
    }

    /**
     * Gets the value of the level property.
     */
    public int getLevel() {
        return level;
    }

    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        Skill skill = SkillEngine.getInstance().getSkill(player, skillid, level, player.getTarget());
        if (skill == null)
            return false;

        Effects effects = skill.getSkillTemplate().getEffects();
        if (effects != null) {
            if (player.getFlyState() != 0 && effects.isTransform())
                return false;
        }

        return skill.canUseSkill();
    }

    @Override
    public void act(Player player, Item parentItem, Item targetItem) {
        Skill skill = SkillEngine.getInstance().getSkill(player, skillid, level, player.getTarget());
        if (skill != null) {
            skill.setItemTemplate(parentItem.getItemTemplate());
            PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
                    parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId()), true);
            skill.useSkill();

            player.getInventory().removeFromBagByObjectId(parentItem.getObjectId(), 1);
        }
    }

}
