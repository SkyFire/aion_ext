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

import gameserver.model.NpcType;
import gameserver.model.gameobjects.*;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.items.ItemSlot;
import gameserver.model.items.NpcEquippedGear;
import gameserver.model.siege.Artifact;
import gameserver.model.siege.FortressGate;
import gameserver.model.templates.NpcTemplate;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;
import java.util.Map.Entry;

/**
 * This packet is displaying visible npc/monsters.
 *
 * @author -Nemesiss-
 */
public class SM_NPC_INFO extends AionServerPacket {
    /**
     * Visible npc
     */
    private Creature npc;
    private NpcTemplate npcTemplate;
    private int npcId;
    private int masterObjId;
    private String masterName = "";
    @SuppressWarnings("unused")
    private float speed = 0.3f;
    private final int npcTypeId;


    /**
     * Constructs new <tt>SM_NPC_INFO </tt> packet
     *
     * @param npc    visible npc.
     * @param player
     */
    public SM_NPC_INFO(Npc npc, Player player) {
        this.npc = npc;
        npcTemplate = npc.getObjectTemplate();
        npcTypeId = (player.isAggroIconTo(npc.getTribe()) ?
                NpcType.AGGRESSIVE.getId() : npcTemplate.getNpcType().getId());
        npcId = npc.getNpcId();

    }

    /**
     * Constructs new <tt>SM_NPC_INFO </tt> packet
     *
     * @param player
     * @param kisk   - the visible npc.
     */
    public SM_NPC_INFO(Player player, Kisk kisk) {
        this.npc = kisk;
        npcTypeId = (kisk.isAggroFrom(player) ?
                NpcType.ATTACKABLE.getId() : NpcType.NON_ATTACKABLE.getId());
        npcTemplate = kisk.getObjectTemplate();
        npcId = kisk.getNpcId();

        masterObjId = kisk.getOwnerObjectId();
        masterName = kisk.getOwnerName();
    }

    /**
     * @param player
     * @param groupgate - the visible npc.
     */
    public SM_NPC_INFO(Player player, GroupGate groupgate) {
        this.npc = groupgate;
        npcTypeId = (groupgate.isAggroFrom(player) ?
                NpcType.ATTACKABLE.getId() : NpcType.NON_ATTACKABLE.getId());
        npcTemplate = groupgate.getObjectTemplate();
        npcId = groupgate.getNpcId();

        Player owner = (Player) groupgate.getCreator();
        if (owner != null) {
            masterObjId = owner.getObjectId();
            masterName = owner.getName();
        }
    }

    /**
     * @param summon
     */
    public SM_NPC_INFO(Summon summon) {
        this.npc = summon;
        npcTemplate = summon.getObjectTemplate();
        npcTypeId = npcTemplate.getNpcType().getId();
        npcId = summon.getNpcId();
        Player owner = summon.getMaster();
        if (owner != null) {
            masterObjId = owner.getObjectId();
            masterName = owner.getName();
            speed = owner.getGameStats().getCurrentStat(StatEnum.SPEED) / 1000f;
        } else {
            masterName = "LOST";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeF(buf, npc.getX());// x
        writeF(buf, npc.getY());// y
        writeF(buf, npc.getZ());// z
        writeD(buf, npc.getObjectId());
        writeD(buf, npcId);
        writeD(buf, npcId);

        writeC(buf, npcTypeId);

        writeH(buf, npc.getState());// unk 65=normal,0x47 (71)= [dead npc ?]no drop,0x21(33)=fight state,0x07=[dead monster?]
        // no drop
        // 3,19 - wings spread (NPCs)
        // 5,6,11,21 - sitting (NPC)
        // 7,23 - dead (no drop)
        // 8,24 - [dead][NPC only] - looks like some orb of light (no normal mesh)
        // 32,33 - fight mode

        writeC(buf, npc.getHeading());
        writeD(buf, npcTemplate.getNameId());
        writeD(buf, npcTemplate.getTitleId());// titleID

        writeH(buf, 0x00);// unk
        writeC(buf, 0x00);// unk
        writeD(buf, 0x00);// unk

        /*
           * Master Info (Summon, Kisk, Etc)
           */
        writeD(buf, masterObjId);// masterObjectId
        writeS(buf, masterName);// masterName

        int maxHp = npc.getLifeStats().getMaxHp();
        int currHp = npc.getLifeStats().getCurrentHp();
        writeC(buf, 100 * currHp / maxHp);// %hp
        writeD(buf, npc.getGameStats().getCurrentStat(StatEnum.MAXHP));
        writeC(buf, npc.getLevel());// lvl

        NpcEquippedGear gear = npcTemplate.getEquipment();
        if (gear == null)
            writeH(buf, 0x00);
        else {
            writeH(buf, gear.getItemsMask());
            for (Entry<ItemSlot, ItemTemplate> item : gear) // getting it from template ( later if we make sure that npcs actually use items, we'll make Item from it )
            {
                writeD(buf, item.getValue().getTemplateId());
                writeD(buf, 0x00);
                writeD(buf, 0x00);
                writeH(buf, 0x00);
            }
        }

        writeF(buf, 1.5f);// unk
        writeF(buf, npcTemplate.getHeight());
        writeF(buf, npc.getMoveController().getSpeed());// speed

        writeH(buf, 2000);// 0x834 (depends on speed ? )
        writeH(buf, 2000);// 0x834

        if (npc instanceof Servant)
            writeC(buf, 0x01);// unk
        else
            writeC(buf, 0x00);

        /**
         * Movement
         */
        writeF(buf, npc.getX());// x
        writeF(buf, npc.getY());// y
        writeF(buf, npc.getZ());// z
        writeC(buf, 0x00); // move type

        SpawnTemplate spawn = npc.getSpawn();
        if (spawn == null)
        {
        	writeH(buf, 0);
        }
        else
        {
        	int mask = (Math.round(spawn.getStaticid() / 0xFF) < 0 ? 0 : Math.round(spawn.getStaticid() / 0xFF));
        	byte[] statics = new byte[]{(byte) spawn.getStaticid(), (byte) mask};
        	writeB(buf, statics);
        }

        writeC(buf, 0);
        writeC(buf, 0); // all unknown
        writeC(buf, 0);
        writeC(buf, 0);
        writeC(buf, 0);
        writeC(buf, 0);
        writeC(buf, 0);
        writeC(buf, 0);
        writeC(buf, npc.getVisualState()); // visualState

        /**
         * 1 : normal (kisk too)
         * 2 : summon
         * 32 : trap
         * 1024 : holy servant, noble energy
         */
        writeH(buf, npc.getNpcObjectType().getId());
        writeC(buf, 0x00);// unk
        if (npc instanceof Artifact) {
            writeD(buf, 0);
        } else {
            writeD(buf, npc.getTarget() == null ? 0 : npc.getTarget().getObjectId());
		}
	}
}
