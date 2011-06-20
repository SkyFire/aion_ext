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

/**
 * @author ZeroSignal
 */
public class SurveyOption {

    protected int surveyId;
    protected int optionId;
    protected String optionText;
    protected int itemId;
    protected int itemCount;
    protected int playerCount;

    public SurveyOption(int surveyId, int optionId, String optionText) {
        init(surveyId, optionId, optionText, 0, 0);
    }
    public SurveyOption(int surveyId, int optionId, String optionText, int itemId, int itemCount) {
        init(surveyId, optionId, optionText, itemId, itemCount);
    }

    protected void init(int surveyId, int optionId, String optionText, int itemId, int itemCount) {
        this.surveyId = surveyId;
        this.optionId = optionId;
        this.optionText = optionText;
        this.itemId = itemId;
        this.itemCount = itemCount;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public int getOptionId() {
        return optionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public int getItemId() {
        return itemId;
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public String toString() {
        String msg = "\n\toption_id: " + optionId +
            "\n\toption_text: \"" + optionText + "\"\n";
        if (itemId > 0)
            msg += "\titemId: " + itemId + ", itemCount: " + itemCount + "\n";
        return msg;
    }
}
