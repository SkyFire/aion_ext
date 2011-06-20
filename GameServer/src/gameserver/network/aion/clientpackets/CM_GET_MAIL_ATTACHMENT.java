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
import gameserver.services.MailService;

/**
 * @author kosyachok
 */
public class CM_GET_MAIL_ATTACHMENT extends AionClientPacket {

    private int mailObjId;
    private int attachmentType;

    public CM_GET_MAIL_ATTACHMENT(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        mailObjId = readD();
        attachmentType = readC(); // 0 - item , 1 - kinah
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        MailService.getInstance().getAttachments(player, mailObjId, attachmentType);
    }
}
