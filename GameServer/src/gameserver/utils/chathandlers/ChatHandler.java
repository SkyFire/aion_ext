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

package gameserver.utils.chathandlers;

import gameserver.model.ChatType;
import gameserver.model.gameobjects.player.Player;

/**
 * ChatHandler is called every time when player is trying to send a message using chat. ChatHandler can decide whether
 * message should be send later to players (i.e. admin command handler will block it) and can also change the content of
 * the message ( for example censor may put *** in place of vulgar words)
 *
 * @author Luno
 */
public interface ChatHandler {
    /**
     * This method may check content of message and take proper actions based on it. The message can be changed and also
     * blocked to forwarding to players.
     *
     * @param chatType
     * @param message
     * @param sender
     * @return response {@link ChatHandlerResponse}
     */
    public ChatHandlerResponse handleChatMessage(ChatType chatType, String message, Player sender);
}
