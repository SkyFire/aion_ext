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

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.dao.LegionDAO;
import gameserver.model.gameobjects.player.Friend;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_LEGION_UPDATE_MEMBER;
import gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.LegionService;
import gameserver.services.PlayerService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.Executor;

import java.util.Iterator;

/**
 * @author xitanium
 */
public class CM_NAME_CHANGE extends AionClientPacket {
    private int action;
    private int itemId;
    private String newName;

    public CM_NAME_CHANGE(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        action = readC(); // 0: Change Char Name / 1: Change Legion Name 
        readC();
        readH();
        itemId = readD();
        newName = readS();
    }

    @Override
    protected void runImpl() {
        final Player player = getConnection().getActivePlayer();
        switch (action) {
            case 0:
                // Change Player Name
                if (!PlayerService.isValidName(newName)) {
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400151));
                    return;
                }
                if (player.getName().equals(newName)) {
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400153));
                    return;
                }
                if (!PlayerService.isFreeName(newName)) {
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400155));
                    return;
                }
                player.getCommonData().setName(newName);
                PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
                Iterator<Friend> knownFriends = player.getFriendList().iterator();
                player.getKnownList().doOnAllPlayers(new Executor<Player>() {
                    @Override
                    public boolean run(Player p) {
                        PacketSendUtility.sendPacket(p, new SM_PLAYER_INFO(player, player.isEnemyPlayer(p)));
                        return true;
                    }
                }, true);

                while (knownFriends.hasNext()) {
                    Friend nextObject = knownFriends.next();
                    if (nextObject.getPlayer() != null) {
                        if (nextObject.getPlayer().isOnline())
                            PacketSendUtility.sendPacket(nextObject.getPlayer(), new SM_PLAYER_INFO(player, false));
                    }
                }
                if (player.isLegionMember()) {
                    PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_UPDATE_MEMBER(player, 0, ""));
                }
                player.getInventory().removeFromBagByObjectId(itemId, 1);
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400157, newName));
                break;
            case 1:
                // Change Legion Name
                if (!player.isLegionMember())
                    return;
                if (!LegionService.getInstance().isValidName(newName)) {
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400152));
                    return;
                }
                if (player.getLegion().getLegionName().equals(newName)) {
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400154));
                    return;
                }
                if (DAOManager.getDAO(LegionDAO.class).isNameUsed(newName)) {
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400156));
                    return;
                }
                LegionService.getInstance().setLegionName(player.getLegion(), newName, true);
                player.getInventory().removeFromBagByObjectId(itemId, 1);
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400158, newName));
                break;
        }
    }
}
