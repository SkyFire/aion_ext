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

package mysql5;

import com.aionemu.commons.database.DatabaseFactory;
import gameserver.dao.SurveyDAO;
import gameserver.model.gameobjects.Survey;
import gameserver.model.gameobjects.SurveyOption;
import gameserver.model.gameobjects.SurveyOptionStat;
import gameserver.model.gameobjects.player.Player;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZeroSignal, ginho1
 */
public class MySQL5SurveyDAO extends SurveyDAO {
    private static final Logger log = Logger.getLogger(MySQL5InventoryDAO.class);

    public static final String DELETE_QUERY =
        "DELETE FROM `surveys` WHERE `survey_id`=?";
    public static final String SELECT_SURVEY_ID_QUERY =
        "SELECT `survey_id` FROM `surveys` WHERE `survey_id`=?";
    public static final String SELECT_SURVEY_IDS_QUERY =
        "SELECT `survey_id` FROM `surveys`";
    public static final String SELECT_SURVEY_LIST_QUERY =
        "SELECT `survey_id`,`owner_id`,`title`,`message`,`player_level_min`,`player_level_max`,`itemId`,`itemCount`,`survey_all` FROM `surveys`" +
        "WHERE `player_level_min`<=? AND `player_level_max`>=? AND `survey_all`=1 AND `survey_id` NOT IN (" +
        "SELECT `survey_id` FROM `player_surveys` WHERE `player_id`=?)" +
        " OR `survey_id` IN (" +
        "SELECT `survey_id` FROM `player_surveys` WHERE `player_id`=? AND option_id=0)";
    public static final String SELECT_SURVEY_QUERY = 
        "SELECT `survey_id`,`owner_id`,`title`,`message`,`player_level_min`,`player_level_max`,`itemId`,`itemCount`,`survey_all` "+
        "FROM `surveys` WHERE `survey_id`=?";
    public static final String INSERT_SURVEY_QUERY =
        "INSERT INTO `surveys` (`owner_id`,`title`,`message`,`player_level_min`,`player_level_max`,`itemId`,`itemCount`, `survey_all`) VALUES(?,?,?,?,?,?,?,?)";
    public static final String SELECT_SURVEY_OPTION_MAX_OPTION_ID_QUERY =
        "SELECT MAX(`option_id`) `max_option_id` FROM `surveys_option` WHERE `survey_id`=?";
    public static final String SELECT_SURVEY_OPTION_ALL_QUERY =
        "SELECT `option_id`,`option_text`,`itemId`,`itemCount`"+ 
        "FROM `surveys_option` WHERE `survey_id`=?";
    public static final String SELECT_SURVEY_OPTION_QUERY =
        "SELECT `option_text`,`itemId`,`itemCount`" +
        "FROM `surveys_option` WHERE `survey_id`=? AND `option_id`=?";
    public static final String SELECT_SURVEY_OPTION_STATS_QUERY =
        "SELECT so.`survey_id`, so.`option_id`,`option_text`,`itemId`,`itemCount`," +
            "COUNT(ps.`option_id`) AS `playerCount` " +
        "FROM `surveys_option` so LEFT OUTER JOIN `player_surveys` ps " +
        "ON so.`survey_id`=ps.`survey_id` AND so.`option_id`=ps.`option_id` " +
        "WHERE so.`survey_id`=? " +
        "GROUP BY so.`option_id`";
    public static final String INSERT_SURVEY_OPTION_QUERY =
        "INSERT INTO `surveys_option` (`survey_id`,`option_id`,`option_text`,`itemId`,`itemCount`) VALUES(?,?,?,?,?)";
    public static final String DELETE_SURVEY_OPTION_QUERY =
        "DELETE FROM `surveys_option` WHERE `survey_id`=? AND `option_id`=?";
    public static final String DELETE_SURVEY_OPTION_ALL_QUERY =
        "DELETE FROM `surveys_option` WHERE `survey_id`=?";
    public static final String UPDATE_SURVEY_OPTION_FIX_OPTION_ID_QUERY =
        "UPDATE `surveys_option` SET `option_id`=(`option_id`-1) WHERE `survey_id`=? AND `option_id`>=?";
    public static final String UPDATE_PLAYER_SURVEY_QUERY =
        "UPDATE `player_surveys` SET `option_id`=? WHERE `survey_id`=? AND `player_id`=?";
    public static final String INSERT_PLAYER_SURVEY_QUERY =
        "INSERT INTO `player_surveys` (`survey_id`,`player_id`,`option_id`) VALUES(?,?,?)";
    public static final String SELECT_PLAYER_SURVEY_QUERY =
        "SELECT `option_id` FROM `player_surveys` WHERE `survey_id`=? AND `player_id`=?";
    public static final String DELETE_PLAYER_SURVEY_QUERY =
        "DELETE FROM `player_surveys` WHERE `survey_id`=? and `player_id`=?";

