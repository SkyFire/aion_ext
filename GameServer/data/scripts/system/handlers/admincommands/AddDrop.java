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

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import gameserver.configs.administration.AdminConfig;
import gameserver.model.drop.DropList;
import gameserver.model.drop.DropTemplate;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.DropService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author ATracer
 */
public class AddDrop extends AdminCommand {

    public AddDrop() {
        super("adddrop");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_ADDDROP) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }

        if (params.length != 5) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDDROP_SYNTAX));
            return;
        }

        try {
            final int mobId = Integer.parseInt(params[0]);
            final int itemId = Integer.parseInt(params[1]);
            final int min = Integer.parseInt(params[2]);
            final int max = Integer.parseInt(params[3]);
            final int chance = Integer.parseInt(params[4]);

            DropList dropList = DropService.getInstance().getDropList();

            DropTemplate dropTemplate = new DropTemplate(mobId, itemId, min, max, chance);
            dropList.addDropTemplate(mobId, dropTemplate);

            DB.insertUpdate("INSERT INTO droplist ("
                    + "`mobId`, `itemId`, `min`, `max`, `chance`)" + " VALUES "
                    + "(?, ?, ?, ?, ?)", new IUStH() {
                @Override
                public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
                    ps.setInt(1, mobId);
                    ps.setInt(2, itemId);
                    ps.setInt(3, min);
                    ps.setInt(4, max);
                    ps.setInt(5, chance);
                    ps.execute();
                }
            });
        }
        catch (Exception ex) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.INTEGER_PARAMETERS_ONLY));
            return;
        }
    }
}
