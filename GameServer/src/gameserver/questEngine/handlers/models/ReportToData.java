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
import gameserver.questEngine.handlers.template.ReportTo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author MrPoke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportToData")
public class ReportToData extends QuestScriptData {
    @XmlAttribute(name = "start_npc_id", required = true)
    protected int startNpcId;
    @XmlAttribute(name = "end_npc_id", required = true)
    protected int endNpc;
    @XmlAttribute(name = "item_id")
    protected int itemId;
    @XmlAttribute(name = "readable_item_id")
    protected int readableItemId;

    @Override
    public void register(QuestEngine questEngine) {
        ReportTo template = new ReportTo(id, startNpcId, endNpc, itemId, readableItemId);
        questEngine.addQuestHandler(template);
    }
}
