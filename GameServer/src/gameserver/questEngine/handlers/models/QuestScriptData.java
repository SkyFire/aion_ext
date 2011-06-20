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

import gameserver.questEngine.QuestEngine;

import javax.xml.bind.annotation.*;

/**
 * @author MrPoke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestScriptData")
@XmlSeeAlso({ReportToData.class,
        MonsterHuntData.class,
        ItemCollectingData.class,
        WorkOrdersData.class,
        XmlQuestData.class})
public abstract class QuestScriptData {

    @XmlAttribute(required = true)
    protected int id;

    /**
     * Gets the value of the id property.
     */
    public int getId() {
        return id;
    }

    public abstract void register(QuestEngine questEngine);
}
