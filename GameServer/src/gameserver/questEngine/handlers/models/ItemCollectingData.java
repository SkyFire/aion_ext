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
import gameserver.questEngine.handlers.template.ItemCollecting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author MrPoke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemCollectingData")
public class ItemCollectingData extends QuestScriptData {

    @XmlAttribute(name = "start_npc_id", required = true)
    protected int startNpcId;
    @XmlAttribute(name = "action_item_id")
    protected int actionItemId;
    @XmlAttribute(name = "end_npc_id")
    protected int endNpcId;
    @XmlAttribute(name = "readable_item_id")
    protected int readableItemId;

    @Override
    public void register(QuestEngine questEngine) {
        ItemCollecting template = new ItemCollecting(id, startNpcId, actionItemId, endNpcId, readableItemId);
        questEngine.addQuestHandler(template);
    }

}
