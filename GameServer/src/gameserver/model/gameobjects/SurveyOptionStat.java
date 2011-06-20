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

/**
 * @author ZeroSignal
 */
public class SurveyOptionStat extends SurveyOption {
    private int playerCount;

    public SurveyOptionStat(int surveyId, int optionId, String optionText, int itemId, int itemCount, int playerCount) {
        super(surveyId, optionId, optionText, itemId, itemCount);
        this.playerCount = playerCount;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    @Override
    public String toString() {
        String msg = super.toString();
        msg += "\tplayerCount: " + playerCount + "\n";
        return msg;
    }
}
