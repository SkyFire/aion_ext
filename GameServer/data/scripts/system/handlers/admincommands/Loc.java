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

import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/**
 * Admin /loc [replacement] command.
 *
 * @author Untamed AKA TimeBomb
 */

public class Loc extends AdminCommand {

    /**
     * Constructor.
     */

    public Loc() {
        super("loc");
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < 2) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        float x = admin.getPosition().getX();
        float y = admin.getPosition().getY();
        float z = admin.getPosition().getZ();
        float heading = admin.getPosition().getHeading();
        int mapid = admin.getPosition().getMapId();
        
        String message = "mapid=" + mapid + "; x=" + x + "; y=" + y + "; z=" + z + "; heading=" + heading ;
        PacketSendUtility.sendMessage(admin, message);
    }
}
