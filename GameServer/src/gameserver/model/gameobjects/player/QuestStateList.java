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
package gameserver.model.gameobjects.player;

import gameserver.questEngine.model.QuestState;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author MrPoke
 */
public class QuestStateList {
    private static final Logger log = Logger.getLogger(QuestStateList.class);

    private final SortedMap<Integer, QuestState> _quests;

    /**
     * Creates an empty quests list
     */
    public QuestStateList() {
        _quests = Collections.synchronizedSortedMap(new TreeMap<Integer, QuestState>());
    }

    public boolean addQuest(int questId, QuestState questState) {
        if (_quests.containsKey(questId)) {
            log.warn("Duplicate quest. ");
            return false;
        }
        _quests.put(questId, questState);
        return true;
    }

    public boolean removeQuest(int questId) {
        if (_quests.containsKey(questId)) {
            _quests.remove(questId);
            return true;
        }
        return false;
    }

    public QuestState getQuestState(int questId) {
        return _quests.get(questId);
    }

    public Collection<QuestState> getAllQuestState() {
        return _quests.values();
    }
}
