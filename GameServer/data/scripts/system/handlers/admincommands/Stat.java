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
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.CreatureGameStats;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class Stat extends AdminCommand {
    public Stat() {
        super("status");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_STAT) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        if (admin.getTarget() == null) {
            PacketSendUtility.sendMessage(admin, "You have to select a target");
            return;
        }

        VisibleObject target = admin.getTarget();

        if (!(target instanceof Creature)) {
            PacketSendUtility.sendMessage(admin, "Your target is not a Creature");
            return;
        }

        Creature cTarget = (Creature) target;

        PacketSendUtility.sendMessage(admin, ">>> Stats information about " + cTarget.getClass().getSimpleName() + " \"" + cTarget.getName() + "\"");
        if (cTarget.getGameStats() != null) {
            CreatureGameStats<?> cgs = cTarget.getGameStats();
            for (int i = 0; i < StatEnum.values().length; i++) {
                if (cgs.getCurrentStat(StatEnum.values()[i]) != 0) {
                    PacketSendUtility.sendMessage(admin, StatEnum.values()[i] + ": " + cgs.getBaseStat(StatEnum.values()[i]) + " (" + cgs.getStatBonus(StatEnum.values()[i]) + ")");
                }
            }
        }
        PacketSendUtility.sendMessage(admin, ">>> End of stats information");
    }
}
