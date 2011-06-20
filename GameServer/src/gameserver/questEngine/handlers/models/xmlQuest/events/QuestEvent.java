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

package gameserver.questEngine.handlers.models.xmlQuest.events;

import gameserver.questEngine.handlers.models.xmlQuest.conditions.QuestConditions;
import gameserver.questEngine.handlers.models.xmlQuest.operations.QuestOperations;
import gameserver.questEngine.model.QuestCookie;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestEvent", propOrder = {"conditions", "operations"})
@XmlSeeAlso({OnKillEvent.class,
        OnTalkEvent.class})
public abstract class QuestEvent {

    protected QuestConditions conditions;
    protected QuestOperations operations;
    @XmlAttribute
    protected List<Integer> ids;

    public boolean operate(QuestCookie env) {
        return false;
    }

    /**
     * Gets the value of the ids property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the ids property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <p/>
     * <pre>
     * getIds().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list {@link Integer }
     */
    public List<Integer> getIds() {
        if (ids == null) {
            ids = new ArrayList<Integer>();
        }
        return this.ids;
    }
}
