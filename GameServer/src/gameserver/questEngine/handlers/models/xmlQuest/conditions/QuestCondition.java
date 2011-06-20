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

package gameserver.questEngine.handlers.models.xmlQuest.conditions;

import gameserver.questEngine.model.ConditionOperation;
import gameserver.questEngine.model.QuestCookie;

import javax.xml.bind.annotation.*;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestCondition")
@XmlSeeAlso({NpcIdCondition.class, DialogIdCondition.class, PcInventoryCondition.class, QuestVarCondition.class,
        QuestStatusCondition.class})
public abstract class QuestCondition {

    @XmlAttribute(required = true)
    protected ConditionOperation op;

    /**
     * Gets the value of the op property.
     *
     * @return possible object is {@link ConditionOperation }
     */
    public ConditionOperation getOp() {
        return op;
    }

    public abstract boolean doCheck(QuestCookie env);

}
