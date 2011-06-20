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

package gameserver.questEngine.handlers.models.xmlQuest.operations;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetQuestStatusOperation")
public class SetQuestStatusOperation
        extends QuestOperation {

    @XmlAttribute(required = true)
    protected QuestStatus status;

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.questEngine.handlers.models.xmlQuest.operations.QuestOperation#doOperate(com.aionemu.gameserver.questEngine.model.QuestEnv)
      */

    @Override
    public void doOperate(QuestCookie env) {
        Player player = env.getPlayer();
        int questId = env.getQuestId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null) {
            qs.setStatus(status);
            PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
            if (qs.getStatus() == QuestStatus.COMPLETE)
                player.getController().updateNearbyQuests();
        }
    }
}
