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

package gameserver.model.gameobjects;

import gameserver.model.gameobjects.SurveyOption;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZeroSignal, ginhol
 */
public class Survey {

    private int surveyId;
    private int playerId;
    private String title;
    private String message;
    private int itemId;
    private int itemCount;
    private int playerLevelMin;
    private int playerLevelMax;
    private int surveyAll;
    private List<SurveyOption> surveyOptions = new ArrayList<SurveyOption>();

    public Survey(int surveyId, int playerId, String title, String message, int playerLevelMin, int playerLevelMax, int itemId, int itemCount, int surveyAll, String selectText) {
        init(surveyId, playerId, title, message, itemId, itemCount, playerLevelMin, playerLevelMax, surveyAll);
        this.surveyOptions.add(new SurveyOption(surveyId, 1, selectText, 0, 0));
    }

    public Survey(int surveyId, int playerId, String title, String message, int playerLevelMin, int playerLevelMax, int itemId, int itemCount, int surveyAll, List<SurveyOption> surveyOptions) {
        init(surveyId, playerId, title, message, itemId, itemCount, playerLevelMin, playerLevelMax, surveyAll);
        this.surveyOptions = surveyOptions;
    }

    public Survey(int playerId, String title, String message, int playerLevelMin, int playerLevelMax, int itemId, int itemCount, int surveyAll) {
        init(0, playerId, title, message, itemId, itemCount, playerLevelMin, playerLevelMax, surveyAll);
    }

    protected void init(int surveyId, int playerId, String title, String message, int itemId, int itemCount, int playerLevelMin, int playerLevelMax, int surveyAll) {
        this.surveyId = surveyId;
        this.playerId = playerId;
        this.title = title;
        this.message = message;
        this.itemId = itemId;
        this.itemCount = itemCount;
        this.playerLevelMin = playerLevelMin;
        this.playerLevelMax = playerLevelMax;
        this.surveyAll = surveyAll;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public int getItemId() {
        return itemId;
    }

    public int getPlayerLevelMin() {
        return playerLevelMin;
    }

    public int getPlayerLevelMax() {
        return playerLevelMax;
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getSurveyAll() {
        return surveyAll;
    }

    public List<SurveyOption> getSurveyOptions() {
        return surveyOptions;
    }

    public void setSurveyOptions(List<SurveyOption>  surveyOptions) {
        this.surveyOptions = surveyOptions;
    }

    public void addSurveyOption(SurveyOption surveyOption) {
        this.surveyOptions.add(surveyOption);
    }

    public int getSurveyOptionsSize() {
        return surveyOptions.size();
    }

    public SurveyOption getSurveyOption(int index) {
        if (surveyOptions.isEmpty())
            return null;
        if (index < 0 || index >= surveyOptions.size())
            return null;
        return surveyOptions.get(index);
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public String toString() {
        String msg = "\nsurvey_id: " + surveyId +
            "\nowner_id: " + playerId +
            "\ntitle: \"" + title + "\"" +
            "\nmessage: \"" + message + "\"";
        if (itemId > 0)    
            msg += "\nitemId: " + itemId + ", itemCount: " + itemCount;
        msg += "\nplayer_level_min: " + playerLevelMin +
            ", player_level_max: " + playerLevelMax +
            "\nsurvey_all: " + surveyAll + "\n";
        if (!surveyOptions.isEmpty()) {
            for (SurveyOption surveyOption : surveyOptions) {
                msg += surveyOption.toString();
            }
        }
        return msg;
    }
}