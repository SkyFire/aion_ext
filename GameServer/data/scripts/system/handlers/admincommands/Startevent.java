/*
 *  This file is part of Aion X EMU <aionxemu.com>.
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.Race;
import gameserver.network.aion.serverpackets.SM_PLAYER_SPAWN;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

/**
 * @author Alfa
 * 
 */
public class Startevent extends AdminCommand
{
    /**
    * Constructor.
    */
    public Startevent()
    {
        super("startevent");
    }

    @Override
    public void executeCommand(Player admin, String[] params)
    {
        if(admin.getAccessLevel() < AdminConfig.COMMAND_STARTEVENT) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }

        if(params.length < 3) {
            PacketSendUtility.sendMessage(admin, "syntax //startevent <Check isLookingforevent 0:False|1:True> <0:Elyos|1:Asmos|3:All> <0:Start|1:End>");
            return;
        }

        int playerscount = 0;
        World world = admin.getActiveRegion().getWorld();
        switch (Integer.parseInt(params[1])) {
             // All Players
            case 3:
                for(final Player p : World.getInstance().getPlayers()) {
                    if(p.equals(admin))
                        continue;

                    if(Integer.parseInt(params[2]) == 0) {
                        TeleportService.teleportTo(p,admin.getWorldId(),admin.getInstanceId(),admin.getX(), admin.getY(), admin.getZ(), admin.getHeading(),5);
                        playerscount++;
                        PacketSendUtility.sendPacket(p, new SM_PLAYER_SPAWN(p));
                        PacketSendUtility.sendMessage(p, "Teleported for the event by " + admin.getName() + ".");
                    }
                    else {
                        if (p.isLookingForEvent() &&
                            p.getCommonData().getRace().getRaceId() == Integer.parseInt(params[1]))
                        {
                            TeleportService.moveToBindLocation(p, true);
                            playerscount++;
                            PacketSendUtility.sendMessage(p, "Teleported to bind point by " + admin.getName() + ".");
                            p.setLookingForEvent(false);
                            PacketSendUtility.sendMessage(p, "You are no longer waiting for event.");
                        }
                    }
                }
            //Specified Faction
            default:
            switch (Integer.parseInt(params[0])) {
                //Not check boolean
                case 0:
                    for(final Player p : World.getInstance().getPlayers()) {
                        if(p.equals(admin))
                            continue;

                        if(Integer.parseInt(params[2]) == 0) {
                            if(p.getCommonData().getRace().getRaceId() == Integer.parseInt(params[1])) {
                                TeleportService.teleportTo(p,admin.getWorldId(),admin.getInstanceId(),admin.getX(), admin.getY(), admin.getZ(), admin.getHeading(),5);
                                playerscount++;
                                PacketSendUtility.sendPacket(p, new SM_PLAYER_SPAWN(p));
                                PacketSendUtility.sendMessage(p, "Teleported for the event by " + admin.getName() + ".");
                            }
                        }
                        else {
                            if (p.isLookingForEvent() &&
                                p.getCommonData().getRace().getRaceId() == Integer.parseInt(params[1]))
                            {
                                TeleportService.moveToBindLocation(p, true);
                                playerscount++;
                                PacketSendUtility.sendMessage(p, "Teleported to bind point by " + admin.getName() + ".");
                                p.setLookingForEvent(false);
                                PacketSendUtility.sendMessage(p, "You are no longer waiting for event.");
                            }
                        }
                    }
                //Check Boolean	
                case 1:
                for(final Player p : World.getInstance().getPlayers()) {
                    if(p.equals(admin))
                        continue;

                    if(Integer.parseInt(params[2]) == 0) {
                        if (p.isLookingForEvent() &&
                            p.getCommonData().getRace().getRaceId() == Integer.parseInt(params[1]))
                        {
                            TeleportService.teleportTo(p,admin.getWorldId(),admin.getInstanceId(),admin.getX(), admin.getY(), admin.getZ(), admin.getHeading(),5);
                            playerscount++;
                            PacketSendUtility.sendPacket(p, new SM_PLAYER_SPAWN(p));                        
                            PacketSendUtility.sendMessage(p, "Teleported for the event by " + admin.getName() + ".");
                        }
                    }
                    else {
                        if (p.isLookingForEvent() &&
                            p.getCommonData().getRace().getRaceId() == Integer.parseInt(params[0]))
                        {
                            TeleportService.moveToBindLocation(p, true);
                            playerscount++;
                            PacketSendUtility.sendMessage(p, "Teleported to bind point by " + admin.getName() + ".");
                            p.setLookingForEvent(false);
                            PacketSendUtility.sendMessage(p, "You are no longer waiting for event.");
                        }
                    }
                }
            }
        }
        PacketSendUtility.sendMessage(admin, playerscount + " players teleported.");
    }
}
