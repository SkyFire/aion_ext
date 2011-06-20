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

package gameserver.network.aion.clientpackets;


import gameserver.dataholders.DataManager;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SHOW_NPC_ON_MAP;

/**
 * @author Lyahim
 */
public class CM_OBJECT_SEARCH extends AionClientPacket {
    private int npcId;

    /**
     * Constructs new client packet instance.
     *
     * @param opcode
     */
    public CM_OBJECT_SEARCH(int opcode) {
        super(opcode);

    }

    /**
     * Nothing to do
     */
    @Override
    protected void readImpl() {
        this.npcId = readD();
    }

    /**
     * Logging
     */
    @Override
    protected void runImpl() {
        SpawnTemplate spawnTemplate = DataManager.SPAWNS_DATA.getFirstSpawnByNpcId(npcId);
        if (spawnTemplate != null) {
            sendPacket(new SM_SHOW_NPC_ON_MAP(npcId, spawnTemplate.getWorldId(), spawnTemplate.getX(),
                    spawnTemplate.getY(), spawnTemplate.getZ()));
		}
	}
}
