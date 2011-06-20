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

package gameserver.model.templates.quest;

import javax.xml.bind.annotation.*;

/**
 * @author MrPoke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestDrop")
public class QuestDrop {

    @XmlAttribute(name = "npc_id")
    protected Integer npcId;
    @XmlAttribute(name = "item_id")
    protected Integer itemId;
    @XmlAttribute
    protected Integer chance;
    @XmlAttribute(name = "drop_each_member")
    protected Boolean dropEachMember;

    @XmlTransient
    protected Integer questId;

    /**
     * Gets the value of the npcId property.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getNpcId() {
        return npcId;
    }

    /**
     * Gets the value of the itemId property.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * Gets the value of the chance property.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getChance() {
        return chance;
    }

    /**
     * Gets the value of the dropEachMember property.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isDropEachMember() {
        return dropEachMember;
    }

    /**
     * @return the questId
     */
    public Integer getQuestId() {
        return questId;
    }

    /**
     * @param questId the questId to set
     */
    public void setQuestId(Integer questId)
	{
		this.questId = questId;
	}

}
