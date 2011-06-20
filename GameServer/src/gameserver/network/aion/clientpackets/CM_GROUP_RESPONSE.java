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


import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_PLAYER_ID;
import gameserver.services.InstanceService;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * @author Lyahim
 * @author Arkshadow
 */
public class CM_GROUP_RESPONSE extends AionClientPacket {
    private static Logger log = Logger.getLogger(CM_GROUP_RESPONSE.class);

    private int unk1, unk2;

    public CM_GROUP_RESPONSE(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        unk1 = readD();
        unk2 = readC(); //channel?
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();
        log.debug(String.valueOf(unk1) + "," + String.valueOf(unk2));
        if (unk1 == 0 && unk2 == 0) {
            PacketSendUtility.sendPacket(activePlayer, new SM_PLAYER_ID(activePlayer, true)); //init Window
            Map<Integer, Integer> infos = InstanceService.getTimeInfo(activePlayer);
            for (int i : infos.keySet()) {
                int time = infos.get(i);
                if (time != 0)
                    PacketSendUtility.sendPacket(activePlayer, new SM_PLAYER_ID(activePlayer, i, time));

            }
        }
    }
}
