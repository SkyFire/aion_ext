package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.DredgionInstanceService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/*
 *
 * @author ArkShadow
 * @edited by Kamui
 *
*/

public class Dredgion extends AdminCommand {
    public Dredgion() {
        super("dredgion");
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.utils.chathandlers.AdminCommand#executeCommand(com.aionemu.gameserver.model.gameobjects.player.Player, java.lang.String[])
      */
    @Override
    public void executeCommand(Player admin, String[] params) {
        // TODO Auto-generated method stub

        if (admin.getAccessLevel() < AdminConfig.COMMAND_DREDGION) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
            return;
        }
        if (params.length == 0) {
            PacketSendUtility.sendMessage(admin, "Syntax : //dredgion <register | unregister>");
            PacketSendUtility.sendMessage(admin, "Syntax : //dredgion <start | reset | time | autorun>");
            return;
        }
        if (params[0].equals("register")) {
            DredgionInstanceService.getInstance().registerPlayer(admin);
            return;
        }
        if (params[0].equals("unregister")) {
            DredgionInstanceService.getInstance().unregisterPlayer(admin);
            return;
        } else if (params[0].equals("start")) {
            if (DredgionInstanceService.getInstance().loadDredgion()) {
                PacketSendUtility.sendMessage(admin, "Dredgion successfully started");
            } else {
                PacketSendUtility.sendMessage(admin, "An error has occured");
            }
        } else if (params[0].equals("reset")) {
            DredgionInstanceService.getInstance().reset();
            PacketSendUtility.sendMessage(admin, "Dredgion successfully resetted");
        } else if (params[0].equals("time")) {
            try {
                int time = Integer.parseInt(params[1]);
                DredgionInstanceService.getInstance().setTimer(time);
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "Time must be an integer (in minutes)");
            }
        } else if (params[0].equals("autorun")) {
            if (params.length == 1) {
                PacketSendUtility.sendMessage(admin, "Syntax : //dredgion autorun <on | off>");
            } else if (params[1].equals("on")) {
                DredgionInstanceService.getInstance().setAuto(true);
                PacketSendUtility.sendMessage(admin, "Dredgion is now set to autostart");
            } else if (params[1].equals("off")) {
                DredgionInstanceService.getInstance().setAuto(false);
                PacketSendUtility.sendMessage(admin, "Dredgion is now set to manual");
            } else {
                PacketSendUtility.sendMessage(admin, "Synthax : //dredgion autorun <on | off>");
            }
        }
    }
}
