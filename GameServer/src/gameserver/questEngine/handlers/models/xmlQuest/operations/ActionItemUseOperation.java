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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.questEngine.model.QuestCookie;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActionItemUseOperation", propOrder = {"finish"})
public class ActionItemUseOperation extends QuestOperation {

    @XmlElement(required = true)
    protected QuestOperations finish;

    /*
      * (non-Javadoc)
      * @seecom.aionemu.gameserver.questEngine.handlers.models.xmlQuest.operations.QuestOperation#doOperate(com.aionemu.
      * gameserver.services.QuestService, com.aionemu.gameserver.questEngine.model.QuestEnv)
      */

    @Override
    public void doOperate(final QuestCookie env) {
        final Player player = env.getPlayer();
        final Npc npc;
        if (env.getVisibleObject() instanceof Npc)
            npc = (Npc) env.getVisibleObject();
        else
            return;
        final int defaultUseTime = 3000;
        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
                npc.getObjectId(), defaultUseTime, 1));
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, npc.getObjectId()), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
                        npc.getObjectId(), defaultUseTime, 0));
                finish.operate(env);
            }
        }, defaultUseTime);

    }

}
