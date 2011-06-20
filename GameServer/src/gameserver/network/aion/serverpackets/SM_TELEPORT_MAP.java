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

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.teleport.TeleporterTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;

/**
 * @author alexa026 , orz
 */
public class SM_TELEPORT_MAP extends AionServerPacket {
    private int targetObjectId;
    private Player player;
    private TeleporterTemplate teleport;
    public Npc npc;

    private static final Logger log = Logger.getLogger(SM_TELEPORT_MAP.class);


    public SM_TELEPORT_MAP(Player player, int targetObjectId, TeleporterTemplate teleport) {
        this.player = player;
        this.targetObjectId = targetObjectId;
        this.npc = (Npc) World.getInstance().findAionObject(targetObjectId);
        this.teleport = teleport;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        if ((teleport != null) && (teleport.getNpcId() != 0) && (teleport.getTeleportId() != 0)) {
            writeD(buf, targetObjectId);
            writeH(buf, teleport.getTeleportId());
        } else {
            PacketSendUtility.sendMessage(player, "Missing info at npc_teleporter.xml with npcid: " + npc.getNpcId());
            log.info(String.format("Missing teleport info with npcid: %d", npc.getNpcId()));
        }
    }
}
