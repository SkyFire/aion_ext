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

import gameserver.model.Petition;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_PETITION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.PetitionService;

/**
 * @author zdead
 */
public class CM_PETITION extends AionClientPacket {
    private int action;
    private String title = "";
    private String text = "";
    private String additionalData = "";

    public CM_PETITION(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        action = readH();
        if (action == 2) {
            readD();
        } else {
            String data = readS();
            String[] dataArr = data.split("/", 3);
            title = dataArr[0];
            text = dataArr[1];
            additionalData = dataArr[2];
        }
    }

    @Override
    protected void runImpl() {
        int playerObjId = getConnection().getActivePlayer().getObjectId();
        if (action == 2) {
            if (PetitionService.getInstance().hasRegisteredPetition(playerObjId)) {
                int petitionId = PetitionService.getInstance().getPetition(playerObjId).getPetitionId();
                PetitionService.getInstance().deletePetition(playerObjId);
                sendPacket(new SM_SYSTEM_MESSAGE(1300552, petitionId));
                sendPacket(new SM_SYSTEM_MESSAGE(1300553, 49));
                return;
            }

        }

        if (!PetitionService.getInstance().hasRegisteredPetition(getConnection().getActivePlayer().getObjectId())) {
            Petition petition = PetitionService.getInstance().registerPetition(getConnection().getActivePlayer(), action, title, text, additionalData);
            sendPacket(new SM_PETITION(petition));
        }
    }
}
