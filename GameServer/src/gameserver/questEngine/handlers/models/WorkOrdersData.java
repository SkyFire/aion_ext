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

package gameserver.questEngine.handlers.models;

import gameserver.model.templates.quest.QuestItems;
import gameserver.questEngine.QuestEngine;
import gameserver.questEngine.handlers.template.WorkOrders;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkOrdersData", propOrder = {"giveComponent"})
public class WorkOrdersData extends QuestScriptData {

    @XmlElement(name = "give_component", required = true)
    protected List<QuestItems> giveComponent;
    @XmlAttribute(name = "start_npc_id", required = true)
    protected int startNpcId;
    @XmlAttribute(name = "recipe_id", required = true)
    protected int recipeId;

    /**
     * Gets the value of the giveComponent property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the giveComponent property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <p/>
     * <pre>
     * getGiveComponent().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list {@link QuestItems }
     */
    public List<QuestItems> getGiveComponent() {
        if (giveComponent == null) {
            giveComponent = new ArrayList<QuestItems>();
        }
        return this.giveComponent;
    }

    /**
     * Gets the value of the startNpcId property.
     */
    public int getStartNpcId() {
        return startNpcId;
    }

    /**
     * Gets the value of the recipeId property.
     */
    public int getRecipeId() {
        return recipeId;
    }

    /*
      * (non-Javadoc)
      * @see com.aionemu.gameserver.questEngine.handlers.models.QuestScriptData#register()
      */

    @Override
    public void register(QuestEngine questEngine) {
        questEngine.addQuestHandler(new WorkOrders(this));
    }
}
