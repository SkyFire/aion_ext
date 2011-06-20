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
package gameserver.model.templates.quest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MrPoke
 */
public class NpcQuestData {
    private final List<Integer> onQuestStart;
    private final List<Integer> onKillEvent;
    private final List<Integer> onTalkEvent;
    private final List<Integer> onAttackEvent;
    private final List<Integer> onActionItemEvent;

    public NpcQuestData() {
        onQuestStart = new ArrayList<Integer>();
        onKillEvent = new ArrayList<Integer>();
        onTalkEvent = new ArrayList<Integer>();
        onAttackEvent = new ArrayList<Integer>();
        onActionItemEvent = new ArrayList<Integer>();
    }

    public void addOnQuestStart(int questId) {
        if (!onQuestStart.contains(questId)) {
            onQuestStart.add(questId);
        }
    }

    public List<Integer> getOnQuestStart() {
        return onQuestStart;
    }

    public void addOnAttackEvent(int questId) {
        if (!onAttackEvent.contains(questId)) {
            onAttackEvent.add(questId);
        }
    }

    public List<Integer> getOnAttackEvent() {
        return onAttackEvent;
    }

    public void addOnKillEvent(int questId) {
        if (!onKillEvent.contains(questId)) {
            onKillEvent.add(questId);
        }
    }

    public List<Integer> getOnKillEvent() {
        return onKillEvent;
    }

    public void addOnTalkEvent(int questId) {
        if (!onTalkEvent.contains(questId)) {
            onTalkEvent.add(questId);
        }
    }

    public List<Integer> getOnTalkEvent() {
        return onTalkEvent;
    }

    public void addOnActionItemEvent(int questId) {
        if (!onActionItemEvent.contains(questId)) {
            onActionItemEvent.add(questId);
        }
    }

    public List<Integer> getOnActionItemEvent() {
        return onActionItemEvent;
    }
}
