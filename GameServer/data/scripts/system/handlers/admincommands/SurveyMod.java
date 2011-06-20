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

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.dao.SurveyDAO;
import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.Survey;
import gameserver.model.gameobjects.SurveyOption;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

import java.util.List;

/**
 * @author ZeroSignal
 */
public class SurveyMod extends AdminCommand {
    public SurveyMod() {
        super("survey");
    }

    @Override
    public void executeCommand(Player user, String[] params) {
        if (user.getAccessLevel() < AdminConfig.COMMAND_SURVEY) {
            PacketSendUtility.sendMessage(user,
                LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }

        String surveyUsage = "Usage: //survey <modify: info | result | add | del | edit | addoption | deloption | editoption | infoplayer | addplayer | delplayer >\n";
        if (params == null || params.length < 1) {
            PacketSendUtility.sendMessage(user, surveyUsage);
            return;
        }

        int userId = user.getObjectId();
        String syntaxMsg;
        if (params[0].equals("info"))
            cmdSurveyInfo(user, params);
        else if (params[0].equals("result"))
            cmdSurveyResult(user, params);
        else if (params[0].equals("add"))
            cmdSurveyAdd(user, params);
        else if (params[0].equals("del"))
            cmdSurveyDelete(user, params);
        else if (params[0].equals("edit"))
            cmdSurveyEdit(user, params);
        else if (params[0].equals("addoption"))
            cmdSurveyAddOption(user, params);
        else if (params[0].equals("deloption"))
            cmdSurveyDeleteOption(user, params);
        else if (params[0].equals("editoption"))
            cmdSurveyEditOption(user, params);
        else if (params[0].equals("infoplayer"))
            cmdSurveyInfoPlayer(user, params);
        else if (params[0].equals("addplayer"))
            cmdSurveyAddPlayer(user, params);
        else if (params[0].equals("delplayer"))
            cmdSurveyDeletePlayer(user, params);
        else {
            PacketSendUtility.sendMessage(user, surveyUsage);
        }
    }

    private void cmdSurveyResult(Player user, String[] params) {
        if (params.length != 2) {
            PacketSendUtility.sendMessage(user, "Usage: //survey result <survey_id>\n");
            return;
        }

        int surveyId = Integer.parseInt(params[1]);
        Survey survey = DAOManager.getDAO(SurveyDAO.class).loadSurveyStat(surveyId);
        if (survey != null)
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEY_RESULT_SUCCESS) + survey.toString());
        else
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEY_RESULT_FAILURE, surveyId));
    }

    private void cmdSurveyInfoPlayer(Player user, String[] params) {
        if (params.length != 2) {
            PacketSendUtility.sendMessage(user, "Usage: //survey infoplayer <survey_id>\n");
            return;
        }

        Player target = getTargetPlayer(user);
        if (target == null)
            return;

        int surveyId = Integer.parseInt(params[1]);
        int optionId = DAOManager.getDAO(SurveyDAO.class).loadPlayerSurvey(surveyId, target.getObjectId());

        if (optionId >= 0) {
            Survey survey = DAOManager.getDAO(SurveyDAO.class).loadSurvey(surveyId, target.getObjectId());
            if (optionId == 0)
                PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYPLAYER_INFO_NOTTAKEN, survey.getTitle(), target.getName()));
            else
                PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYPLAYER_INFO_SUCCESS, survey.getTitle(), target.getName()) + survey.getSurveyOption(optionId-1).toString());
        }
        else
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYPLAYER_INFO_FAILURE, surveyId, target.getObjectId(), target.getName()));            
    }

    private void cmdSurveyAddPlayer(Player user, String[] params) {
        if (params.length != 2) {
            PacketSendUtility.sendMessage(user, "Usage: //survey addplayer <survey_id>\n");
            return;
        }

        Player target = getTargetPlayer(user);
        if (target == null)
            return;

        int surveyId = Integer.parseInt(params[1]);
        Survey survey = DAOManager.getDAO(SurveyDAO.class).loadSurvey(surveyId, user.getObjectId());

        if (survey == null) {
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYPLAYER_ADD_FAILURE, surveyId, target.getObjectId(), target.getName()));
            return;
        }

        boolean result = DAOManager.getDAO(SurveyDAO.class).insertPlayerSurvey(surveyId, target.getObjectId(), 0);
        if (result) {
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYPLAYER_ADD_SUCCESS, survey.getTitle(), target.getName()));
        }
        else
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYPLAYER_ADD_FAILURE, surveyId, target.getObjectId(), target.getName()));
    }

    private void cmdSurveyDeletePlayer(Player user, String[] params) {
        if (params.length != 2) {
            PacketSendUtility.sendMessage(user, "Usage: //survey delplayer <survey_id>\n");
            return;
        }

        Player target = getTargetPlayer(user);
        if (target == null)
            return;

        int surveyId = Integer.parseInt(params[1]);
        boolean result = DAOManager.getDAO(SurveyDAO.class).deletePlayerSurvey(surveyId, target.getObjectId());
        if (result) {
            Survey survey = DAOManager.getDAO(SurveyDAO.class).loadSurvey(surveyId, user.getObjectId());
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYPLAYER_DEL_SUCCESS, survey.getTitle(), target.getName()));
        }
        else
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYPLAYER_DEL_FAILURE, surveyId, target.getObjectId(), target.getName()));
    }

    private void cmdSurveyInfo(Player user, String[] params) {
        if (params.length != 2) {
            PacketSendUtility.sendMessage(user,
                "Usage: //survey info <survey_id>\n" + 
                "Usage: //survey info all\n");
            return;
        }

        if (params[1].equals("all")) {
            List<Integer> surveyIds = DAOManager.getDAO(SurveyDAO.class).loadSurveyIds();
            if (surveyIds.isEmpty()) {
                PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEY_INFO_ALL_FAILURE));
                return;
            }
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEY_INFO_ALL_SUCCESS));
            for (Integer surveyId : surveyIds) {
                Survey survey = DAOManager.getDAO(SurveyDAO.class).loadSurvey(surveyId, user.getObjectId());
                if (survey != null)
                    PacketSendUtility.sendMessage(user, survey.toString());
                else
                    PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEY_INFO_FAILURE, surveyId));
            }
        }
        else {
            int surveyId = Integer.parseInt(params[1]);
            Survey survey = DAOManager.getDAO(SurveyDAO.class).loadSurvey(surveyId, user.getObjectId());
            if (survey != null)
                PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEY_INFO_SUCCESS) + survey.toString());
            else
                PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEY_INFO_FAILURE, surveyId));
        }
    }

    private void cmdSurveyEdit(Player user, String[] params) {
        String syntaxMsg = "Usage: //survey edit <survey_id> " +
            "<name: title | message | player_level_min | player_level_max | itemId | itemCount | survey_all > <value>\n";

        if (params.length != 4) {
            PacketSendUtility.sendMessage(user, syntaxMsg);
            return;
        }

        int surveyId = Integer.parseInt(params[1]);
        String name = params[2];
        boolean result = false;
        if (name.equals("title") || name.equals("message")) {
            result = DAOManager.getDAO(SurveyDAO.class).editSurvey(surveyId, name, params[3]);
        }
        else if (name.equals("player_level_min") || name.equals("player_level_max") || name.equals("itemId") || name.equals("itemCount") || name.equals("survey_all")) {
            result = DAOManager.getDAO(SurveyDAO.class).editSurvey(surveyId, name, Integer.parseInt(params[3]));
        }
        else {
            PacketSendUtility.sendMessage(user, syntaxMsg);
            return;            
        }

        if (result) {
            Survey survey = DAOManager.getDAO(SurveyDAO.class).loadSurvey(surveyId, user.getObjectId());
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEY_EDIT_SUCCESS) + survey.toString());
        }
        else
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEY_EDIT_FAILURE, surveyId, name, params[3]));
    }

    private void cmdSurveyEditOption(Player user, String[] params) {
        String syntaxMsg = "Usage: //survey editoption <survey_id> <option_id> <name: option_text | itemId | itemCount > <value>\n";
        if (params.length != 5) {
            PacketSendUtility.sendMessage(user, syntaxMsg);
            return;
        }

        int surveyId = Integer.parseInt(params[1]);
        int optionId = Integer.parseInt(params[2]);
        String name = params[3];
        boolean result = false;
        if (name.equals("option_text")) {
            result = DAOManager.getDAO(SurveyDAO.class).editSurveyOption(surveyId, optionId, name, params[4]);
        }            
        else if (name.equals("itemId") || name.equals("itemCount")) {
            result = DAOManager.getDAO(SurveyDAO.class).editSurveyOption(surveyId, optionId, name, Integer.parseInt(params[4]));
        }
        else {
            PacketSendUtility.sendMessage(user, syntaxMsg);
            return;        
        }

        if (result) {
            Survey survey = DAOManager.getDAO(SurveyDAO.class).loadSurvey(surveyId, user.getObjectId());
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYOPTION_EDIT_SUCCESS) + survey.toString());
        }
        else
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYOPTION_EDIT_FAILURE, surveyId, optionId, name, params[4]));
    }

    private void cmdSurveyAdd(Player user, String[] params) {
        if (params.length < 3 || params.length > 6) {
            String syntaxMsg = 
                "Usage: //survey add \"title\" \"message\" <level_min> <level_max> <item_id> <item_count> <survey_all: 0 (def) | 1 >\n" +
                "Usage: //survey add \"title\" \"message\" <level_min> <level_max> <survey_all: 0 (def) | 1 >\n" +
                "Usage: //survey add \"title\" \"message\" <item_id> <item_count> <survey_all: 0 (def) | 1 >\n" +
                "Usage: //survey add \"title\" \"message\" <item_id>\n" +
                "Usage: //survey add \"title\" \"message\"\n";
            PacketSendUtility.sendMessage(user, syntaxMsg);
            return;
        }

        String surveyTitle = params[1];
        String surveyMessage = params[2];
        int levelMin = 1;
        int levelMax = 55;
        int itemId = 0;
        int itemCount = 0;

        if (params.length > 3 && params.length <= 6) {
            // Check to see if an ItemID.
            if (Integer.parseInt(params[3]) > 100000000) {
                itemId = Integer.parseInt(params[3]);
                itemCount = (params.length == 5) ? Integer.parseInt(params[4]) : 1;
            }
            else {
                levelMin = Integer.parseInt(params[3]);
                levelMax = Integer.parseInt(params[4]);
            }
        }
        int surveyAll = (params.length == 6) ? Integer.parseInt(params[5]) : 0;

        Survey survey = new Survey(user.getObjectId(), surveyTitle, surveyMessage, levelMin, levelMax, itemId, itemCount, surveyAll);
        int surveyId = DAOManager.getDAO(SurveyDAO.class).insertSurvey(survey);
        if (surveyId > 0) {
            survey.setSurveyId(surveyId);
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEY_ADD_SUCCESS) + survey.toString());
        }
        else
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEY_ADD_FAILURE) + survey.toString());
    }

    private void cmdSurveyDelete(Player user, String[] params) {
        if (params.length != 2) {
            PacketSendUtility.sendMessage(user, "Usage: //survey del <surveyId>\n");
            return;
        }
        int surveyId = Integer.parseInt(params[1]);
        boolean result = DAOManager.getDAO(SurveyDAO.class).deleteSurvey(surveyId);
        if (result)
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEY_DEL_SUCCESS, surveyId));
        else
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEY_DEL_FAILURE, surveyId));
    }

    private void cmdSurveyAddOption(Player user, String[] params) {
        if (params.length < 3 || params.length > 5) {
            String syntaxMsg = "Usage: //survey addoption <surveyId> \"optionText\" <itemId> <itemCount>\n" +
                "Usage: //survey addoption <surveyId> \"optionText\" <itemId>\n" +
                "Usage: //survey addoption <surveyId> \"optionText\"\n";
            PacketSendUtility.sendMessage(user, syntaxMsg);
            return;
        }
        int surveyId = Integer.parseInt(params[1]);
        String optionText = params[2];
        int itemId = (params.length >= 4) ? Integer.parseInt(params[3]) : 0;
        int itemCount = 0;
        if (params.length == 4)
            itemCount = 1;
        if (params.length == 5)
            itemCount = Integer.parseInt(params[4]);

        SurveyOption surveyOption = new SurveyOption(surveyId, 0, optionText, itemId, itemCount);
        int optionId = DAOManager.getDAO(SurveyDAO.class).insertSurveyOption(surveyOption);

        if (optionId > 0) {
            Survey survey = DAOManager.getDAO(SurveyDAO.class).loadSurvey(surveyId, user.getObjectId());
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYOPTION_ADD_SUCCESS) + survey.toString());
        }
        else
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYOPTION_ADD_FAILURE) + surveyOption.toString());
    }

    private void cmdSurveyDeleteOption(Player user, String[] params) {
        String syntaxMsg = "Usage: //survey deloption <survey_id> <option_id>\n" +
            "Usage: //survey deloption <survey_id> all\n";

        if (params.length != 3) {
            PacketSendUtility.sendMessage(user, syntaxMsg);
            return;
        }

        int surveyId = Integer.parseInt(params[1]);
        if (surveyId <= 0) {
            PacketSendUtility.sendMessage(user, syntaxMsg);
            return;
        }

        boolean result;
        if (params[2].equals("all")) {
            result = DAOManager.getDAO(SurveyDAO.class).deleteSurveyOptions(surveyId);
        }
        else {
            int optionId = (params.length == 3) ? Integer.parseInt(params[2]) : 0;
            result = DAOManager.getDAO(SurveyDAO.class).deleteSurveyOption(surveyId, optionId);            
        }

        if (result) {
            Survey survey = DAOManager.getDAO(SurveyDAO.class).loadSurvey(surveyId, user.getObjectId());
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYOPTION_DEL_SUCCESS) + survey.toString());
        }
        else
            PacketSendUtility.sendMessage(user, LanguageHandler.translate(CustomMessageId.COMMAND_SURVEYOPTION_DEL_FAILURE));
    }

    private Player getTargetPlayer(Player owner) {
        Player target = null;
        if (owner.getTarget() instanceof Player)
            target = (Player) owner.getTarget();

        if (target == null)
            PacketSendUtility.sendMessage(owner, LanguageHandler.translate(CustomMessageId.COMMAND_TARGET_PLAYER_NOT_ACQUIRED));
        return target;
    }
}
