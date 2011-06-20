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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author ATracer
 */

public class Speed extends AdminCommand {
    public Speed() {
        super("speed");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_SPEED) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        if (params == null || params.length < 1) {
            PacketSendUtility.sendMessage(admin, "Syntax //speed <0~" + AdminConfig.COMMAND_SPEED_MAXVALUE + ">");
            return;
        }

        int parameter = 0;
        try {
            parameter = Integer.parseInt(params[0]);
        }
        catch (NumberFormatException e) {
            PacketSendUtility.sendMessage(admin, "Parameter should be a number");
            return;
        }

        if (parameter < 0 || parameter > AdminConfig.COMMAND_SPEED_MAXVALUE) {
            PacketSendUtility.sendMessage(admin, "Valid values are in 0~" + AdminConfig.COMMAND_SPEED_MAXVALUE + " range");
            return;
        }

        int speed = 6000;
        int flyspeed = 9000;

        admin.getGameStats().setStat(StatEnum.SPEED, (speed + (speed * parameter) / 100));
        admin.getGameStats().setStat(StatEnum.FLY_SPEED, (flyspeed + (flyspeed * parameter) / 100));
        PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
    }
}
