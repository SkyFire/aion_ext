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
package gameserver.itemengine.actions;

import gameserver.model.TaskId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Kisk;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.spawnengine.SpawnEngine;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.concurrent.Future;

/**
 * @author Sarynth
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ToyPetSpawnAction")
public class ToyPetSpawnAction extends AbstractItemAction {

    @XmlAttribute
    protected int npcid;

    @XmlAttribute
    protected int time;

    /**
     * @return the Npc Id
     */
    public int getNpcId() {
        return npcid;
    }

    public int getTime() {
        return time;
    }

    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        if (player.getFlyState() != 0) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_BINDSTONE_ITEM_WHILE_FLYING);
            return false;
        }
        if (player.isInInstance()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_FAR_FROM_NPC);
            return false;
        }

        return true;
    }

    @Override
    public void act(Player player, Item parentItem, Item targetItem) {
        SpawnEngine spawnEngine = SpawnEngine.getInstance();
        float x = player.getX();
        float y = player.getY();
        float z = player.getZ();
        byte heading = (byte) ((player.getHeading() + 60) % 120);
        int worldId = player.getWorldId();
        int instanceId = player.getInstanceId();

        SpawnTemplate spawn = spawnEngine.addNewSpawn(worldId,
                instanceId, npcid, x, y, z, heading, 0, 0, true, true);

        final Kisk kisk = spawnEngine.spawnKisk(spawn, instanceId, player);

        // Schedule Despawn Action
        Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                kisk.getController().onDespawn(true);
            }
        }, 7200000);
        // Fixed 2 hours 2 * 60 * 60 * 1000

        kisk.getController().addTask(TaskId.DESPAWN, task);

        //ShowAction
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
                parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId()), true);

        //RemoveKisk
        player.getInventory().removeFromBagByObjectId(parentItem.getObjectId(), 1);
	}
}
