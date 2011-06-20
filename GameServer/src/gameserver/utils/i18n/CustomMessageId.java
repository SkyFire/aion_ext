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
package gameserver.utils.i18n;

/**
 * @author xavier
 */
public enum CustomMessageId {
    WELCOME_VIP("Welcome VIP Member to %s server.\nCopyright 2011 www.aionxemu.com..\nSERVER RATES:\nExp Rate: %d\nQuest Rate: %d\nDrop Rate: %d\n"),
    WELCOME_PREMIUM("Welcome Premium Member to %s server.\nCopyright 2010 www.aionxemu.com..\nSERVER RATES:\nExp Rate: %d\nQuest Rate: %d\nDrop Rate: %d\n"),
    WELCOME_REGULAR("Welcome to %s server.\nCopyright 2011 www.aionxemu.com. .\nSERVER RATES:\nExp Rate: %d\nQuest Rate: %d\nDrop Rate: %d\n"),
    SERVER_REVISION("Server Revision: %-6s"),
    WELCOME_BASIC("Welcome to %s server.\nCopyright 2011 www.aionxemu.com."),
    ANNOUNCE_GM_CONNECTION("%s just entered into Atreia."),
    COMMAND_DISABLED("This command is disabled"),
    COMMAND_NOT_ENOUGH_RIGHTS("You dont have enough rights to execute this command"),
    COMMAND_ADMIN_NO_PARAMETERS("No parameters detected.\nPlease use //%s"),
    COMMAND_ADMIN_INCORRECT_PARAMETERS("Incorrect parameters detected.\nPlease use //%s"),
    COMMAND_ADMIN_SUBCOMMAND_NOT_EXIST("Sub Command does not exist.\nPlease use //%s"),
    COMMAND_ADMIN_ERROR_REQUEST("Error with your request.\nPlease use //%s"),
    COMMAND_PARAM_REQUIRED_INT("%s Parameter needs to be an integer"),
    COMMAND_PARAM_INCORRECT_VALUE("Incorrect %s value.\nPlease use //%s"),

    PLAYER_NOT_ONLINE("The player %s is not online"),
    INTEGER_PARAMETER_REQUIRED("Parameter needs to be an integer"),
    INTEGER_PARAMETERS_ONLY("Parameters need to be only integers"),
    SOMETHING_WRONG_HAPPENED("Something wrong happened"),

    COMMAND_ADMIN_PROMOTE_SYNTAX("promote <characterName> <rolemask>"),
    COMMAND_ADMIN_REVOKE_SYNTAX("revoke <characterName>"),
    COMMAND_ADMIN_ADD_SYNTAX("add <itemID> <quantity>"),
    COMMAND_ADMIN_ADDSKILL_SYNTAX("addskill <skillId> <skillLevel>"),
    COMMAND_ADMIN_DELSKILL_SYNTAX("delskill <skillId>"),
    COMMAND_ADMIN_GIVEMISSINGSKILLS_SYNTAX("givemissingskills"),
    COMMAND_ADMIN_ADDTITLE_SYNTAX("addtitle <title_id> <playerName>"),
    COMMAND_ADMIN_ANNOUNCE_SYNTAX("announce <message>"),
    COMMAND_ADMIN_NOTICE_SYNTAX("notice <message>"),
    COMMAND_ADMIN_INFO_SYNTAX("info <target player>"),
    COMMAND_ADMIN_SETLEVEL_SYNTAX("set level <level>"),
    COMMAND_ADMIN_SETEXP_SYNTAX("set exp <value>"),
    COMMAND_ADMIN_SETTITLE_SYNTAX("set title <title_id>"),
    COMMAND_ADMIN_SETCLASS_SYNTAX("set class <class_id>"),
    COMMAND_ADMIN_SPEED_SYNTAX("speed <value>"),
    COMMAND_ADMIN_HEAL_SYNTAX("heal <target>"),
    COMMAND_ADMIN_KILL_SYNTAX("kill <playername>"),
    COMMAND_ADMIN_KICK_SYNTAX("kick <playername>"),
    COMMAND_ADMIN_UNSTUCK_SYNTAX("unstuck"),
    COMMAND_ADMIN_MOVEPLAYERTOPLAYER_SYNTAX("moveplayertoplayer <player1> <player2>"),
    COMMAND_ADMIN_MOVETOME_SYNTAX("movetome <player>"),
    COMMAND_ADMIN_MOVETOPLAYER_SYNTAX("movetoplayer <player>"),
    COMMAND_ADMIN_MOVETO_SYNTAX("moveto < X Y Z>"),
    COMMAND_ADMIN_GOTO_SYNTAX("goto <place name>"),
    COMMAND_ADMIN_SPAWN_SYNTAX("spawn <npc_id>"),
    COMMAND_ADMIN_DELETE_SYNTAX("delete <target npc>"),
    COMMAND_ADMIN_SAVESPAWN_SYNTAX("save_spawn"),
    COMMAND_ADMIN_RELOADSPAWN_SYNTAX("reload_spawn"),
    COMMAND_ADMIN_ADDDROP_SYNTAX("adddrop <mobid> <itemid> <min> <max> <chance> <quest>"),
    COMMAND_ADMIN_ZONE_SYNTAX("zone"),
    COMMAND_ADMIN_RELOADSKILL_SYNTAX("reload skill"),
    COMMAND_ADMIN_RELOADQUEST_SYNTAX("reload quest"),
    COMMAND_ADMIN_QUEST_SYNTAX("quest <start|delete|step|info|vars> <quest_id>"),
    COMMAND_ADMIN_SYS_SYNTAX("sys <info|memory|gc|restart|shutdown>"),
    COMMAND_ADMIN_AI_SYNTAX("ai <info|event|state>"),
    COMMAND_ADMIN_WEATHER_SYNTAX("weather <location> <0 - 8>"),
    COMMAND_ADMIN_LEGION_SYNTAX("legion <disband|setlevel|setname|setpoints>"),
    COMMAND_ADMIN_SPRISON_SYNTAX("sprison <player> <delay>"),
    COMMAND_ADMIN_RPRISON_SYNTAX("rprison <player>"),
    COMMAND_ADMIN_REMOVECD_SYNTAX("removecd <target>"),
    COMMAND_ADMIN_DISPEL_SYNTAX("dispel <all | skillid>"),
    COMMAND_ADMIN_ADDEFFECT_SYNTAX("addeffect <skillid> <duration>"),
    COMMAND_ADMIN_DROPLIST_SYNTAX("droplist <target>"),

