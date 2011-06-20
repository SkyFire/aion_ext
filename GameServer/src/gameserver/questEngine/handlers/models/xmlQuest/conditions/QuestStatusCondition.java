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

import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestStatusCondition")
public class QuestStatusCondition extends QuestCondition {

    @XmlAttribute(required = true)
    protected QuestStatus value;
    @XmlAttribute(name = "quest_id")
    protected Integer questId;


    /* (non-Javadoc)
      * @see com.aionemu.gameserver.questEngine.handlers.template.xmlQuest.condition.QuestCondition#doCheck(com.aionemu.gameserver.questEngine.model.QuestEnv)
      */

    @Override
    public boolean doCheck(QuestCookie env) {
        Player player = env.getPlayer();
        int qstatus = 0;
        int id = env.getQuestId();
        if (questId != null)
            id = questId;
        QuestState qs = player.getQuestStateList().getQuestState(id);
        if (qs != null)
            qstatus = qs.getStatus().value();

        switch (getOp()) {
            case EQUAL:
                return qstatus == value.value();
            case GREATER:
                return qstatus > value.value();
            case GREATER_EQUAL:
                return qstatus >= value.value();
            case LESSER:
                return qstatus < value.value();
            case LESSER_EQUAL:
                return qstatus <= value.value();
            case NOT_EQUAL:
                return qstatus != value.value();
            default:
                return false;
        }
    }
}
