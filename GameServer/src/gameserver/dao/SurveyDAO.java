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

package gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import gameserver.model.gameobjects.Survey;
import gameserver.model.gameobjects.SurveyOption;
import gameserver.model.gameobjects.player.Player;

import java.util.List;

/**
 * @author ZeroSignal, ginhol
 */
public abstract class SurveyDAO implements DAO {

    @Override
    public final String getClassName() {
        return SurveyDAO.class.getName();
    }

    public abstract boolean deleteSurveyOptions(int surveyId);

    public abstract boolean deleteSurveyOption(int surveyId, int optionId);

    public abstract boolean deleteSurvey(int surveyId);

    public abstract boolean deletePlayerSurvey(int surveyId, int playerId);

    public abstract int insertSurvey(Survey survey);
    public abstract int insertSurveyOption(SurveyOption surveyOption);

    public abstract int loadPlayerSurvey(int surveyId, int playerId);
    public abstract boolean insertPlayerSurvey(int surveyId, int playerId, int optionId);
    public abstract boolean updatePlayerSurvey(int surveyId, int playerId, int optionId);

    public abstract List<Integer> loadSurveyIds();
    public abstract Survey loadSurvey(int surveyId, int playerId);
    public abstract List<Survey> loadSurveys(Player player);
    public abstract SurveyOption loadSurveyOption(int surveyId, int optionId);
    public abstract List<SurveyOption> loadSurveyOptions(int surveyId);

    public abstract Survey loadSurveyStat(int surveyId);
    public abstract List<SurveyOption> loadSurveyOptionStats(int surveyId);

    public abstract boolean editSurvey(int surveyId, String columnName, String columnValue);
    public abstract boolean editSurvey(int surveyId, String columnName, int columnValue);

    public abstract boolean editSurveyOption(int surveyId, int optionId, String columnName, String columnValue);
    public abstract boolean editSurveyOption(int surveyId, int optionId, String columnName, int columnValue);
}
