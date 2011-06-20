/*
*This file is part of Aion X Emu <aionxemu.com>
*
*  aion-emu is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  (at your option) any later version.
*
*  aion-emu is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
*/
package usercommands;

import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.drop.DropTemplate;
import gameserver.services.DropService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;

/**
 * @author AionChs, Rajin
 */

public class Droplist extends UserCommand
{
    public Droplist()
    {
        super("droplist");
    }

    @Override
    public void executeCommand(Player player, String params)
    {
        VisibleObject target = player.getTarget();
        int npcId = -1;

        if(target == null)
        {
            PacketSendUtility.sendMessage(player, "Please select a Target!");
            return;
        }
        else
        {
            if(target instanceof Npc)
            {
                Npc npc = (Npc) player.getTarget();
                npcId = npc.getNpcId();
            }
            else
            {
                PacketSendUtility.sendMessage(player, "Target must be an NPC");
                return;
            }
        }

        if(npcId == -1)
            return;

        String drops = "Drops: \n-------------------------\n";
        try
        {
            for (DropTemplate dropTemplate : DropService.getInstance().getDropList().getDropsFor(npcId))
            {
                drops += "[item:" + dropTemplate.getItemId() + "] " + dropTemplate.getChance() + "%\n";
            }
        }
        catch(Exception e)
        {
            drops += "NO DROPS!";
        }
        PacketSendUtility.sendMessage(player, drops);

    }
}
