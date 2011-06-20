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

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Rolandas
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BonusTemplate")
public class BonusTemplate {
    @XmlElements({
            @XmlElement(name = "boss", type = BossBonus.class),
            @XmlElement(name = "coin", type = CoinBonus.class),
            @XmlElement(name = "enchant", type = EnchantBonus.class),
            @XmlElement(name = "food", type = FoodBonus.class),
            @XmlElement(name = "fortress", type = FortressBonus.class),
            @XmlElement(name = "goods", type = GoodsBonus.class),
            @XmlElement(name = "island", type = IslandBonus.class),
            @XmlElement(name = "magical", type = MagicalBonus.class),
            @XmlElement(name = "manastone", type = ManastoneBonus.class),
            @XmlElement(name = "master_recipe", type = MasterRecipeBonus.class),
            @XmlElement(name = "material", type = MaterialBonus.class),
            @XmlElement(name = "medal", type = MedalBonus.class),
            @XmlElement(name = "medicine", type = MedicineBonus.class),
            @XmlElement(name = "movie", type = CutSceneBonus.class),
            @XmlElement(name = "recipe", type = RecipeBonus.class),
            @XmlElement(name = "redeem", type = RedeemBonus.class),
            @XmlElement(name = "task", type = WorkOrderBonus.class),
            @XmlElement(name = "wrap", type = WrappedBonus.class)
    })
    protected List<AbstractInventoryBonus> itemBonuses;

    @XmlAttribute()
    private int questId;


    /**
     * Gets the value of the itemBonuses property.
     */
    public List<AbstractInventoryBonus> getItemBonuses() {
        if (itemBonuses == null) {
            itemBonuses = new ArrayList<AbstractInventoryBonus>();
        }
        return this.itemBonuses;
    }

    public int getQuestId() {
        return questId;
    }
}