    COMMAND_DISPEL_SYNTAX("Syntax: //dispel <all | skillid> \nThis command removes all or a specified effect from the target."),
    COMMAND_DISPEL_EFFECT("Effect %s was dispelled from player %s."),
    COMMAND_DISPEL_EFFECT_ALL("%s had all buff effects dispelled !"),
    COMMAND_WRONG_SKILL_ID("Wrong skill id!"),

    COMMAND_ADDEFFECT_SYNTAX("Syntax: //addeffect <skillid> <duration>\nThis command adds specified effect to the target for a specified duration (in seconds). Leaving duration null will use the default skill duration."),
    COMMAND_ADDEFFECT_SUCCESS("Effect %s added to player %s for %d second(s)."),

    COMMAND_ADD_SYNTAX("Syntax: //add <player name> <item id> <quantity>"),
    COMMAND_ADD_ADMIN_SUCCESS("Item(s) successfully added to player %s"),
    COMMAND_ADD_PLAYER_SUCCESS("Admin %s gave you %d item(s)"),
    COMMAND_ADD_FAILURE("Item %d does not exists and/or cannot be added to %s"),
    COMMAND_ADDDROP_SYNTAX("Syntax: //adddrop <mob id> <item id> <min> <max> <chance>"),
    COMMAND_ADDSET_SYNTAX("Syntax: //addset <player name> <itemset id>"),
    COMMAND_ADDSET_SET_DOES_NOT_EXISTS("Item set with id %d does not exists"),
    COMMAND_ADDSET_NOT_ENOUGH_SLOTS("Inventory needs at least %d free slots"),
    COMMAND_ADDSET_CANNOT_ADD_ITEM("Item %d cannot be added to %s"),
    COMMAND_ADDSET_ADMIN_SUCCESS("Item set %d successfully added to %s"),
    COMMAND_ADDSET_PLAYER_SUCCESS("Admin %s gave you an item set"),
    COMMAND_ADDSKILL_SYNTAX("Syntax: //addskill <skill id> <skill level>"),
    COMMAND_ADDSKILL_ADMIN_SUCCESS("Skill %d was added to player %s with success"),
    COMMAND_ADDSKILL_PLAYER_SUCCESS("%s gave you a new skill"),
    COMMAND_DELSKILL_SYNTAX("Syntax: //delsskill <skill id>"),
    COMMAND_DELSKILL_ADMIN_SUCCESS("Skill %d was deleted from player %s with success"),
    COMMAND_DELSKILL_PLAYER_SUCCESS("%s removed from you Skill %d"),
    COMMAND_ADDTITLE_SYNTAX("Syntax: //addtitle <title id> <player name> [special]"),
    COMMAND_ADDTITLE_TITLE_INVALID("Title %d is invalid, it must be between 1 and 50"),
    COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_ME("You can't add title %d to yourself"),
    COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_PLAYER("You can't add title %d to %s"),
    COMMAND_ADDTITLE_ADMIN_SUCCESS_ME("You added title %d to yourself with success"),
    COMMAND_ADDTITLE_ADMIN_SUCCESS("You added title %d to %s with success"),
    COMMAND_ADDTITLE_PLAYER_SUCCESS("Admin %s gave you title %d"),
    COMMAND_SEND_SYNTAX("Syntax: //send <filename>"),
    COMMAND_SEND_MAPPING_NOT_FOUND("Mapping %s not found"),
    COMMAND_SEND_NO_PACKET("No packet to send"),

