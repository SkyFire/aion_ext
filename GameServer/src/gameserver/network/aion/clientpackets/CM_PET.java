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
import gameserver.services.ToyPetService;
import gameserver.utils.PacketSendUtility;

/**
 * @author xitanium
 */
public class CM_PET extends AionClientPacket {

    private int actionId;
    private int petId;
    private String petName;
    private int decorationId;
    private int eggObjId;

    @SuppressWarnings("unused")
    private int unk2;
    @SuppressWarnings("unused")
    private int unk3;
    @SuppressWarnings("unused")
    private int unk5;
    @SuppressWarnings("unused")
    private int unk6;

    public CM_PET(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        actionId = readH();
        switch (actionId) {
            case 1:
                //adopt
                eggObjId = readD();
                petId = readD();
                unk2 = readC();
                unk3 = readD();
                decorationId = readD();
                unk5 = readD();
                unk6 = readD();
                petName = readS();
                break;
            case 2:
            case 3:
            case 4:
                petId = readD();
                break;
            case 10:
                petId = readD();
                petName = readS();
                break;
            default:
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        switch (actionId) {
            case 1:
                // adopt
                player.getInventory().removeFromBagByObjectId(eggObjId, 1);
                ToyPetService.getInstance().createPetForPlayer(player, petId, decorationId, petName);
                break;
            case 2:
                // surrender
                ToyPetService.getInstance().surrenderPet(player, petId);
                break;
            case 3:
                // spawn
                ToyPetService.getInstance().summonPet(player, petId);
                break;
            case 4:
                // dismiss
                ToyPetService.getInstance().dismissPet(player, petId);
                break;
            case 10:
                // rename
                ToyPetService.getInstance().renamePet(player, petId, petName);
                break;
            default:
                break;
        }
    }
}
