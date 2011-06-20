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

import gameserver.configs.main.CustomConfig;
import gameserver.configs.main.GSConfig;
import gameserver.model.ChatType;
import gameserver.model.gameobjects.player.FriendList;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.restrictions.RestrictionsManager;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.world.World;
import org.apache.log4j.Logger;

/**
 * Packet that reads Whisper chat messages.<br>
 *
 * @author SoulKeeper
 */
public class CM_CHAT_MESSAGE_WHISPER extends AionClientPacket {
    /**
     * Logger
     */
    private static final Logger log = Logger.getLogger(CM_CHAT_MESSAGE_WHISPER.class);

    /**
     * To whom this message is sent
     */
    private String name;

    /**
     * Message text
     */
    private String message;

    /**
     * Constructs new client packet instance.
     *
     * @param opcode
     */
    public CM_CHAT_MESSAGE_WHISPER(int opcode) {
        super(opcode);

    }

    /**
     * Read message
     */
    @Override
    protected void readImpl() {
        name = readS();
        message = readS();
    }

    /**
     * Print debug info
     */
    @Override
    protected void runImpl() {
        if (CustomConfig.GMTAG_DISPLAY) {
            name = name.replaceAll(CustomConfig.GM_LEVEL1, "");
            name = name.replaceAll(CustomConfig.GM_LEVEL2, "");
            name = name.replaceAll(CustomConfig.GM_LEVEL3, "");
            name = name.replaceAll(CustomConfig.GM_LEVEL4, "");
            name = name.replaceAll(CustomConfig.GM_LEVEL5, "");
        }
        
        String formatname = Util.convertName(name);

        Player sender = getConnection().getActivePlayer();
        Player receiver = World.getInstance().findPlayer(formatname);

        if (GSConfig.LOG_CHAT)
            log.info(String.format("[MESSAGE] [%s] Whisper To: %s, Message: %s", sender.getName(), formatname, message));

        if (receiver == null || receiver.getFriendList().getStatus() == FriendList.Status.OFFLINE) {
            sendPacket(SM_SYSTEM_MESSAGE.PLAYER_IS_OFFLINE(formatname));
        } else if (sender.getLevel() < CustomConfig.LEVEL_TO_WHISPER) {
            sendPacket(SM_SYSTEM_MESSAGE.LEVEL_NOT_ENOUGH_FOR_WHISPER(String.valueOf(CustomConfig.LEVEL_TO_WHISPER)));
        } else if (receiver.getBlockList().contains(sender.getObjectId())) {
            sendPacket(SM_SYSTEM_MESSAGE.YOU_ARE_BLOCKED_BY(receiver.getName()));
        } else if (!sender.isGM() && !receiver.isWhisperable()) {
            PacketSendUtility.sendMessage(sender, receiver.getName() + " is on whisper refusal mode now, sorry.");
        } else {
            if (RestrictionsManager.canChat(sender))
                PacketSendUtility.sendPacket(receiver, new SM_MESSAGE(sender, message, ChatType.WHISPER));
		}
	}
}