    @Override
    public boolean supports(String arg0, int arg1, int arg2) {
        return MySQL5DAOUtils.supports(arg0, arg1, arg2);
    }

    @Override
    public boolean deleteSurvey(int surveyId) {
        boolean result;
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
            stmt.setInt(1, surveyId);
            stmt.execute();
            result = (stmt.getUpdateCount() != -1);
            stmt.close();
        }
        catch (Exception e) {
            log.error("Error delete surveyId: " + surveyId, e);
            return false;
        }
        finally {
            DatabaseFactory.close(con);
        }
        if (!deleteSurveyOptions(surveyId))
            return false;
        return result;
    }

    @Override
    public boolean deleteSurveyOptions(int surveyId) {
        boolean result = false;
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_SURVEY_OPTION_ALL_QUERY);
            stmt.setInt(1, surveyId);
            result = (stmt.executeUpdate() != 0);
            stmt.close();
        }
        catch (Exception e) {
            log.error("Error delete SurveyOptions for surveyId: " + surveyId +
                " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
            return result;
        }
    }

    @Override
    public boolean deleteSurveyOption(int surveyId, int optionId) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_SURVEY_OPTION_QUERY);
            stmt.setInt(1, surveyId);
            stmt.setInt(2, optionId);
            if (stmt.executeUpdate() == 0)
                return false;
            if (!updateSurveyOptionFixOptionIds(surveyId, optionId))
                return false;
        }
        catch (Exception e) {
            log.error("Error delete SurveyOptions for surveyId: " + surveyId +
                ", optionId: " + optionId +
                " from DB: " + e.getMessage(), e);
            return false;
        }
        finally {
            DatabaseFactory.close(con);
        }
        return true;
    }

    @Override
    public boolean deletePlayerSurvey(int surveyId, int playerId) {
        boolean result = false;
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_PLAYER_SURVEY_QUERY);
            stmt.setInt(1, surveyId);
            stmt.setInt(2, playerId);
            result = (stmt.executeUpdate() != 0);
            stmt.close();
        }
        catch (Exception e) {
            log.error("Error delete Player Survey for surveyId: " + surveyId + ", playerId: " + playerId, e);
        }
        finally {
            DatabaseFactory.close(con);
            return result;
        }
    }

    @Override
    public int insertSurvey(Survey survey) {
        int id = 0;
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(INSERT_SURVEY_QUERY, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, survey.getPlayerId());
            stmt.setString(2, survey.getTitle());
            stmt.setString(3, survey.getMessage());
            stmt.setInt(4, survey.getPlayerLevelMin());
            stmt.setInt(5, survey.getPlayerLevelMax());
            stmt.setInt(6, survey.getItemId());
            stmt.setInt(7, survey.getItemCount());
            stmt.setInt(8, survey.getSurveyAll());
            stmt.execute();
            ResultSet res = stmt.getGeneratedKeys();
            res.next();
            id = res.getInt(1);
            res.close();
            stmt.close();
        }
        catch (Exception e) {
            log.error("Cannot insert survey", e);
        }
        finally {
            DatabaseFactory.close(con);
        }
        return id;
    }

    @Override
    public int insertSurveyOption(SurveyOption surveyOption) {
        if (!isSurveyExist(surveyOption.getSurveyId()))
            return 0;

        Connection con = null;
        int optionId = getSurveyOptionIdMax(surveyOption.getSurveyId()) + 1;
        surveyOption.setOptionId(optionId);
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(INSERT_SURVEY_OPTION_QUERY);
            stmt.setInt(1, surveyOption.getSurveyId());
            stmt.setInt(2, surveyOption.getOptionId());
            stmt.setString(3, surveyOption.getOptionText());
            stmt.setInt(4, surveyOption.getItemId());
            stmt.setInt(5, surveyOption.getItemCount());
            stmt.execute();
            stmt.close();
        }
        catch (Exception e) {
            log.error("Cannot insert survey option for surveyId: " + surveyOption.getSurveyId() +
                " from DB: " + e.getMessage(), e);
            return 0;
        }
        finally {
            DatabaseFactory.close(con);
        }
        return optionId;
    }

    private int getSurveyOptionIdMax(int surveyId) {
        int optionId = 0;
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            // If the optionId is 0 then retrieve the Maximum+1.
            PreparedStatement stmt = con.prepareStatement(SELECT_SURVEY_OPTION_MAX_OPTION_ID_QUERY);
            stmt.setInt(1, surveyId);
            ResultSet res = stmt.executeQuery();
            while(res.next()) {
                optionId = res.getInt("max_option_id");
            }
            res.close();
            stmt.close();
        }
        catch (Exception e) {
            log.error("Cannot get max survey option id for surveyId: " + surveyId +
                " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
        }
        return optionId;
    }

    @Override
    public boolean insertPlayerSurvey(int surveyId, int playerId, int optionId) {
        int optionIdCheck = loadPlayerSurvey(surveyId, playerId);
        if (optionIdCheck != -1)
            return false;
        Connection con = null;
        boolean result = false;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(INSERT_PLAYER_SURVEY_QUERY);
            stmt.setInt(1, surveyId);
            stmt.setInt(2, playerId);
            stmt.setInt(3, optionId);
            result = (stmt.executeUpdate() != 0);
            stmt.close();
        }
        catch (Exception e) {
            log.error("Cannot insert player survey", e);
        }
        finally {
            DatabaseFactory.close(con);
            return result;
        }
    }

    @Override
    public boolean updatePlayerSurvey(int surveyId, int playerId, int optionId) {
        boolean result = false;
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(UPDATE_PLAYER_SURVEY_QUERY);
            stmt.setInt(1, optionId);
            stmt.setInt(2, surveyId);
            stmt.setInt(3, playerId);
            result = (stmt.executeUpdate() != 0);
            stmt.close();
        }
        catch (Exception e) {
            log.error("Error update Player Survey for surveyId: " + surveyId + ", playerId: " + playerId +
                ", optionId: " + optionId + " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
            return result;
        }
    }

    @Override
    public int loadPlayerSurvey(int surveyId, int playerId) {
        int optionId = -1;
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_PLAYER_SURVEY_QUERY);
            stmt.setInt(1, surveyId);
            stmt.setInt(2, playerId);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                optionId = rset.getInt("option_id");
            }
            rset.close();
            stmt.close();
        }
        catch (Exception e) {
            log.fatal("Could not restore SurveyPlayer optionId for player: " + playerId + " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
        }
        return optionId;
    }

    @Override
    public List<Integer> loadSurveyIds() {
        List<Integer> surveyIds = new ArrayList<Integer>();
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_SURVEY_IDS_QUERY);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                surveyIds.add(rset.getInt("survey_id"));
            }
            rset.close();
            stmt.close();
        }
        catch (Exception e) {
            log.fatal("Could not restore surveyIds from DB: " + e.getMessage(), e);
            return null;
        }
        finally {
            DatabaseFactory.close(con);
        }
        return surveyIds;
    }

    @Override
    public Survey loadSurvey(int surveyId, int playerId) {
        Survey survey = null;
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_SURVEY_QUERY);
            stmt.setInt(1, surveyId);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                int ownerId = rset.getInt("owner_id");
                String title = rset.getString("title");
                String message = rset.getString("message");
                int playerLevelMin = rset.getInt("player_level_min");
                int playerLevelMax = rset.getInt("player_level_max");
                int itemId = rset.getInt("itemId");
                int itemCount = rset.getInt("itemCount");
                int surveyAll = rset.getInt("survey_all");

                survey = new Survey(surveyId, ownerId, title, message, playerLevelMin, playerLevelMax,
                    itemId, itemCount, surveyAll, loadSurveyOptions(surveyId));
            }
            rset.close();
            stmt.close();
        }
        catch (Exception e) {
            log.fatal("Could not restore Survey surveyId: " + surveyId +
                " for playerId: " + playerId + " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
        }
        return survey;
    }

    @Override
    public SurveyOption loadSurveyOption(int surveyId, int optionId) {
        SurveyOption surveyOption = null;
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_SURVEY_OPTION_QUERY);
            stmt.setInt(1, surveyId);
            stmt.setInt(2, optionId);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                String optionText = rset.getString("option_text");
                int itemId = rset.getInt("itemId");
                int itemCount = rset.getInt("itemCount");
                surveyOption = new SurveyOption(surveyId, optionId, optionText, itemId, itemCount);
            }
            rset.close();
            stmt.close();
        }
        catch (Exception e) {
            log.fatal("Could not restore SurveyOption surveyId: " + surveyId +
                ", optionId: " + optionId +
                " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
        }
        return surveyOption;
    }

    @Override
    public List<Survey> loadSurveys(Player player) {
        final List<Survey> surveys = new ArrayList<Survey>();
        int playerId = player.getObjectId();
        int playerLevel = player.getCommonData().getLevel();

        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_SURVEY_LIST_QUERY);
            stmt.setInt(1, playerLevel);
            stmt.setInt(2, playerLevel);
            stmt.setInt(3, playerId);
            stmt.setInt(4, playerId);

            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                int surveyId = rset.getInt("survey_id");
                int ownerId = rset.getInt("owner_id");
                String title = rset.getString("title");
                String message = rset.getString("message");
                int playerLevelMin = rset.getInt("player_level_min");
                int playerLevelMax = rset.getInt("player_level_max");
                int itemId = rset.getInt("itemId");
                int itemCount = rset.getInt("itemCount");
                int surveyAll = rset.getInt("survey_all");

                Survey survey = new Survey(surveyId, ownerId, title, message, playerLevelMin, playerLevelMax,
                    itemId, itemCount, surveyAll, loadSurveyOptions(surveyId));
                surveys.add(survey);
            }
            rset.close();
            stmt.close();
        }
        catch (Exception e) {
            log.fatal("Could not restore Survey data for player: " + playerId + " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
        }
        return surveys;
    }

    @Override
    public List<SurveyOption> loadSurveyOptions(int surveyId) {
        final List<SurveyOption> surveyOptions = new ArrayList<SurveyOption>();

        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_SURVEY_OPTION_ALL_QUERY);
            stmt.setInt(1, surveyId);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                int optionId = rset.getInt("option_id");
                String optionText = rset.getString("option_text");
                int itemId = rset.getInt("itemId");
                int itemCount = rset.getInt("itemCount");
                SurveyOption surveyOption = new SurveyOption(surveyId, optionId, optionText, itemId, itemCount);
                surveyOptions.add(surveyOption);
            }
            rset.close();
            stmt.close();
        }
        catch (Exception e) {
            log.fatal("Could not restore SurveyOptions for surveyId: " + surveyId + " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
        }
        return surveyOptions;
    }

    @Override
    public Survey loadSurveyStat(int surveyId) {
        Survey survey = null;
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_SURVEY_QUERY);
            stmt.setInt(1, surveyId);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                int ownerId = rset.getInt("owner_id");
                String title = rset.getString("title");
                String message = rset.getString("message");
                int playerLevelMin = rset.getInt("player_level_min");
                int playerLevelMax = rset.getInt("player_level_max");
                int itemId = rset.getInt("itemId");
                int itemCount = rset.getInt("itemCount");
                int surveyAll = rset.getInt("survey_all");

                survey = new Survey(surveyId, ownerId, title, message, playerLevelMin, playerLevelMax,
                    itemId, itemCount, surveyAll, loadSurveyOptionStats(surveyId));
            }
            rset.close();
            stmt.close();
        }
        catch (Exception e) {
            log.fatal("Could not restore SurveyStats surveyId: " + surveyId +
                " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
        }
        return survey;    
    }

    @Override
    public List<SurveyOption> loadSurveyOptionStats(int surveyId) {
        List<SurveyOption> surveyOptionsStats = new ArrayList<SurveyOption>();
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_SURVEY_OPTION_STATS_QUERY);
            stmt.setInt(1, surveyId);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                int optionId = rset.getInt("option_id");
                String optionText = rset.getString("option_text");
                int itemId = rset.getInt("itemId");
                int itemCount = rset.getInt("itemCount");
                int playerCount = rset.getInt("playerCount");
                SurveyOptionStat surveyOptionStat = new SurveyOptionStat(surveyId, optionId, optionText, itemId, itemCount, playerCount);
                surveyOptionsStats.add(surveyOptionStat);
            }
            rset.close();
            stmt.close();
        }
        catch (Exception e) {
            log.fatal("Could not restore SurveyOptionStats for surveyId: " + surveyId +
                " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
        }
        return surveyOptionsStats;
    }

    @Override
    public boolean editSurvey(int surveyId, String columnName, String columnValue)
    {
        boolean result = false;
        String sql = "UPDATE `surveys` SET `" + columnName + "`=? WHERE `survey_id`=?";
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, columnValue);
            stmt.setInt(2, surveyId);
            result = (stmt.executeUpdate() != 0);
            stmt.close();
        }
        catch (Exception e) {
            log.error("Error edit Survey for surveyId: " + surveyId +
                ", columnName: " + columnName +
                ", columnValue: \"" + columnValue + "\"" +
                " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
            return result;
        }
    }

    @Override
    public boolean editSurvey(int surveyId, String columnName, int columnValue)
    {
        boolean result = false;
        String sql = "UPDATE `surveys` SET `" + columnName + "`=? WHERE `survey_id`=?";
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, columnValue);
            stmt.setInt(2, surveyId);
            result = (stmt.executeUpdate() != 0);
            stmt.close();
        }
        catch (Exception e) {
            log.error("Error edit Survey for surveyId: " + surveyId +
                ", columnName: " + columnName +
                ", columnValue: " + columnValue +
                " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
            return result;
        }

    }

    @Override
    public boolean editSurveyOption(int surveyId, int optionId, String columnName, String columnValue)
    {
        boolean result = false;
        String sql = "UPDATE `surveys_option` SET `" + columnName + "`=? WHERE `survey_id`=? AND `option_id`=?";
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, columnValue);
            stmt.setInt(2, surveyId);
            stmt.setInt(3, optionId);
            result = (stmt.executeUpdate() != 0);
            stmt.close();
        }
        catch (Exception e) {
            log.error("Error edit SurveyOption for surveyId: " + surveyId +
                ", optionId: " + optionId + ", columnName: " + columnName +
                ", columnValue: \"" + columnValue + "\"" +
                " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
            return result;
        }
    }

    @Override
    public boolean editSurveyOption(int surveyId, int optionId, String columnName, int columnValue) {
        boolean result = false;
        String sql = "UPDATE `surveys_option` SET `" + columnName + "`=? WHERE `survey_id`=? AND `option_id`=?";
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, columnValue);
            stmt.setInt(2, surveyId);
            stmt.setInt(3, optionId);
            result = (stmt.executeUpdate() != 0);
            stmt.close();
        }
        catch (Exception e) {
            log.error("Error edit SurveyOption for surveyId: " + surveyId +
                ", optionId: " + optionId + ", columnName: " + columnName +
                ", columnValue: " + columnValue +
                " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
            return result;
        }
    }

    private boolean updateSurveyOptionFixOptionIds(int surveyId, int optionId) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            // If the optionId is 0 then retrieve the Maximum+1.
            PreparedStatement stmt = con.prepareStatement(UPDATE_SURVEY_OPTION_FIX_OPTION_ID_QUERY);
            stmt.setInt(1, surveyId);
            stmt.setInt(2, optionId);
            stmt.execute();
            stmt.close();
        }
        catch (Exception e) {
            log.error("Cannot update SurveyOption and fix optionId's for surveyId: " + surveyId +
                " optionId: " + optionId +
                " from DB: " + e.getMessage(), e);
            return false;
        }
        finally {
            DatabaseFactory.close(con);
        }
        return true;
    }

    private boolean isSurveyExist(int surveyId) {
        boolean result = false;
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_SURVEY_ID_QUERY);
            stmt.setInt(1, surveyId);
            ResultSet rset = stmt.executeQuery();
            result = rset.next();
            rset.close();
            stmt.close();
        }
        catch (Exception e) {
            log.fatal("Could not check isSurveyExist for surveyId: " + surveyId + " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
            return result;
        }
    }

}