    // COMMAND SIEGE
    COMMAND_SIEGE_RACE_RESTRICTION("Race must be: Elyos, Asmos, or Balaur.\nPlease use //siege help capture"),
    COMMAND_SIEGE_HELP_LIST("[Help: Siege List Command]\n" +
        "  The siege list command outputs each siege location." +
        "  Format is: - (fortress|artifact) <location id> (faction owner) [legion id]\n"),
    COMMAND_SIEGE_HELP_GENERAL("[Help: Siege Command]\n" +
        "  Use //siege help <capture|set> for more details on the command.\n" +
        "  Notice: This command uses smart matching. You may abbreviate most commands.\n" +
        "  For example: (//siege cap 1011 ely) will match to (//siege capture 1011 elyos)\n"),
    COMMAND_SIEGE_HELP_TIMER("Timer not implemented."),
    COMMAND_SIEGE_HELP_SET("Syntax: //siege set <location id> <current state> [next state]\n" +
        "Current State Values: 0 - Invulnerable, 2 - Vulnerable\n" +
        "Next State Values: 0 - Invulnerable, 1 - Vulnerable"),
    COMMAND_SIEGE_HELP_CAPTURE("Syntax: //siege capture <location id> <race> [legion id]\n" +
            "Race may be: Elyos, Asmos, Balaur. (Not case sensitive.)"),

