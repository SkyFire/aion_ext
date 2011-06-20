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
import gameserver.services.HTMLService;

import org.apache.log4j.Logger;

/**
 * @author lhw, Kaipo and ginho1
 */
public class CM_QUESTIONNAIRE extends AionClientPacket {
    private static final Logger log = Logger.getLogger(CM_QUESTIONNAIRE.class);

    private int objectId;
    private int unknown1;
    private int choice;
    private int unknown2;
    private int unknown3;

    public CM_QUESTIONNAIRE(int opcode) {
        super(opcode);
    }

    /* (non-Javadoc)
      * @see com.aionemu.commons.network.packet.BaseClientPacket#readImpl()
      */

    @Override
    protected void readImpl() {
        objectId = readD(); // when one option given is the Player ID.
        unknown1 = readH(); // seems to be always 34.
        choice = (readH() - 48); // removing 48 gives the actual option value.
        unknown2 = readH(); // seems to be always 34.
        unknown3 = readH(); // seems to be always 0.
    }

    /* (non-Javadoc)
      * @see com.aionemu.commons.network.packet.BaseClientPacket#runImpl()
      */

    @Override
    protected void runImpl() {
        //log.info("CM_QUESTION_RESPONSE - " + " objectId:" + objectId + " unknown1:" + unknown1 + " choice:" + choice + " unknown2:" + unknown2 + " unknown3:" + unknown3);
        if (objectId > 0) {
            Player player = getConnection().getActivePlayer();
            HTMLService.getMessage(player, objectId, choice);
        }
    }
}
