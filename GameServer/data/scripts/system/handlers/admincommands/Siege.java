/**
 *  This file is part of Aion X Emu <aionxemu.com>
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
import gameserver.configs.main.SiegeConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.siege.Artifact;
import gameserver.model.siege.Commander;
import gameserver.model.siege.SiegeLocation;
import gameserver.model.siege.SiegeRace;
import gameserver.services.SiegeService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

/**
 * @author Sarynth, Dallas
 * 
 * Command Syntax
 * //siege capture <location id> <race id> [legion id]
 * //siege set <location id> <current state> [next state] 
 * [not implemented] //siege timer <reset|set [time]>
 * //siege help
 *
 */

public class Siege extends AdminCommand
{
    static final String command = "siege";
    static final String commandHelp = "siege help";
    static final String commandHelpSet = commandHelp + " set";
    static final String commandHelpCapture = commandHelp + " capture";

    /**
     * Constructor
     */
    public Siege()
    {
        super(command);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void executeCommand(Player admin, String[] params)
    {
        if (SiegeConfig.SIEGE_ENABLED == false) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_DISABLED));
            return;
        }

        if (admin.getAccessLevel() < AdminConfig.COMMAND_SIEGE) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }

        if (params == null || params.length == 0) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_ADMIN_NO_PARAMETERS, commandHelp));
            return;
        }

        // Determine Command
        try {
            String cmd = params[0].toLowerCase();
            if (("help").startsWith(cmd)) {
                sendHelp(admin, params);
            }
            else if (("capture").startsWith(cmd)) {
                processCapture(admin, params);
            }
            else if (("set").startsWith(cmd)) {
                processSet(admin, params);
            }
            else if (("timer").startsWith(cmd)) {
                processTimer(admin, params);
            }
            else {
                PacketSendUtility.sendMessage(admin,
                    LanguageHandler.translate(CustomMessageId.COMMAND_ADMIN_SUBCOMMAND_NOT_EXIST, commandHelp));
            }
        }
        catch (Exception e) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_ADMIN_ERROR_REQUEST, commandHelp));
        }
    }

    /**
     * @param admin
     * @param params
     */
    private void processTimer(Player admin, String[] params)
    {
        // TODO Auto-generated method stub
    }

    /**
     * @param admin
     * @param params
     */
    private void processSet(Player admin, String[] params) {
        if (params.length < 3 || params.length > 4) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_ADMIN_INCORRECT_PARAMETERS, commandHelpSet));
            return;
        }

        // Try to get Location Id
        int locationId;
        try {
            locationId = Integer.parseInt(params[1]);
        }
        catch (NumberFormatException e) {
            // TODO: Enable set state by partial fortress location names. 
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_PARAM_REQUIRED_INT, "Location ID"));
            return;
        }

        // Try to get Current State
        int currentState;
        try {
            currentState = Integer.parseInt(params[2]);
        }
        catch (NumberFormatException e) {
            String cmd = params[2].toLowerCase();
            if (("invulnerable").startsWith(cmd)) {
                currentState = 0;
            }
            else if (("vulnerable").startsWith(cmd)) {
                currentState = 2;
            }
            else {
                PacketSendUtility.sendMessage(admin,
                    LanguageHandler.translate(CustomMessageId.COMMAND_PARAM_REQUIRED_INT, "Current State"));
                return;
            }
        }

        // Try to get next state
        int nextState = -1;
        if (params.length == 4) {
            try {
                nextState = Integer.parseInt(params[3]);
            }
            catch (NumberFormatException e) {
                String cmd = params[3].toLowerCase();
                if (("invulnerable").startsWith(cmd)) {
                    nextState = 0;
                }
                else if (("vulnerable").startsWith(cmd)) {
                    nextState = 1;
                }
                else {
                    PacketSendUtility.sendMessage(admin,
                        LanguageHandler.translate(CustomMessageId.COMMAND_PARAM_REQUIRED_INT, "Next State"));
                    return;
                }
            }
        }

        if (currentState != 0 && currentState != 2) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_PARAM_INCORRECT_VALUE, "current state", commandHelpSet));
            return;
        }

        if (params.length == 4 && nextState != 0 && nextState != 1) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_PARAM_INCORRECT_VALUE, "next state", commandHelpSet));
            return;
        }

        SiegeLocation sLoc = SiegeService.getInstance().getSiegeLocation(locationId);

        if (sLoc == null) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.ERROR_LOCATION_NOT_EXIST, locationId));
            return;         
        }

        PacketSendUtility.sendMessage(admin, "[Admin Set State]\n - Location ID: " + locationId +
            "\n - New Current State: " + (currentState == 2 ? "Vulnerable" : "Invulnerable") +
            (params.length == 4 ? "\n - New Next State: " + (nextState == 1 ? "Vulnerable" : "Invulnerable") : "") +
            "\n");

        if (sLoc.isVulnerable() != (currentState == 2)) {
            sLoc.setVulnerable(currentState == 2);
        }

        if (params.length == 4 && sLoc.getNextState() != nextState) {
            sLoc.setNextState(nextState);
        }

        SiegeService.getInstance().broadcastUpdate(sLoc);
    }

    /**
     * //siege capture <location id> <race id> [legion id]
     * @param admin
     * @param params
     */
    private void processCapture(Player admin, String[] params)
    {
        if (params.length < 3 || params.length > 4) {
            PacketSendUtility.sendMessage(admin, 
                LanguageHandler.translate(CustomMessageId.COMMAND_ADMIN_INCORRECT_PARAMETERS, commandHelp));
            return;
        }

        // Try to get Location Id
        int locationId;
        try {
            locationId = Integer.parseInt(params[1]);
        }
        catch (NumberFormatException e) {
            // TODO: Enable capture by partial fortress location names. 
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_PARAM_REQUIRED_INT, "Location ID"));
            return;
        }

        // Try to get capturing race
        SiegeRace race;
        String raceName = params[2].toLowerCase();
        if (("elyos").startsWith(raceName)) {
            race = SiegeRace.ELYOS;
        }
        else if (("asmos").startsWith(raceName)) {
            race = SiegeRace.ASMODIANS;
        }
        else if (("balaur").startsWith(raceName)) {
            race = SiegeRace.BALAUR;
        }
        else {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_SIEGE_RACE_RESTRICTION));
            return;
        }

        // Try to get legion id
        int legionId = 0;
        if (params.length == 4) {
            try {
                legionId = Integer.parseInt(params[3]);
            }
            catch (NumberFormatException e) {
                // TODO: Enable capture by legion name as a string. 
                PacketSendUtility.sendMessage(admin,
                    LanguageHandler.translate(CustomMessageId.COMMAND_PARAM_REQUIRED_INT, "Legion ID"));
                return;
            }
        }

        SiegeLocation sLoc = SiegeService.getInstance().getSiegeLocation(locationId);

        if (sLoc == null) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.ERROR_LOCATION_NOT_EXIST, locationId));
            return;         
        }        
        PacketSendUtility.sendMessage(admin, "[Admin Capture]\n - Location ID: " + locationId +
            "\n - Race: " + race.toString() + "\n - Legion ID: " + legionId + "\n");
        SiegeService.getInstance().capture(locationId, race, legionId);
    }

    /**
     * @param admin
     * @param params
     */
    private void sendHelp(Player admin, String[] params)
    {
        if (params.length != 2)
            sendHelpGeneral(admin);

        String cmd = params[1].toLowerCase();
        if (("capture").startsWith(cmd)) {
            sendHelpCapture(admin);
            return;
        }
        else if (("set").startsWith(cmd)) {
            sendHelpSet(admin);
            return;
        }
        else if (("timer").startsWith(cmd)) {
            sendHelpTimer(admin, params);
            return;
        }

    }

    /**
     * @param admin
     * @param params
     */
    private void sendHelpList(Player admin, String[] params)
    {
        PacketSendUtility.sendMessage(admin,
            LanguageHandler.translate(CustomMessageId.COMMAND_SIEGE_HELP_LIST));
    }

    /**
     * @param admin
     */
    private void sendHelpGeneral(Player admin)
    {
        PacketSendUtility.sendMessage(admin,
            LanguageHandler.translate(CustomMessageId.COMMAND_SIEGE_HELP_GENERAL));
    }

    /**
     * @param admin
     * @param params
     */
    private void sendHelpTimer(Player admin, String[] params)
    {
        PacketSendUtility.sendMessage(admin,
            LanguageHandler.translate(CustomMessageId.COMMAND_SIEGE_HELP_TIMER));
    }

    /**
     * @param admin
     */
    private void sendHelpSet(Player admin)
    {
        PacketSendUtility.sendMessage(admin,
            LanguageHandler.translate(CustomMessageId.COMMAND_SIEGE_HELP_SET));
    }

    /**
     * @param admin
     */
    private void sendHelpCapture(Player admin)
    {
        PacketSendUtility.sendMessage(admin,
            LanguageHandler.translate(CustomMessageId.COMMAND_SIEGE_HELP_CAPTURE));
    }
}