    COMMAND_SURVEY_ADD_SUCCESS("The Survey has been successfully added."),
    COMMAND_SURVEY_ADD_FAILURE("There was an error adding the Survey, please check the syntax with //survey add"),
    COMMAND_SURVEY_DEL_SUCCESS("The Survey has been successfully deleted."),
    COMMAND_SURVEY_DEL_FAILURE("There was an error deleting the Survey, please check the syntax with //survey del"),
    COMMAND_SURVEY_EDIT_SUCCESS("The Survey has been successfully edited."),
    COMMAND_SURVEY_EDIT_FAILURE("There was an error editing the Survey. surveyId: %d, columnName: %s, newValue: %s"),
    COMMAND_SURVEY_INFO_SUCCESS("The Survey has been successfully retrieved."),
    COMMAND_SURVEY_INFO_FAILURE("The was an error retrieving the Survey SurveyId: %d."),
    COMMAND_SURVEY_INFO_ALL_SUCCESS("All Surveys has been successfully retrieved."),
    COMMAND_SURVEY_INFO_ALL_FAILURE("The was an error retrieving the all Surveys."),
    COMMAND_SURVEY_RESULT_SUCCESS("The Survey Stats has been successfully retrieved."),
    COMMAND_SURVEY_RESULT_FAILURE("The was an error retrieving the Survey Stats SurveyId: %d."),
    COMMAND_SURVEYOPTION_ADD_SUCCESS("The SurveyOption has been successfully added."),
    COMMAND_SURVEYOPTION_ADD_FAILURE("There was an error adding the SurveyOption, please check the syntax with //survey addoption"),
    COMMAND_SURVEYOPTION_DEL_SUCCESS("The SurveyOption has been successfully deleted."),
    COMMAND_SURVEYOPTION_DEL_FAILURE("There was an error deleting the SurveyOption, please check the syntax with //survey deloption"),
    COMMAND_SURVEYOPTION_EDIT_SUCCESS("The SurveyOption has been successfully edited."),
    COMMAND_SURVEYOPTION_EDIT_FAILURE("There was an error editing the SurveyOption. surveyId: %d, optionId: %d, columnName: %s, newValue: %s"),
    COMMAND_SURVEYPLAYER_ADD_SUCCESS("The Player Survey has been successfully added for Survey \"%s\", Player \"%s\"."),
    COMMAND_SURVEYPLAYER_ADD_FAILURE("There was an error adding the Player Survey, SurveyId: %d, TargetId: %d, TargetName: \"%s\""),
    COMMAND_SURVEYPLAYER_DEL_SUCCESS("The Player Survey has been successfully deleted for Survey \"%s\", Player \"%s\"."),
    COMMAND_SURVEYPLAYER_DEL_FAILURE("There was an error deleting the Player Survey, SurveyId: %d, TargetId: %d, TargetName: \"%s\""),
    COMMAND_SURVEYPLAYER_INFO_SUCCESS("The Player Survey has been successfully retrieved for Survey \"%s\", Player \"%s\"."),
    COMMAND_SURVEYPLAYER_INFO_FAILURE("There was an error retreiving the Player Survey, SurveyId: %d, TargetId: %d, TargetName: \"%s\""),
    COMMAND_SURVEYPLAYER_INFO_NOTTAKEN("The Player Survey has been not been taken for Survey \"%s\", Player \"%s\"."),
    COMMAND_TARGET_PLAYER_NOT_ACQUIRED("Target Player could not be Acquired."),
    CHANNEL_WORLD_DISABLED("The channel %s is disabled, please use channel %s or %s according to your faction"),
    CHANNEL_ALL_DISABLED("The custom channels are disabled"),
    CHANNEL_ALREADY_FIXED("Your channel is already fixed on %s"),
    CHANNEL_FIXED("Your chat is now fixed on %s"),
    CHANNEL_NOT_ALLOWED("You're not allowed to speak on this channel"),
    CHANNEL_FIXED_BOTH("Your chat is now fixed on %s and %s"),
    CHANNEL_UNFIX_HELP("Type %s unfix to release your chat"),
    CHANNEL_NOT_FIXED("Your chat isn't fixed on any channel"),
    CHANNEL_FIXED_OTHER("Your chat is not fixed on this channel, but on %s"),
    CHANNEL_RELEASED("Your chat has been released from %s channel"),
    CHANNEL_RELEASED_BOTH("Your chat has been released from %s and %s channels"),
    CHANNEL_BAN_ENDED("You're not anymore banned from chat channels"),
    CHANNEL_BAN_ENDED_FOR("The player %s is no more banned from chat channels"),
    CHANNEL_NAME_ASMOS("Asmodians"),
    CHANNEL_NAME_ELYOS("Elyos"),
    CHANNEL_NAME_WORLD("World"),
    CHANNEL_NAME_BOTH("Asmodians/Elyos"),
    CHANNEL_COMMAND_ASMOS("asmo"),
    CHANNEL_COMMAND_ELYOS("ely"),
    CHANNEL_COMMAND_WORLD("world"),
    CHANNEL_COMMAND_BOTH("both"),
    CHANNEL_BANNED("You can't use chat channels because %s banned you for following reason: %s, remaining time: %s"),
    COMMAND_MISSING_SKILLS_STIGMAS_ADDED("%d basic skills and %d stigma skills has been added to you"),
    COMMAND_MISSING_SKILLS_ADDED("%d basic skills has been added to you"),
    USER_COMMAND_DOES_NOT_EXIST("This user command does not exist"),
    COMMAND_XP_DISABLED("Your experience gain has been disabled. Type .xpon to re-enable"),
    COMMAND_XP_ALREADY_DISABLED("Your experience gain is already disabled"),
    COMMAND_XP_ENABLED("Your experience gain has been enabled"),
    COMMAND_XP_ALREADY_ENABLED("Your experience gain is already enabled"),
    ERROR_ITEM_COMPRESSED("An error occurred while attempting to open the item, please report it to the administrator"),
    ERROR_COMPRESS_MIN_LEVEL("You must be Level %d in order to Extract the Item."),
    ERROR_INVALID_EXCHANGE("There was an error trying to perform the Trade"),
    ERROR_ITEM_TEMPATE_MISSING("ITEM TEMPLATE NOT FOUND: PlayerId %d, ItemObjectId %d, ItemId %d"),
    ERROR_TRADEITEM_TEMPATE_MISSING("ITEM TEMPLATE NOT FOUND: PlayerId %d, ItemObjectId %d"),
    ERROR_LOCATION_NOT_EXIST("Location does not exist: %s"),
    PLAYER_INVENTORY_FULL("Your Inventory is Full. Free some space and try again.");

    private String fallbackMessage;

    private CustomMessageId(String fallbackMessage) {
        this.fallbackMessage = fallbackMessage;
    }

    public String getFallbackMessage() {
        return fallbackMessage;
    }
}
