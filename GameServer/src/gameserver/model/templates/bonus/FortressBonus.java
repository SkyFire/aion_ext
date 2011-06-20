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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FortressBonus")
public class FortressBonus extends AbstractInventoryBonus {

    static final InventoryBonusType type = InventoryBonusType.FORTRESS;

    @XmlAttribute()
    protected int checkItem;

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.itemengine.bonus.AbstractInventoryBonus#canApply(com.aionemu.gameserver.model.gameobjects.player.Player, int)
      */

    @Override
    public boolean canApply(Player player, int itemId, int questId) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.itemengine.bonus.AbstractInventoryBonus#apply(com.aionemu.gameserver.model.gameobjects.player.Player)
      */

    @Override
    public boolean apply(Player player, Item item) {
        // TODO Auto-generated method stub
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
