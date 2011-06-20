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
package gameserver.model.items;

import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.services.ItemService;

import java.util.TreeSet;

/**
 * @author ATracer
 */
public class ManaStone extends ItemStone {

    private TreeSet<StatModifier> modifiers;

    public ManaStone(int itemObjId, int itemId, int slot, PersistentState persistentState) {
        super(itemObjId, itemId, slot, ItemStoneType.MANASTONE, persistentState);

        ItemTemplate stoneTemplate = ItemService.getItemTemplate(itemId);
        if (stoneTemplate != null && stoneTemplate.getModifiers() != null) {
            this.modifiers = stoneTemplate.getModifiers();
        }
    }

    /**
     * @return modifiers
     */
    public TreeSet<StatModifier> getModifiers() {
        return modifiers;
    }

    public StatModifier getFirstModifier() {
        return modifiers != null ? modifiers.first() : null;
    }

}
