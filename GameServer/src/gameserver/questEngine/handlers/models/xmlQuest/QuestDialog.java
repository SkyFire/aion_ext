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

package gameserver.questEngine.handlers.models.xmlQuest;

import gameserver.questEngine.handlers.models.xmlQuest.conditions.QuestConditions;
import gameserver.questEngine.handlers.models.xmlQuest.operations.QuestOperations;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestDialog", propOrder = {"conditions", "operations"})
public class QuestDialog {

    protected QuestConditions conditions;
    protected QuestOperations operations;
    @XmlAttribute(required = true)
    protected int id;

    public boolean operate(QuestCookie env, QuestState qs) {
        if (env.getDialogId() != id)
            return false;
        if (conditions == null || conditions.checkConditionOfSet(env)) {
            if (operations != null) {
                return operations.operate(env);
            }
        }
        return false;
    }
}
