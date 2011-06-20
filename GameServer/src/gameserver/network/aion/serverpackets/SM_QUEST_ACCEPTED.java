/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.questEngine.model.QuestStatus;

import java.nio.ByteBuffer;

/**
 * @author MrPoke
 */
public class SM_QUEST_ACCEPTED extends AionServerPacket {
    private int questId;
    private int status;
    private int step;
    private int action;
    private int timer;

    // accept = 1 - get quest 2 - quest steps/hand in 3 - fail/delete 4 - display client timer

    /**
     * Accept Quest(1)
     */
    public SM_QUEST_ACCEPTED(int questId, int status, int step) {
        this.action = 1;
        this.questId = questId;
        this.status = status;
        this.step = step;
    }

    /**
     * Quest Steps/Finish (2)
     */
    public SM_QUEST_ACCEPTED(int questId, QuestStatus status, int step) {
        this.action = 2;
        this.questId = questId;
        this.status = status.value();
        this.step = step;
    }

    /**
     * Delete Quest(3)
     */
    public SM_QUEST_ACCEPTED(int questId) {
        this.action = 3;
        this.questId = questId;
        this.status = 0;
        this.step = 0;
    }

    /**
     * Display Timer(4)
     */
    public SM_QUEST_ACCEPTED(int questId, int timer) {
        this.action = 4;
        this.questId = questId;
        this.timer = timer;
        this.step = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        switch (action) {
            case 1:
                writeC(buf, action);
                writeD(buf, questId);
                writeC(buf, status);// quest status goes by ENUM value
                writeC(buf, 0x0);
                writeD(buf, step);// current quest step
                writeH(buf, 0);
                break;
            case 2:
                writeC(buf, action);
                writeD(buf, questId);
                writeC(buf, status);// quest status goes by ENUM value
                writeC(buf, 0x0);
                writeD(buf, step);// current quest step
                writeH(buf, 0);
                break;
            case 3:
                writeC(buf, action);
                writeD(buf, questId);
                writeC(buf, status);// quest status goes by ENUM value
                writeD(buf, step);// current quest step
                break;
            case 4:
                writeC(buf, action);
                writeD(buf, questId);
                writeD(buf, timer);// sets client timer ie 84030000 is 900 seconds/15 mins
                writeC(buf, 0x01);
                writeH(buf, 0x0);
                writeC(buf, 0x01);
		}
	}
}
