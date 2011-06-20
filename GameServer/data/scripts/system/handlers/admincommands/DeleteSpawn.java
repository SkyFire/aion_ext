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

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.configs.administration.AdminConfig;
import gameserver.dao.SpawnDAO;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Luno
 */

public class DeleteSpawn extends AdminCommand {

    public DeleteSpawn() {
        super("delete");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_DELETESPAWN) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        VisibleObject cre = admin.getTarget();
        if (!(cre instanceof Npc)) {
            PacketSendUtility.sendMessage(admin, "Wrong target");
            return;
        }
        Npc npc = (Npc) cre;
        SpawnTemplate template = npc.getSpawn();
        int spawnId = DAOManager.getDAO(SpawnDAO.class).isInDB(template.getSpawnGroup().getNpcid(), template.getX(), template.getY());
        DAOManager.getDAO(SpawnDAO.class).deleteSpawn(spawnId);
        DataManager.SPAWNS_DATA.removeSpawn(npc.getSpawn());
        npc.getController().delete();
        PacketSendUtility.sendMessage(admin, "Spawn removed");
    }
}
