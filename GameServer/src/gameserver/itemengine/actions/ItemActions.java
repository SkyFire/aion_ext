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

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemActions")
public class ItemActions {

    @XmlElements({
            @XmlElement(name = "skilllearn", type = SkillLearnAction.class),
            @XmlElement(name = "extract", type = ExtractAction.class),
            @XmlElement(name = "skilluse", type = SkillUseAction.class),
            @XmlElement(name = "enchant", type = EnchantItemAction.class),
            @XmlElement(name = "queststart", type = QuestStartAction.class),
            @XmlElement(name = "dye", type = DyeAction.class),
            @XmlElement(name = "craftlearn", type = CraftLearnAction.class),
            @XmlElement(name = "toypetspawn", type = ToyPetSpawnAction.class),
            @XmlElement(name = "read", type = ReadAction.class)
    })
    protected List<AbstractItemAction> itemActions;

    /**
     * Gets the value of the itemActions property.
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link SkillLearnAction }
     * {@link SkillUseAction }
     */
    public List<AbstractItemAction> getItemActions() {
        if (itemActions == null)
            itemActions = new ArrayList<AbstractItemAction>();
        return this.itemActions;
    }

    public List<SkillLearnAction> getSkillLearnActions() {
        List<SkillLearnAction> result = new ArrayList<SkillLearnAction>();
        if (itemActions == null)
            return result;

        for (AbstractItemAction action : itemActions)
            if (action instanceof SkillLearnAction)
                result.add((SkillLearnAction) action);
        return result;
    }

    public List<ExtractAction> getExtractActions() {
        List<ExtractAction> result = new ArrayList<ExtractAction>();
        if (itemActions == null)
            return result;

        for (AbstractItemAction action : itemActions)
            if (action instanceof ExtractAction)
                result.add((ExtractAction) action);
        return result;
    }

    public List<SkillUseAction> getSkillUseActions() {
        List<SkillUseAction> result = new ArrayList<SkillUseAction>();
        if (itemActions == null)
            return result;

        for (AbstractItemAction action : itemActions)
            if (action instanceof SkillUseAction)
                result.add((SkillUseAction) action);
        return result;
    }

    public List<EnchantItemAction> getEnchantActions() {
        List<EnchantItemAction> result = new ArrayList<EnchantItemAction>();
        if (itemActions == null)
            return result;

        for (AbstractItemAction action : itemActions)
            if (action instanceof EnchantItemAction)
                result.add((EnchantItemAction) action);
        return result;
    }

    public List<QuestStartAction> getQuestStartActions() {
        List<QuestStartAction> result = new ArrayList<QuestStartAction>();
        if (itemActions == null)
            return result;

        for (AbstractItemAction action : itemActions)
            if (action instanceof QuestStartAction)
                result.add((QuestStartAction) action);
        return result;
    }

    public List<DyeAction> getDyeActions() {
        List<DyeAction> result = new ArrayList<DyeAction>();
        if (itemActions == null)
            return result;

        for (AbstractItemAction action : itemActions)
            if (action instanceof DyeAction)
                result.add((DyeAction) action);
        return result;
    }

    public List<CraftLearnAction> getCraftLearnActions() {
        List<CraftLearnAction> result = new ArrayList<CraftLearnAction>();
        if (itemActions == null)
            return result;

        for (AbstractItemAction action : itemActions)
            if (action instanceof CraftLearnAction)
                result.add((CraftLearnAction) action);
        return result;
    }

    public List<ToyPetSpawnAction> getToyPetSpawnActions() {
        List<ToyPetSpawnAction> result = new ArrayList<ToyPetSpawnAction>();
        if (itemActions == null)
            return result;

        for (AbstractItemAction action : itemActions)
            if (action instanceof ToyPetSpawnAction)
                result.add((ToyPetSpawnAction) action);
        return result;
    }

    public List<ReadAction> getReadActions() {
        List<ReadAction> result = new ArrayList<ReadAction>();
        if (itemActions == null)
            return result;

        for (AbstractItemAction action : itemActions)
            if (action instanceof ReadAction)
                result.add((ReadAction) action);
        return result;
    }

}