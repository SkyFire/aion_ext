/**
 * This file is part of Aion X Emu <aionxemu.com>
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
package gameserver.model;

/**
 * @author zdead
 */
public class NpcShout {
    private int npcTemplateId;
    private int shoutMessageId;
    private int interval;

    public NpcShout(int npcId, int msgId, int itvl) {
        this.npcTemplateId = npcId;
        this.shoutMessageId = msgId;
        this.interval = itvl;
    }

    public int getNpcTemplateId() {
        return npcTemplateId;
    }

    public int getMessageId() {
        return shoutMessageId;
    }

    public int getInterval() {
        return interval;
    }

}
