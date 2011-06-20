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
package gameserver.utils;

import gameserver.configs.main.CustomConfig;
import com.aionemu.commons.objects.filter.ObjectFilter;
import gameserver.model.ChatType;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.legion.Legion;
import gameserver.network.aion.AionServerPacket;
import gameserver.network.aion.AionServerPacketSeq;
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.utils.Util;
import gameserver.world.Executor;

/**
 * This class contains static methods, which are utility methods, all of them are interacting only with objects passed
 * as parameters.<br>
 * These methods could be placed directly into Player class, but we want to keep Player class as a pure data holder.<br>
 *
 * @author Luno
 */
public class PacketSendUtility {
    /**
     * Sends message to player (used for system messages)
     *
     * @param player
     * @param msg
     */
    public static void sendMessage(Player player, String msg) {
        String[] msgPiece = Util.splitStringFixedLen(msg, CustomConfig.MESSAGE_LENGTH);
        for (int i = 0; i < msgPiece.length; ++i) {
            sendPacket(player, new SM_MESSAGE(0, null, msgPiece[i], ChatType.ANNOUNCEMENTS));
        }
    }

    /**
     * Sends message to player (used for system notices)
     *
     * @param player
     * @param msg
     */
    public static void sendSysMessage(Player player, String msg) {
        String[] msgPiece = Util.splitStringFixedLen(msg, CustomConfig.MESSAGE_LENGTH);
        for (int i = 0; i < msgPiece.length; ++i) {
            sendPacket(player, new SM_MESSAGE(0, null, msgPiece[i], ChatType.SYSTEM_NOTICE));
        }
    }

    /**
     * Send packet to this player.
     *
     * @param player
     * @param packet
     */
    public static void sendPacket(Player player, AionServerPacket packet) {
        if (player != null && player.getClientConnection() != null)
            player.getClientConnection().sendPacket(packet);
    }

    /**
     * Send packet seq to this player.
     * 
     * @param player
     * @param packetSeq
     */
     public static void sendPacketSeq(Player player, AionServerPacketSeq packetSeq) {
        if (player.getClientConnection() != null)
            player.getClientConnection().sendPacketSeq(packetSeq);
     }

    /**
     * Broadcast packet to all visible players.
     *
     * @param player
     * @param packet ServerPacket that will be broadcast
     * @param toSelf true if packet should also be sent to this player
     */
    public static void broadcastPacket(Player player, AionServerPacket packet, boolean toSelf) {
        if (toSelf)
            sendPacket(player, packet);

        broadcastPacket(player, packet);
    }

    /**
     * Broadcast packet to all visible players.
     *
     * @param visibleObject
     * @param packet
     */
    public static void broadcastPacketAndReceive(VisibleObject visibleObject, AionServerPacket packet) {
        if (visibleObject instanceof Player)
            sendPacket((Player) visibleObject, packet);

        broadcastPacket(visibleObject, packet);
    }

    /**
     * Broadcast packet to all Players from knownList of the given visible object.
     *
     * @param visibleObject
     * @param packet
     */
    public static void broadcastPacket(VisibleObject visibleObject, final AionServerPacket packet) {
        visibleObject.getKnownList().doOnAllPlayers(new Executor<Player>() {
            @Override
            public boolean run(Player obj) {
                sendPacket(obj, packet);
                return true;
            }
        }, true);
    }

    /**
     * Broadcasts packet to all visible players matching a filter
     *
     * @param player
     * @param packet ServerPacket to be broadcast
     * @param toSelf true if packet should also be sent to this player
     * @param filter filter determining who should be messaged
     */
    public static void broadcastPacket(Player player, final AionServerPacket packet, boolean toSelf, final ObjectFilter<Player> filter) {
        if (toSelf) {
            sendPacket(player, packet);
        }

        player.getKnownList().doOnAllPlayers(new Executor<Player>() {
            @Override
            public boolean run(Player target) {
                if (filter.acceptObject(target))
                    sendPacket(target, packet);
                return true;
            }
        }, true);
    }

    /**
     * Broadcasts packet to all legion members of a legion
     *
     * @param legion Legion to broadcast packet to
     * @param packet ServerPacket to be broadcast
     */
    public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet) {
        for (Player onlineLegionMember : legion.getOnlineLegionMembers()) {
            sendPacket(onlineLegionMember, packet);
        }
    }

    public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet, int playerObjId) {
        for (Player onlineLegionMember : legion.getOnlineLegionMembers())
		{
			if(onlineLegionMember.getObjectId() != playerObjId)
				sendPacket(onlineLegionMember, packet);
		}
	}
}
