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

import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestVar", propOrder = {"npc"})
public class QuestVar {

    protected List<QuestNpc> npc;
    @XmlAttribute(required = true)
    protected int value;

    public boolean operate(QuestCookie env, QuestState qs) {
        int var = -1;
        if (qs != null)
            var = qs.getQuestVars().getQuestVars();
        if (var != value)
            return false;
        for (QuestNpc questNpc : npc) {
            if (questNpc.operate(env, qs))
                return true;
        }
        return false;
    }
}
